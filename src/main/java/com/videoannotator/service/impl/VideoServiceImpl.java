package com.videoannotator.service.impl;

import com.videoannotator.constant.RoleEnum;
import com.videoannotator.constant.VideoStatusEnum;
import com.videoannotator.exception.NotFoundException;
import com.videoannotator.exception.NotLoginException;
import com.videoannotator.exception.PermissionDeniedException;
import com.videoannotator.exception.SegmentOverlapException;
import com.videoannotator.model.*;
import com.videoannotator.model.mapper.ObjectMapper;
import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoRequest;
import com.videoannotator.model.response.SegmentResponse;
import com.videoannotator.model.response.UserListResponse;
import com.videoannotator.model.response.VideoListResponse;
import com.videoannotator.model.response.VideoResponse;
import com.videoannotator.repository.*;
import com.videoannotator.repository.specification.VideoSpecification;
import com.videoannotator.service.IVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private final VideoSpecification videoSpecification;

    @Override
    public VideoListResponse listVideo(Integer pageNo, Integer pageSize, String sortBy, String keyword) {
        UserDetailsImpl userDetails = userDetails();
        List<VideoResponse> listVideo = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(null, null, keyword), paging);
            setListVideo(listVideo, videos.getContent(), true);
            return setVideoListResponse(videos, pageNo, listVideo);
        } else {
            var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(user, null, keyword), paging);
            setListVideo(listVideo, videos.getContent(), false);
            return setVideoListResponse(videos, pageNo, listVideo);
        }
    }

    @Override
    public VideoResponse detailVideo(Long id) {
        UserDetailsImpl userDetails = userDetails();
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
        UserDetailsImpl userDetails = userDetails();
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
        UserDetailsImpl userDetails = userDetails();
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
        UserDetailsImpl userDetails = userDetails();
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
        UserDetailsImpl userDetails = userDetails();
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
        UserDetailsImpl userDetails = userDetails();
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
        UserDetailsImpl userDetails = userDetails();
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
        UserDetailsImpl userDetails = userDetails();
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
    public List<VideoResponse> listVideoPublic() {
        List<Video> videoList = videoRepository.findAll();
        List<VideoResponse> responses = new ArrayList<>();
        for (Video video : videoList) {
            var videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
            responses.add(videoResponse);
        }
        return responses;
    }

    @Override
    public VideoResponse detailVideoPublic(Long id) {
        var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        return setVideoResponse(video, false);
    }

    @Override
    public List<VideoResponse> listVideoByCategoryPublic(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NotFoundException::new);
        List<VideoResponse> responses = new ArrayList<>();
        for (SubCategory subCategory : category.getSubCategoryList()) {
            for (Video video : subCategory.getVideos()) {
                var videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
                responses.add(videoResponse);
            }
        }
        return responses;
    }

    @Override
    public List<VideoResponse> listVideoBySubcategoryPublic(Long subcategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subcategoryId).orElseThrow(NotFoundException::new);
        List<VideoResponse> responses = new ArrayList<>();
        for (Video video : subCategory.getVideos()) {
            var videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
            responses.add(videoResponse);
        }
        return responses;
    }

    @Override
    public VideoListResponse listVideoByCategory(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long categoryId) {
        UserDetailsImpl userDetails = userDetails();
        List<VideoResponse> videoResponses = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Category category = categoryRepository.findById(categoryId).orElseThrow(NotFoundException::new);
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(null, category.getSubCategoryList(), keyword), paging);
            setListVideo(videoResponses, videos.getContent(), true);
            return setVideoListResponse(videos, pageNo, videoResponses);
        } else {
            var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(user, category.getSubCategoryList(), keyword), paging);
            setListVideo(videoResponses, videos.getContent(), false);
            return setVideoListResponse(videos, pageNo, videoResponses);
        }
    }

    @Override
    public VideoListResponse listVideoBySubcategory(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long subcategoryId) {
        UserDetailsImpl userDetails = userDetails();
        List<VideoResponse> videoResponses = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        SubCategory subCategory = subCategoryRepository.findById(subcategoryId).orElseThrow(NotFoundException::new);
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(null, Collections.singletonList(subCategory), keyword), paging);
            setListVideo(videoResponses, videos.getContent(), true);
            return setVideoListResponse(videos, pageNo, videoResponses);
        } else {
            var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
            Page<Video> videos = videoRepository.findAll(videoSpecification.filterByConditions(user, Collections.singletonList(subCategory), keyword), paging);
            setListVideo(videoResponses, videos.getContent(), false);
            return setVideoListResponse(videos, pageNo, videoResponses);

        }
    }

    private UserDetailsImpl userDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (userDetails == null) {
            throw new NotLoginException();
        }
        return userDetails;
    }

    private void setListVideo(List<VideoResponse> videoResponses, List<Video> videoList, boolean isAdmin) {
        for (Video video : videoList) {
            var videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
            setUserAssigned(videoResponse, video, isAdmin);
            videoResponses.add(videoResponse);
        }
    }

    private VideoResponse setVideoResponse(Video video, boolean isAdmin) {
        VideoResponse videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
        setUserAssigned(videoResponse, video, isAdmin);
        List<SegmentResponse> segments = new ArrayList<>();
        List<VideoSegment> videoSegments = video.getVideoSegments();
        if (null != videoSegments) {
            Collections.sort(videoSegments);
            for (VideoSegment segment : videoSegments) {
                var segmentResponse = ObjectMapper.INSTANCE.segmentToListResponse(segment);
                segments.add(segmentResponse);
            }
        }
        videoResponse.setSegments(segments);
        return videoResponse;
    }

    private void setUserAssigned(VideoResponse videoResponse, Video video, boolean isAdmin) {
        List<UserListResponse> userListResponse = new ArrayList<>();
        List<User> userList = video.getUserList();
        if (isAdmin && null != userList) {
            for (User user : userList) {
                var userResponse = ObjectMapper.INSTANCE.userToListResponse(user);
                userListResponse.add(userResponse);
            }
        }
        videoResponse.setAssignedUsers(userListResponse);
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

    private VideoListResponse setVideoListResponse(Page<Video> videoList, Integer pageNo, List<VideoResponse> videoResponses) {
        VideoListResponse listResponse = new VideoListResponse();
        listResponse.setTotalPage(videoList.getTotalPages());
        listResponse.setTotalRecord(videoList.getTotalElements());
        listResponse.setCurrentPageNo(pageNo);
        listResponse.setVideoList(videoResponses);
        return listResponse;
    }
}
