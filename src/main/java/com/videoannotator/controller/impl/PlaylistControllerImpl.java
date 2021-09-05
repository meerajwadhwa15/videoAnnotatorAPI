package com.videoannotator.controller.impl;

import com.videoannotator.controller.IPlaylistController;
import com.videoannotator.model.response.PlaylistResponse;
import com.videoannotator.service.IPlayListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class PlaylistControllerImpl implements IPlaylistController {
    private final IPlayListService playListService;

    @Override
    public ResponseEntity<List<PlaylistResponse>> listPlaylist() {
        List<PlaylistResponse> responses = playListService.listPlaylist();
        return ResponseEntity.ok(responses);
    }
}
