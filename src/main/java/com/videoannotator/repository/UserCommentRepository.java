package com.videoannotator.repository;

import com.videoannotator.model.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
}
