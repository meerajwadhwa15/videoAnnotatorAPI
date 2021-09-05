package com.videoannotator.util;

import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.*;
import com.videoannotator.model.mapper.ObjectMapper;
import com.videoannotator.model.response.*;
import com.videoannotator.repository.CategoryRepository;
import com.videoannotator.repository.SubCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoUtils {
    public List<SegmentResponse> setVideoSegment(Video video) {
        List<SegmentResponse> segments = new ArrayList<>();
        List<VideoSegment> videoSegments = video.getVideoSegments();
        if (null != videoSegments) {
            Collections.sort(videoSegments);
            for (VideoSegment segment : videoSegments) {
                var segmentResponse = ObjectMapper.INSTANCE.segmentToListResponse(segment);
                segments.add(segmentResponse);
            }
        }
        return segments;
    }

    public UserCommentResponse setUserComment(Video video, UserDetailsImpl userDetails) {
        UserCommentResponse response = new UserCommentResponse();
        response.setNumberOfComment(video.getComments().size());
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (UserComment comment : video.getComments()) {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(comment.getId());
            commentResponse.setUserName(comment.getUser().getFullName());
            commentResponse.setContent(comment.getContent());
            commentResponse.setAvatar(comment.getUser().getAvatar());
            commentResponse.setCreateTime(comment.getCreateTime());
            commentResponse.setCanEdit(userDetails != null && comment.getUser().getId().equals(userDetails.getId()));
            commentResponses.add(commentResponse);
        }
        response.setCommentList(commentResponses);
        return response;
    }

    public UserLikeResponse setUserLike(Video video, UserDetailsImpl userDetails) {
        UserLikeResponse response = new UserLikeResponse();
        response.setNumberOfLike(video.getLikes().size());
        response.setLiked(userDetails != null && video.getLikes().stream().map(UserLike::getUser).collect(Collectors.toList()).stream().map(User::getId).collect(Collectors.toList()).contains(userDetails.getId()));
        response.setNumberOfDislike(video.getDislikes().size());
        response.setDisliked(userDetails != null && video.getDislikes().stream().map(UserDislike::getUser).collect(Collectors.toList()).stream().map(User::getId).collect(Collectors.toList()).contains(userDetails.getId()));
        return response;
    }

    public UserReviewResponse setUserReview(Video video, UserDetailsImpl userDetails) {
        UserReviewResponse response = new UserReviewResponse();
        int size = video.getReviews().size();
        response.setNumberOfReview(size);
        long total = 0;
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (UserReview review : video.getReviews()) {
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setId(review.getId());
            reviewResponse.setUserName(review.getUser().getFullName());
            reviewResponse.setAvatar(review.getUser().getAvatar());
            reviewResponse.setPoint(review.getPoint());
            reviewResponse.setContent(review.getContent());
            reviewResponse.setCreateTime(review.getCreateTime());
            if (userDetails != null && review.getUser().getId().equals(userDetails.getId())) {
                response.setUserReviewPoint(review.getPoint());
                response.setContent(review.getContent());
            }
            total += review.getPoint();
            reviewResponses.add(reviewResponse);
        }
        response.setReviews(reviewResponses);
        if (size > 0) {
            response.setAveragePoint(total / size);
        }
        return response;
    }

    public List<VideoPlaylistResponse> setVideoPlaylist(Video video, List<Playlist> playlists, UserDetailsImpl userDetails) {
        List<VideoPlaylistResponse> responseList = new ArrayList<>();
        for (Playlist playlist : playlists) {
            VideoPlaylistResponse response = new VideoPlaylistResponse();
            response.setId(playlist.getId());
            response.setName(playlist.getName());
            response.setSelected(false);
            if (userDetails != null) {
                for (UserPlaylist userPlaylist : video.getPlaylists()) {
                    if (userPlaylist.getUser().getId().equals(userDetails.getId()) && playlist.getId() == userPlaylist.getPlaylist().getId()) {
                        response.setSelected(true);
                    }
                }
            }
            responseList.add(response);
        }
        return responseList;
    }

    public List<SubCategory> setSubcategoryList(Long subcategoryId, Long categoryId, SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository) {
        List<SubCategory> subCategoryList = new ArrayList<>();
        if (subcategoryId != null) {
            SubCategory subCategory = subCategoryRepository.findById(subcategoryId).orElseThrow(NotFoundException::new);
            subCategoryList = Collections.singletonList(subCategory);
        } else if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(NotFoundException::new);
            subCategoryList = category.getSubCategoryList();
        }
        return subCategoryList;
    }

    public VideoListResponse setVideoListResponse(Page<Video> videoList, Integer pageNo, List<VideoResponse> videoResponses) {
        VideoListResponse listResponse = new VideoListResponse();
        listResponse.setTotalPage(videoList.getTotalPages());
        listResponse.setTotalRecord(videoList.getTotalElements());
        listResponse.setCurrentPageNo(pageNo);
        listResponse.setVideoList(videoResponses);
        return listResponse;
    }


    public List<VideoResponse> setListVideo(List<Video> videoList, boolean isAdmin) {
        List<VideoResponse> listVideo = new ArrayList<>();
        for (Video video : videoList) {
            var videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
            setUserAssigned(videoResponse, video, isAdmin);
            listVideo.add(videoResponse);
        }
        return listVideo;
    }

    public List<VideoResponse> setListVideoForPublic(List<Video> videoList, List<Playlist> playlists, UserDetailsImpl userDetails) {
        List<VideoResponse> listVideo = new ArrayList<>();
        for (Video video : videoList) {
            var videoResponse = ObjectMapper.INSTANCE.videoToVideoResponse(video);
            videoResponse.setPlaylists(setVideoPlaylist(video, playlists, userDetails));
            listVideo.add(videoResponse);
        }
        return listVideo;
    }

    public void setUserAssigned(VideoResponse videoResponse, Video video, boolean isAdmin) {
        List<UserListResponse> userListResponse = new ArrayList<>();
        List<User> userList = video.getUserList();
        if (isAdmin && null != userList) {
            for (User user : userList) {
                var userResponse = ObjectMapper.INSTANCE.userToListResponse(user);
                userListResponse.add(userResponse);
            }
        }
        videoResponse.setAssignedUsers(userListResponse);
    }
}
