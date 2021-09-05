package com.videoannotator.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_dislike")
public class UserDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
