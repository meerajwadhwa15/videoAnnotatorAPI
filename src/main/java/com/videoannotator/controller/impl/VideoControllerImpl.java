package com.videoannotator.controller.impl;

import com.videoannotator.controller.IVideoController;
import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoPlaylistRequest;
import com.videoannotator.model.request.VideoRequest;
import com.videoannotator.model.response.VideoDetailResponse;
import com.videoannotator.model.response.VideoListResponse;
import com.videoannotator.model.response.VideoResponse;
import com.videoannotator.service.IVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class VideoControllerImpl implements IVideoController {
    private final IVideoService videoService;

    @Override
    public ResponseEntity<VideoListResponse> listVideo(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long categoryId, Long subcategoryId) {
        VideoListResponse videoResponses = videoService.listVideo(pageNo, pageSize, sortBy, keyword, categoryId, subcategoryId);
        return ResponseEntity.ok(videoResponses);
    }

    @Override
    public ResponseEntity<VideoResponse> detailVideo(Long videoId) {
        VideoResponse response = videoService.detailVideo(videoId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> assignVideo(VideoAssignRequest assignRequest, Long videoId) {
        VideoResponse response = videoService.assignVideo(assignRequest, videoId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> addVideo(VideoRequest videoRequest) {
        VideoResponse response = videoService.addVideo(videoRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> updateVideo(Long videoId, VideoRequest videoRequest) {
        VideoResponse response = videoService.updateVideo(videoId, videoRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> deleteVideo(Long videoId) {
        String response = videoService.deleteVideo(videoId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> addSegment(Long videoId, SegmentRequest request) {
        VideoResponse response = videoService.addSegment(videoId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> editSegment(Long videoId, SegmentRequest request, Long segmentId) {
        VideoResponse response = videoService.editSegment(videoId, request, segmentId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> deleteSegment(Long videoId, Long segmentId) {
        VideoResponse response = videoService.deleteSegment(videoId, segmentId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoListResponse> listVideoPublic(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long categoryId, Long subcategoryId, Long playlistId) {
        VideoListResponse responses = videoService.listVideoPublic(pageNo, pageSize, sortBy, keyword, categoryId, subcategoryId, playlistId);
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<VideoDetailResponse> detailVideoPublic(Long videoId) {
        VideoDetailResponse response = videoService.detailVideoPublic(videoId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> addPlaylist(Long videoId, List<VideoPlaylistRequest> request) {
        String response = videoService.addVideoPlaylist(videoId, request);
        return ResponseEntity.ok(response);
    }

}
