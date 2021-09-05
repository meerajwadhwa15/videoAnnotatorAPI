package com.videoannotator.model.response;

import lombok.Data;

@Data
public class VideoPlaylistResponse {
    private Long id;
    private String name;
    private boolean isSelected;
}
