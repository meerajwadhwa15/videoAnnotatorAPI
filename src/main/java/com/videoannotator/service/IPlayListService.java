package com.videoannotator.service;

import com.videoannotator.model.response.PlaylistResponse;

import java.util.List;

/**
 * Service to add or remove video to playlist
 */
public interface IPlayListService {

    /**
     * Get list playlist
     *
     * @return response         - List of playlist
     */
    List<PlaylistResponse> listPlaylist();

}
