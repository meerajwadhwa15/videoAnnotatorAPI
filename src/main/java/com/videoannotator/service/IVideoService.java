package com.videoannotator.service;

import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoPlaylistRequest;
import com.videoannotator.model.request.VideoRequest;
import com.videoannotator.model.response.VideoDetailResponse;
import com.videoannotator.model.response.VideoListResponse;
import com.videoannotator.model.response.VideoResponse;

import java.util.List;

/**
 * Video management api for list, assign, update video
 */
public interface IVideoService {

    /**
     * Get list video data
     *
     * @param pageNo            - Number of page
     * @param pageSize          - Number of record in a page
     * @param sortBy            - sort field
     * @param keyword           - Search text
     * @param categoryId        - category's id
     * @param subcategoryId     - subcategory's id
     *
     * @return response         - List of video data
     */
    VideoListResponse listVideo(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long categoryId, Long subcategoryId);

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
     * @param pageNo            - Number of page
     * @param pageSize          - Number of record in a page
     * @param sortBy            - sort field
     * @param keyword           - Search text
     * @param categoryId        - category's id
     * @param playlistId        - playlist's id
     *
     * @return response         - List of video data
     */
    VideoListResponse listVideoPublic(Integer pageNo, Integer pageSize, String sortBy, String keyword, Long categoryId, Long subcategoryId, Long playlistId);

    /**
     * Get detail video data public
     *
     * @param id                - Video's id
     * @return response         - Video data
     */
    VideoDetailResponse detailVideoPublic(Long id);

    String addVideoPlaylist(Long videoId, List<VideoPlaylistRequest> request);

}
