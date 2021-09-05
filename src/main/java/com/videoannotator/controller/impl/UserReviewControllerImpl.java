package com.videoannotator.controller.impl;

import com.videoannotator.controller.IUserReviewController;
import com.videoannotator.model.request.UserReviewRequest;
import com.videoannotator.model.response.UserReviewResponse;
import com.videoannotator.service.IUserReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;



@RestController
@AllArgsConstructor
@CrossOrigin
public class UserReviewControllerImpl implements IUserReviewController {
    private final IUserReviewService userReviewService;
    @Override
    public ResponseEntity<UserReviewResponse> addReview(Long videoId, UserReviewRequest request) {
        UserReviewResponse response = userReviewService.addReview(videoId, request);
        return ResponseEntity.ok(response);
    }
}
