package com.videoannotator.model.request;

import lombok.Data;

import java.util.List;

@Data
public class VideoAssignRequest {
    private List<Long> userId;
}
