package com.videoannotator.model.response;

import lombok.Data;

import java.util.List;

@Data
public class VideoListResponse {
    private Long totalRecord;
    private Integer totalPage;
    private Integer currentPageNo;
    private List<VideoResponse> videoList;
}
