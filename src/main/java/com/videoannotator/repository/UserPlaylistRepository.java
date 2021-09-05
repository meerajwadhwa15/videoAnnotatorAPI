package com.videoannotator.repository;

import com.videoannotator.model.Playlist;
import com.videoannotator.model.User;
import com.videoannotator.model.UserPlaylist;
import com.videoannotator.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, Long> {
    List<UserPlaylist> findAllByUserAndPlaylist(User user, Playlist playlist);
    Optional<UserPlaylist> findByUserAndPlaylistAndVideo(User user, Playlist playlist, Video video);
}
