package com.videoannotator.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "video_segment")
public class VideoSegment implements Comparable< VideoSegment >{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String label;
    private Long startFrame;
    private Long endFrame;

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public int compareTo(VideoSegment segment) {
        return this.getStartFrame().compareTo(segment.getStartFrame());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoSegment segment = (VideoSegment) o;
        return id == segment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
