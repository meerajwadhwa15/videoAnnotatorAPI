package com.videoannotator.controller.impl;

import com.videoannotator.controller.IVideoController;
import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoRequest;
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
    public ResponseEntity<List<VideoResponse>> listVideo() {
        List<VideoResponse> videoResponses = videoService.listVideo();
        return ResponseEntity.ok(videoResponses);
    }

    @Override
    public ResponseEntity<VideoResponse> detailVideo(Long id) {
        VideoResponse response = videoService.detailVideo(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> assignVideo(VideoAssignRequest assignRequest) {
        VideoResponse response = videoService.assignVideo(assignRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> addVideo(VideoRequest videoRequest) {
        VideoResponse response = videoService.addVideo(videoRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> updateVideo(Long id, VideoRequest videoRequest) {
        VideoResponse response = videoService.updateVideo(id, videoRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> deleteVideo(Long id) {
        String response = videoService.deleteVideo(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VideoResponse> addSegment(Long id, SegmentRequest request) {
        VideoResponse response = videoService.addSegment(id, request);
        return ResponseEntity.ok(response);
    }
}
