package com.videoannotator.model.response;

import com.videoannotator.constant.VideoStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class VideoResponse {
    private Long id;
    private String name;
    private String description;
    private String format;
    private String size;
    private String url;
    private VideoStatusEnum status;
    List<SegmentResponse> segments;
    List<UserListResponse> assignedUsers;
}
