package com.videoannotator.controller.impl;

import com.videoannotator.controller.IVideoController;
import com.videoannotator.model.request.VideoAssignRequest;
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
}
