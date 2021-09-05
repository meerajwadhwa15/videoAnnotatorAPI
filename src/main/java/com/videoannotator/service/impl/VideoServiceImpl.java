package com.videoannotator.service.impl;

import com.videoannotator.constant.RoleEnum;
import com.videoannotator.constant.VideoStatusEnum;
import com.videoannotator.exception.NotFoundException;
import com.videoannotator.exception.PermissionDeniedException;
import com.videoannotator.exception.SegmentOverlapException;
import com.videoannotator.model.*;
import com.videoannotator.model.mapper.ObjectMapper;
import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoPlaylistRequest;
import com.videoannotator.model.request.VideoRequest;
import com.videoannotator.model.response.VideoDetailResponse;
import com.videoannotator.model.response.VideoListResponse;
import com.videoannotator.model.response.VideoResponse;
import com.videoannotator.repository.*;
import com.videoannotator.repository.specification.VideoSpecification;
import com.videoannotator.service.IVideoService;
import com.videoannotator.util.UserUtils;
import com.videoannotator.util.VideoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements IVideoService {

    private static final String SUCCESS = "message.success";

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;
    private final VideoSegmentRepository segmentRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final PlaylistRepository playlistRepository;
    private final UserPlaylistRepository userPlaylistRepository;
    private final VideoSpecification videoSpecification;
    private final UserUtils userUtils;
    private final VideoUtils videoUtils;

    @Override
    public VideoListResponse listVideo(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long categoryId, Long subcategoryId) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(null, videoUtils.setSubcategoryList(subcategoryId, categoryId, subCategoryRepository, categoryRepository), null, keyword), paging);
            return videoUtils.setVideoListResponse(videos, pageNo, videoUtils.setListVideo(videos.getContent(), true));
        } else {
            var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(user, videoUtils.setSubcategoryList(subcategoryId, categoryId, subCategoryRepository, categoryRepository), null, keyword), paging);
            return videoUtils.setVideoListResponse(videos, pageNo, videoUtils.setListVideo(videos.getContent(), false));
        }
    }

    @Override
    public VideoResponse detailVideo(Long id) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            return setVideoResponse(video, true);
        } else {
            if (video.getUserList().stream().map(User::getId).collect(Collectors.toSet()).contains(userDetails.getId())) {
                return setVideoResponse(video, false);
            } else {
                throw new PermissionDeniedException();
            }
        }
    }

    @Override
    public VideoResponse assignVideo(VideoAssignRequest assignRequest, Long videoId) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        if (RoleEnum.ADMIN.getValue() != userDetails.getRoleId()) {
            throw new PermissionDeniedException();
        }
        List<User> userList = userRepository.findAllByRoleIsNotAndIdInAndActive(
                roleRepository.findById(RoleEnum.ADMIN.getValue()).orElseThrow(NotFoundException::new), assignRequest.getUserId(), true);
        var video = videoRepository.findById(videoId).orElseThrow(NotFoundException::new);
        if (userList.isEmpty()) {
            video.setStatus(VideoStatusEnum.NOT_ASSIGNED);
        } else {
            video.setStatus(VideoStatusEnum.ASSIGNED);
        }
        video.setUserList(userList);
        var videoSaved = videoRepository.save(video);
        return setVideoResponse(videoSaved, true);
    }

    @Override
    public VideoResponse addVideo(VideoRequest request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            Video video = new Video();
            video.setStatus(VideoStatusEnum.NOT_ASSIGNED);
            ObjectMapper.INSTANCE.requestToVideo(video, request, subCategoryRepository);
            var videoSaved = videoRepository.save(video);
            return setVideoResponse(videoSaved, true);
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public VideoResponse updateVideo(Long id, VideoRequest request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
            ObjectMapper.INSTANCE.requestToVideo(video, request, subCategoryRepository);
            var videoSaved = videoRepository.save(video);
            return setVideoResponse(videoSaved, true);
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public String deleteVideo(Long id) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
            videoRepository.delete(video);
        } else {
            throw new PermissionDeniedException();
        }
        return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
    }

    @Override
    public VideoResponse addSegment(Long id, SegmentRequest request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        var user = userRepository.findByEmailAndActive(userDetails.getEmail(), true).orElseThrow(NotFoundException::new);
        if (RoleEnum.NORMAL.getValue() == userDetails.getRoleId() && !video.getUserList().stream().map(User::getId).collect(Collectors.toList()).contains(user.getId())) {
            throw new PermissionDeniedException();
        }
        if (validateSegment(video, request, null)) {
            VideoSegment segment = new VideoSegment();
            ObjectMapper.INSTANCE.requestToSegment(segment, request);
            segment.setVideo(video);
            segment.setUser(user);
            segmentRepository.save(segment);
            video.getVideoSegments().add(segment);
            return setVideoResponse(video, RoleEnum.ADMIN.getValue() == userDetails.getRoleId());
        } else {
            throw new SegmentOverlapException();
        }
    }

    @Override
    public VideoResponse editSegment(Long id, SegmentRequest request, Long segmentId) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!video.getVideoSegments().stream().map(VideoSegment::getId).collect(Collectors.toList()).contains(segmentId)) {
            throw new NotFoundException();
        }
        var user = userRepository.findByEmailAndActive(userDetails.getEmail(), true).orElseThrow(NotFoundException::new);
        var segment = segmentRepository.findById(segmentId).orElseThrow(NotFoundException::new);
        if (RoleEnum.NORMAL.getValue() == userDetails.getRoleId() && !segment.getUser().getId().equals(user.getId())) {
            throw new PermissionDeniedException();
        }
        if (validateSegment(video, request, segment)) {
            ObjectMapper.INSTANCE.requestToSegment(segment, request);
            video.getVideoSegments().add(segment);
            var savedVideo = videoRepository.save(video);
            return setVideoResponse(savedVideo, RoleEnum.ADMIN.getValue() == userDetails.getRoleId());
        } else {
            throw new SegmentOverlapException();
        }
    }

    @Override
    public VideoResponse deleteSegment(Long videoId, Long segmentId) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        var video = videoRepository.findById(videoId).orElseThrow(NotFoundException::new);
        if (!video.getVideoSegments().stream().map(VideoSegment::getId).collect(Collectors.toList()).contains(segmentId)) {
            throw new NotFoundException();
        }
        var segment = segmentRepository.findById(segmentId).orElseThrow(NotFoundException::new);
        if (RoleEnum.NORMAL.getValue() == userDetails.getRoleId() && !segment.getUser().getId().equals(userDetails.getId())) {
            throw new PermissionDeniedException();
        }
        video.getVideoSegments().remove(segment);
        Video savedVideo = videoRepository.save(video);
        return setVideoResponse(savedVideo, RoleEnum.ADMIN.getValue() == userDetails.getRoleId());
    }

    @Override
    public VideoListResponse listVideoPublic(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long categoryId, Long subcategoryId, Long playlistId) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        List<UserPlaylist> userPlaylists = null;
        if (playlistId != null) {
            UserDetailsImpl userDetails = userUtils.userDetails();
            var playlist = playlistRepository.findById(playlistId).orElseThrow(NotFoundException::new);
            var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
            userPlaylists = userPlaylistRepository.findAllByUserAndPlaylist(user, playlist);
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = null;
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
        }
        Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(null, videoUtils.setSubcategoryList(subcategoryId, categoryId, subCategoryRepository, categoryRepository), userPlaylists, keyword), paging);
        return videoUtils.setVideoListResponse(videos, pageNo, videoUtils.setListVideoForPublic(videos.getContent(), playlistRepository.findAll(), userDetails));
    }

    @Override
    public VideoDetailResponse detailVideoPublic(Long id) {
        var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        return setVideoDetailPublic(video);
    }

    @Override
    public String addVideoPlaylist(Long videoId, List<VideoPlaylistRequest> request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
        var video = videoRepository.findById(videoId).orElseThrow(NotFoundException::new);
        for (VideoPlaylistRequest videoPlaylist : request) {
            var playlist = playlistRepository.findById(videoPlaylist.getId()).orElseThrow(NotFoundException::new);
            Optional<UserPlaylist> userPlaylist = userPlaylistRepository.findByUserAndPlaylistAndVideo(user, playlist, video);
            if (videoPlaylist.isSelected()) {
                if (userPlaylist.isEmpty()) {
                    UserPlaylist userPlaylistNew = new UserPlaylist();
                    userPlaylistNew.setPlaylist(playlist);
                    userPlaylistNew.setUser(user);
                    userPlaylistNew.setVideo(video);
                    userPlaylistRepository.save(userPlaylistNew);
                }
            } else {
                userPlaylist.ifPresent(userPlaylistRepository::delete);
            }
        }
        return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
    }

    private VideoResponse setVideoResponse(Video video, boolean isAdmin) {
        VideoResponse videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
        videoUtils.setUserAssigned(videoResponse, video, isAdmin);
        videoResponse.setSegments(videoUtils.setVideoSegment(video));
        return videoResponse;
    }

    private boolean validateSegment(Video video, SegmentRequest segmentRequest, VideoSegment segment) {
        if (segmentRequest.getStartFrame() < 0 || segmentRequest.getEndFrame() <= segmentRequest.getStartFrame()) {
            return false;
        }
        List<VideoSegment> videoSegments = video.getVideoSegments();
        if (segment != null) {
            videoSegments.remove(segment);
        }
        return validateOverlapSegment(segmentRequest, videoSegments);
    }

    private boolean validateOverlapSegment(SegmentRequest segmentRequest, List<VideoSegment> videoSegments) {
        Collections.sort(videoSegments);
        if (!videoSegments.isEmpty()) {
            for (VideoSegment videoSegment : videoSegments) {
                if (segmentRequest.getStartFrame() >= videoSegment.getStartFrame() && segmentRequest.getStartFrame() < videoSegment.getEndFrame()
                        || segmentRequest.getEndFrame() > videoSegment.getStartFrame() && segmentRequest.getEndFrame() <= videoSegment.getEndFrame()) {
                    return false;
                }
            }
        }
        return true;
    }

    private VideoDetailResponse setVideoDetailPublic(Video video) {
        VideoDetailResponse response = ObjectMapper.INSTANCE.videoToVideoDetailPublic(video);
        response.setSegments(videoUtils.setVideoSegment(video));
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = null;
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
        }
        response.setUserComment(videoUtils.setUserComment(video, userDetails));
        response.setUserLike(videoUtils.setUserLike(video, userDetails));
        response.setUserReview(videoUtils.setUserReview(video, userDetails));
        response.setPlaylists(videoUtils.setVideoPlaylist(video, playlistRepository.findAll(), userDetails));
        return response;
    }

}
