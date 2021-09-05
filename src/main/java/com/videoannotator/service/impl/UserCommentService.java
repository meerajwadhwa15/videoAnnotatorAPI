package com.videoannotator.service.impl;

import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.User;
import com.videoannotator.model.UserComment;
import com.videoannotator.model.UserDetailsImpl;
import com.videoannotator.model.Video;
import com.videoannotator.model.request.UserCommentRequest;
import com.videoannotator.model.response.UserCommentResponse;
import com.videoannotator.repository.UserCommentRepository;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.repository.VideoRepository;
import com.videoannotator.service.IUserCommentService;
import com.videoannotator.util.UserUtils;
import com.videoannotator.util.VideoUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCommentService implements IUserCommentService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final UserCommentRepository userCommentRepository;
    private final UserUtils userUtils;
    private final VideoUtils videoUtils;

    @Override
    public UserCommentResponse addComment(Long videoId, UserCommentRequest request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        Video video = videoRepository.findById(videoId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
        UserComment comment = new UserComment();
        comment.setUser(user);
        comment.setVideo(video);
        comment.setContent(request.getContent());
        userCommentRepository.save(comment);
        return videoUtils.setUserComment(video, userDetails);
    }

    @Override
    public UserCommentResponse editComment(Long commentId, UserCommentRequest request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        UserComment comment = userCommentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        comment.setContent(request.getContent());
        userCommentRepository.save(comment);
        return videoUtils.setUserComment(comment.getVideo(), userDetails);
    }

    @Override
    public UserCommentResponse deleteComment(Long commentId) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        UserComment comment = userCommentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        userCommentRepository.delete(comment);
        return videoUtils.setUserComment(comment.getVideo(), userDetails);
    }
}
