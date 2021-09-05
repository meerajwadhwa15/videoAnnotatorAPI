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
    private String thumbnail;
    private VideoStatusEnum status;
    private List<SegmentResponse> segments;
    private List<UserListResponse> assignedUsers;
    private SubCategoryResponse subCategory;
    private List<VideoPlaylistResponse> playlists;
}
