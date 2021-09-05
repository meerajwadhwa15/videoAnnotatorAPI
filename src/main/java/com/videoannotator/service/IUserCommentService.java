package com.videoannotator.service;

import com.videoannotator.model.request.UserCommentRequest;
import com.videoannotator.model.response.UserCommentResponse;

/**
 * Service to CRUD comment
 */
public interface IUserCommentService {
    /**
     *
     * @param videoId           -Video's ID
     * @param request           -Comment content
     * @return response         -List comment
     */
    UserCommentResponse addComment(Long videoId, UserCommentRequest request);
    /**
     *
     * @param commentId         -Comment's ID
     * @param request           -Comment content
     * @return response         -List comment
     */
    UserCommentResponse editComment(Long commentId, UserCommentRequest request);
    /**
     *
     * @param commentId         -Comment's ID
     * @return response         -List comment
     */
    UserCommentResponse deleteComment(Long commentId);
}
