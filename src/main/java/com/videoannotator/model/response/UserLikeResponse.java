package com.videoannotator.model.response;

import lombok.Data;

@Data
public class UserLikeResponse {
    private Integer numberOfLike;
    private Integer numberOfDislike;
    private boolean isLiked;
    private boolean isDisliked;
}
