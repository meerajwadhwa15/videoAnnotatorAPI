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
@Table(name = "user_comment")
public class UserComment implements Comparable<UserComment> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String content;

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Override
    public int compareTo(UserComment comment) {
        return this.getCreateTime().compareTo(comment.getCreateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserComment comment = (UserComment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
