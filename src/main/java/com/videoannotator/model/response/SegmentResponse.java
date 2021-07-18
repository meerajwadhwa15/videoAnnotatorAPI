package com.videoannotator.model.response;

import lombok.Data;

@Data
public class SegmentResponse {
    private long id;
    private String label;
    private Long startFrame;
    private Long endFrame;
    private UserListResponse user;
}
