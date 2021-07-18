package com.videoannotator.repository;

import com.videoannotator.model.VideoSegment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoSegmentRepository extends JpaRepository<VideoSegment, Long> {
}
