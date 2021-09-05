package com.videoannotator.controller.impl;

import com.videoannotator.controller.IUserCommentController;
import com.videoannotator.model.request.UserCommentRequest;
import com.videoannotator.model.response.UserCommentResponse;
import com.videoannotator.service.IUserCommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserCommentControllerImpl implements IUserCommentController {
    private final IUserCommentService commentService;
    @Override
    public ResponseEntity<UserCommentResponse> addComment(Long videoId, UserCommentRequest request) {
        UserCommentResponse response = commentService.addComment(videoId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserCommentResponse> editComment(Long commentId, UserCommentRequest request) {
        UserCommentResponse response = commentService.editComment(commentId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserCommentResponse> deleteComment(Long commentId) {
        UserCommentResponse response = commentService.deleteComment(commentId);
        return ResponseEntity.ok(response);
    }
}
