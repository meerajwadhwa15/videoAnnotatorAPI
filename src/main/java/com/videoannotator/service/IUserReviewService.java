package com.videoannotator.service;

import com.videoannotator.model.request.UserReviewRequest;
import com.videoannotator.model.response.UserReviewResponse;

/**
 * Service to CRUD like
 */
public interface IUserReviewService {
    UserReviewResponse addReview(Long videoId, UserReviewRequest request);
}
