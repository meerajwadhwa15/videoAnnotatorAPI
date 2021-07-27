package com.videoannotator.service;

import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoRequest;
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
     * @param assignRequest     - list user's id
     * @param videoId           - Video's id
     * @return response         - Video data
     */
    VideoResponse assignVideo(VideoAssignRequest assignRequest, Long videoId);

    /**
     * Add video
     *
     * @param request           - Video's data
     * @return response         - Video saved data
     */
    VideoResponse addVideo(VideoRequest request);

    /**
     * Update video
     *
     * @param id                - Video's id
     * @param request           - Video's data
     * @return response         - Video saved data
     */
    VideoResponse updateVideo(Long id, VideoRequest request);

    /**
     * Delete video
     *
     * @param id                - Video's id
     * @return string           - Delete video successful
     */
    String deleteVideo(Long id);

    /**
     * Add video annotation
     *
     * @param id                - Video's id
     * @param request           - Segment's data
     * @return response         - Video data
     */
    VideoResponse addSegment(Long id, SegmentRequest request);

    /**
     * Edit video annotation
     *
     * @param id                - Video's id
     * @param request           - Segment's data
     * @param segmentId         - Segment's id
     * @return response         - Video data
     */
    VideoResponse editSegment(Long id, SegmentRequest request, Long segmentId);

    /**
     * Delete video segment
     *
     * @param videoId                - Video's id
     * @param segmentId              - Segment's id
     * @return response              - Video data
     */
    VideoResponse deleteSegment(Long videoId, Long segmentId);

    /**
     * Get list video data for public
     *
     * @return response         - List of video data
     */
    List<VideoResponse> listVideoPublic();

    /**
     * Get detail video data public
     *
     * @param id                - Video's id
     * @return response         - Video data
     */
    VideoResponse detailVideoPublic(Long id);
}
