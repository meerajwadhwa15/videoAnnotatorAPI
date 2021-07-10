package com.videoannotator.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "video_segment")
public class VideoSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String label;
    private String startFrame;
    private String endFrame;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
}
