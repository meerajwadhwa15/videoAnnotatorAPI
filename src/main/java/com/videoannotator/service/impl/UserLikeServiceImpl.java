package com.videoannotator.service.impl;

import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.*;
import com.videoannotator.model.request.UserLikeRequest;
import com.videoannotator.repository.UserDislikeRepository;
import com.videoannotator.repository.UserLikeRepository;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.repository.VideoRepository;
import com.videoannotator.service.IUserLikeService;
import com.videoannotator.util.UserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserLikeServiceImpl implements IUserLikeService {

    private static final String SUCCESS = "message.success";

    private final UserUtils userUtils;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;
    private final UserDislikeRepository userDislikeRepository;
    private final MessageSource messageSource;

    @Override
    public String likeVideo(Long videoId, UserLikeRequest request) {
        UserDetailsImpl userDetails = userUtils.userDetails();
        Video video = videoRepository.findById(videoId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userDetails.getId()).orElseThrow(NotFoundException::new);
        Optional<UserLike> userLike = userLikeRepository.findByUserAndVideo(user, video);
        if (request.isLike()) {
            if (userLike.isEmpty()) {
                UserLike userLikeNew = new UserLike();
                userLikeNew.setUser(user);
                userLikeNew.setVideo(video);
                userLikeRepository.save(userLikeNew);
            }
        } else {
            userLike.ifPresent(userLikeRepository::delete);
        }

        Optional<UserDislike> userDislike = userDislikeRepository.findByUserAndVideo(user, video);
        if (request.isDislike()) {
            if (userDislike.isEmpty()) {
                UserDislike userDislikeNew = new UserDislike();
                userDislikeNew.setUser(user);
                userDislikeNew.setVideo(video);
                userDislikeRepository.save(userDislikeNew);
            }
        } else {
            userDislike.ifPresent(userDislikeRepository::delete);
        }
        return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
    }
}
