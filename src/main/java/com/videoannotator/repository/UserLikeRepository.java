package com.videoannotator.repository;

import com.videoannotator.model.User;
import com.videoannotator.model.UserLike;
import com.videoannotator.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
    Optional<UserLike> findByUserAndVideo(User user, Video video);
}
