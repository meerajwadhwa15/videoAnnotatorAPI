package com.videoannotator.repository;

import com.videoannotator.model.User;
import com.videoannotator.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllByUserList(User user);
}
