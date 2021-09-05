package com.videoannotator.service.impl;

import com.videoannotator.model.Playlist;
import com.videoannotator.model.mapper.ObjectMapper;
import com.videoannotator.model.response.PlaylistResponse;
import com.videoannotator.repository.PlaylistRepository;
import com.videoannotator.service.IPlayListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PlaylistServiceImpl implements IPlayListService {
    private final PlaylistRepository playlistRepository;

    @Override
    public List<PlaylistResponse> listPlaylist() {
        List<Playlist> playlists = playlistRepository.findAll();
        return ObjectMapper.INSTANCE.playlistToListResponse(playlists);
    }

}
