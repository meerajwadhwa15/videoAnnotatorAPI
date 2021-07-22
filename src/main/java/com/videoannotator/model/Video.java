package com.videoannotator.model;

import com.videoannotator.constant.VideoStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "video")
public class Video{
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

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "video" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("startFrame ASC")
    List<VideoSegment> videoSegments;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_video", joinColumns = {@JoinColumn(name = "video_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> userList;

    @PreRemove
    public void removeUser() {
        for (User user : userList) {
            user.getVideos().remove(this);
        }
    }


}
