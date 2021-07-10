package com.videoannotator.service.impl;

import com.videoannotator.constant.RoleEnum;
import com.videoannotator.constant.VideoStatusEnum;
import com.videoannotator.exception.NotFoundException;
import com.videoannotator.exception.NotLoginException;
import com.videoannotator.exception.PermissionDeniedException;
import com.videoannotator.model.User;
import com.videoannotator.model.UserDetailsImpl;
import com.videoannotator.model.Video;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.response.UserResponse;
import com.videoannotator.model.response.VideoResponse;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.repository.VideoRepository;
import com.videoannotator.service.IVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements IVideoService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Override
    public List<VideoResponse> listVideo() {
        UserDetailsImpl userDetails = userDetails();
        List<VideoResponse> videoResponses = new ArrayList<>();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            List<Video> videoList = videoRepository.findAll();
            setListVideoResponse(videoResponses, videoList, true);
        } else {
            var user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
            List<Video> videoList = videoRepository.findAllByUser(user);
            setListVideoResponse(videoResponses, videoList, false);
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
            if (video.getUser().stream().map(User::getId).collect(Collectors.toSet()).contains(userDetails.getId())) {
                setVideoResponse(response, video, false);
            } else {
                throw new PermissionDeniedException();
            }
        }
        return response;
    }

    @Override
    public VideoResponse assignVideo(VideoAssignRequest assignRequest) {
        UserDetailsImpl userDetails = userDetails();
        if (RoleEnum.ADMIN.getValue() != userDetails.getRoleId()) {
            throw new PermissionDeniedException();
        }
        List<User> userList = userRepository.findAllById(assignRequest.getUserId());
        var video = videoRepository.findById(assignRequest.getId()).orElseThrow(NotFoundException::new);
        if (userList.isEmpty()) {
            video.setStatus(VideoStatusEnum.NOT_ASSIGNED);
        } else {
            video.setStatus(VideoStatusEnum.ASSIGNED);
        }
        video.setUser(userList);
        videoRepository.save(video);
        var response = new VideoResponse();
        setVideoResponse(response, video, true);
        return response;
    }

    private UserDetailsImpl userDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (userDetails == null) {
            throw new NotLoginException();
        }
        return userDetails;
    }

    private void setListVideoResponse(List<VideoResponse> videoResponses, List<Video> videoList, boolean isAdmin) {
        for (Video video : videoList) {
            var videoResponse = new VideoResponse();
            setVideoResponse(videoResponse, video, isAdmin);
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
        List<UserResponse> userResponses = new ArrayList<>();
        if (isAdmin) {
            for (User user : video.getUser()) {
                var userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setFullName(user.getFullName());
                userResponse.setEmail(user.getEmail());
                userResponses.add(userResponse);
            }
        }
        videoResponse.setAssignedUsers(userResponses);
    }
}
