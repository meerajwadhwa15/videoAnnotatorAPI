package com.videoannotator.service.impl;

import com.videoannotator.constant.RoleEnum;
import com.videoannotator.constant.VideoStatusEnum;
import com.videoannotator.exception.NotFoundException;
import com.videoannotator.exception.NotLoginException;
import com.videoannotator.exception.PermissionDeniedException;
import com.videoannotator.exception.SegmentOverlapException;
import com.videoannotator.model.*;
import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoRequest;
import com.videoannotator.model.response.*;
import com.videoannotator.repository.*;
import com.videoannotator.service.IVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Override
    public List<VideoResponse> listVideo() {
        UserDetailsImpl userDetails = userDetails();
        List<VideoResponse> videoResponses = new ArrayList<>();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            List<Video> videoList = videoRepository.findAll();
            setListVideoResponse(videoResponses, videoList);
        } else {
            var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
            List<Video> videoList = videoRepository.findAllByUserList(user);
            setListVideoResponse(videoResponses, videoList);
        }
        return videoResponses;
    }

    @Override
    public VideoResponse detailVideo(Long id) {
        UserDetailsImpl userDetails = userDetails();
        var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
        var response = new VideoResponse();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            setVideoResponse(response, video, true);
        } else {
            if (video.getUserList().stream().map(User::getId).collect(Collectors.toSet()).contains(userDetails.getId())) {
                setVideoResponse(response, video, false);
            } else {
                throw new PermissionDeniedException();
            }
        }
        return response;
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
        var response = new VideoResponse();
        setVideoResponse(response, videoSaved, true);
        return response;
    }

    @Override
    public VideoResponse addVideo(VideoRequest request) {
        UserDetailsImpl userDetails = userDetails();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            var video = new Video();
            video.setName(request.getName());
            video.setDescription(request.getDescription());
            video.setStatus(VideoStatusEnum.NOT_ASSIGNED);
            video.setUrl(request.getUrl());
            if (null != request.getSubcategoryId()) {
                SubCategory subCategory = subCategoryRepository.findById(request.getSubcategoryId()).orElseThrow(NotFoundException::new);
                video.setSubCategory(subCategory);
            }
            var videoSaved = videoRepository.save(video);
            var response = new VideoResponse();
            setVideoResponse(response, videoSaved, true);
            return response;
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public VideoResponse updateVideo(Long id, VideoRequest request) {
        UserDetailsImpl userDetails = userDetails();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            var video = videoRepository.findById(id).orElseThrow(NotFoundException::new);
            video.setName(request.getName());
            video.setDescription(request.getDescription());
            video.setUrl(request.getUrl());
            var videoSaved = videoRepository.save(video);
            var response = new VideoResponse();
            setVideoResponse(response, videoSaved, true);
            return response;
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
            segment.setLabel(request.getLabel());
            segment.setStartFrame(request.getStartFrame());
            segment.setEndFrame(request.getEndFrame());
            segment.setVideo(video);
            segment.setUser(user);
            segmentRepository.save(segment);
            video.getVideoSegments().add(segment);
            var videoResponse = new VideoResponse();
            setVideoResponse(videoResponse, video, RoleEnum.ADMIN.getValue() == userDetails.getRoleId());
            return videoResponse;
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
            segment.setLabel(request.getLabel());
            segment.setStartFrame(request.getStartFrame());
            segment.setEndFrame(request.getEndFrame());
            video.getVideoSegments().add(segment);
            var savedVideo = videoRepository.save(video);
            var videoResponse = new VideoResponse();
            setVideoResponse(videoResponse, savedVideo, RoleEnum.ADMIN.getValue() == userDetails.getRoleId());
            return videoResponse;
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
        var videoResponse = new VideoResponse();
        setVideoResponse(videoResponse, savedVideo, RoleEnum.ADMIN.getValue() == userDetails.getRoleId());
        return videoResponse;
    }

    private UserDetailsImpl userDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (userDetails == null) {
            throw new NotLoginException();
        }
        return userDetails;
    }

    private void setListVideoResponse(List<VideoResponse> videoResponses, List<Video> videoList) {
        for (Video video : videoList) {
            var videoResponse = new VideoResponse();
            videoResponse.setId(video.getId());
            videoResponse.setName(video.getName());
            videoResponse.setDescription(video.getDescription());
            videoResponse.setUrl(video.getUrl());
            videoResponse.setStatus(video.getStatus());
            videoResponses.add(videoResponse);
        }
    }

    private void setVideoResponse(VideoResponse videoResponse, Video video, boolean isAdmin) {
        videoResponse.setId(video.getId());
        videoResponse.setName(video.getName());
        videoResponse.setDescription(video.getDescription());
        videoResponse.setSize(video.getSize());
        videoResponse.setFormat(video.getFormat());
        videoResponse.setUrl(video.getUrl());
        videoResponse.setStatus(video.getStatus());
        List<UserListResponse> userListResponse = new ArrayList<>();
        List<User> userList = video.getUserList();
        if (isAdmin && null != userList) {
            for (User user : userList) {
                var userResponse = new UserListResponse();
                userResponse.setId(user.getId());
                userResponse.setFullName(user.getFullName());
                userResponse.setEmail(user.getEmail());
                userListResponse.add(userResponse);
            }
        }
        List<SegmentResponse> segments = new ArrayList<>();
        List<VideoSegment> videoSegments = video.getVideoSegments();
        if (null != videoSegments) {
            Collections.sort(videoSegments);
            for (VideoSegment segment : videoSegments) {
                var segmentResponse = new SegmentResponse();
                segmentResponse.setId(segment.getId());
                segmentResponse.setLabel(segment.getLabel());
                segmentResponse.setStartFrame(segment.getStartFrame());
                segmentResponse.setEndFrame(segment.getEndFrame());
                var userResponse = new UserListResponse();
                userResponse.setId(segment.getUser().getId());
                userResponse.setEmail(segment.getUser().getEmail());
                userResponse.setFullName(segment.getUser().getFullName());
                segmentResponse.setUser(userResponse);
                segments.add(segmentResponse);
            }
        }
        videoResponse.setSegments(segments);
        videoResponse.setAssignedUsers(userListResponse);

        if (null != video.getSubCategory()) {
            var subCategoryResponse = new SubCategoryResponse();
            var categoryResponse = new CategoryResponse();
            categoryResponse.setId(video.getSubCategory().getCategory().getId());
            categoryResponse.setName(video.getSubCategory().getCategory().getName());
            categoryResponse.setDescription(video.getSubCategory().getCategory().getDescription());
            subCategoryResponse.setId(video.getSubCategory().getId());
            subCategoryResponse.setName(video.getSubCategory().getName());
            subCategoryResponse.setDescription(video.getSubCategory().getDescription());
            subCategoryResponse.setCategory(categoryResponse);
            videoResponse.setSubCategory(subCategoryResponse);
        }

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

    private boolean validateOverlapSegment(SegmentRequest segmentRequest, List<VideoSegment> videoSegments){
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
}
