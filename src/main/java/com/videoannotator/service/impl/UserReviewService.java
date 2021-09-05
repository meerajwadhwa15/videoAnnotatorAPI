package com.videoannotator.service.impl;

import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.User;
import com.videoannotator.model.UserDetailsImpl;
import com.videoannotator.model.UserReview;
import com.videoannotator.model.Video;
import com.videoannotator.model.request.UserReviewRequest;
import com.videoannotator.model.response.UserReviewResponse;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.repository.UserReviewRepository;
import com.videoannotator.repository.VideoRepository;
import com.videoannotator.service.IUserReviewService;
import com.videoannotator.util.UserUtils;
import com.videoannotator.util.VideoUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserReviewService implements IUserReviewService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final UserReviewRepository userReviewRepository;
    private final UserUtils userUtils;
    private final VideoUtils videoUtils;

    @Override
    public UserReviewResponse addReview(Long videoId, UserReviewRequest request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
        Video video = videoRepository.findById(videoId).orElseThrow(NotFoundException::new);
        Optional<UserReview> review = userReviewRepository.findByUserAndVideo(user, video);
        if (review.isPresent()) {
            UserReview userReview = review.get();
            if (request.getPoint() > 0) {
                userReview.setPoint(request.getPoint());
                userReview.setContent(request.getContent());
                userReviewRepository.save(userReview);
            } else {
                userReviewRepository.delete(userReview);
            }
        } else if (request.getPoint() > 0) {
            UserReview userReview = new UserReview();
            userReview.setUser(user);
            userReview.setVideo(video);
            userReview.setPoint(request.getPoint());
            userReview.setContent(request.getContent());
            userReviewRepository.save(userReview);
        }
        return videoUtils.setUserReview(video, userDetails);
    }
}
