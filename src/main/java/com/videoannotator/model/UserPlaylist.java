package com.videoannotator.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_playlist")
public class UserPlaylist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    Video video;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
