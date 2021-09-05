package com.videoannotator.service;

import com.videoannotator.model.request.UserLikeRequest;

/**
 * Service to CRUD like
 */
public interface IUserLikeService {

    /**
     * Add like a video
     *
     * @param videoId           - Video's id
     * @param request            - Like or unlike video
     * @return response         - OK
     */
    String likeVideo(Long videoId, UserLikeRequest request);
}
