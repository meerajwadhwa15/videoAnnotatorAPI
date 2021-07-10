package com.videoannotator.model;

import com.videoannotator.constant.VideoStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String format;
    private String size;
    @Enumerated(EnumType.STRING)
    private VideoStatusEnum status;
    private String url;

    @OneToMany(mappedBy = "video" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<VideoSegment> videoSegments;

    @ManyToMany(mappedBy = "videos")
    private List<User> user;
}
