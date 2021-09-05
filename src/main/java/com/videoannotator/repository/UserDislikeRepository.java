package com.videoannotator.repository;

import com.videoannotator.model.User;
import com.videoannotator.model.UserDislike;
import com.videoannotator.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDislikeRepository extends JpaRepository<UserDislike, Long> {
    Optional<UserDislike> findByUserAndVideo(User user, Video video);
}
