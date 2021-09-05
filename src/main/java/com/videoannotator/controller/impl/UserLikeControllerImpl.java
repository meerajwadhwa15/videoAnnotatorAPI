package com.videoannotator.controller.impl;

import com.videoannotator.controller.IUserLikeController;
import com.videoannotator.model.request.UserLikeRequest;
import com.videoannotator.service.IUserLikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserLikeControllerImpl implements IUserLikeController {
    private final IUserLikeService userLikeService;
    @Override
    public ResponseEntity<String> likeVideo(Long videoId, UserLikeRequest request) {
        String response = userLikeService.likeVideo(videoId, request);
        return ResponseEntity.ok(response);
    }
}
