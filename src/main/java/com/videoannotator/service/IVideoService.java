package com.videoannotator.service;

import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.response.VideoResponse;

import java.util.List;

/**
 * Video management api for list, assign, update video
 */
public interface IVideoService {

    /**
     * Get list video data
     *
     * @return response         - List of video data
     */
    List<VideoResponse> listVideo();

    /**
     * Get detail video data
     *
     * @param id                - Video's id
     * @return response         - Video data
     */
    VideoResponse detailVideo(Long id);

    /**
     * Assign video for user
     *
     * @param assignRequest     - Video's id and list user's id
     * @return response         - Video data
     */
    VideoResponse assignVideo(VideoAssignRequest assignRequest);
}
