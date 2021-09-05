package com.videoannotator.repository;

import com.videoannotator.model.User;
import com.videoannotator.model.UserReview;
import com.videoannotator.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    Optional<UserReview> findByUserAndVideo(User user, Video video);
}
