package com.videoannotator.model.response;

import lombok.Data;

import java.util.List;

@Data
public class UserReviewResponse {
    private Integer numberOfReview;
    private Integer userReviewPoint;
    private Long averagePoint;
    private String content;
    private List<ReviewResponse> reviews;
}
