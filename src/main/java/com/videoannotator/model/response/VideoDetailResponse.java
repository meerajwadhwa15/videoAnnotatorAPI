package com.videoannotator.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class VideoDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String format;
    private String size;
    private String url;
    private String thumbnail;
    private List<SegmentResponse> segments;
    private SubCategoryResponse subCategory;
    private UserCommentResponse userComment;
    private UserLikeResponse userLike;
    private UserReviewResponse userReview;
    private List<VideoPlaylistResponse> playlists;
}
