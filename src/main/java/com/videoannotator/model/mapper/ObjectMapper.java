package com.videoannotator.model.mapper;

import com.videoannotator.constant.RoleEnum;
import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.*;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.UpdateUserRequest;
import com.videoannotator.model.request.VideoRequest;
import com.videoannotator.model.response.*;
import com.videoannotator.repository.RoleRepository;
import com.videoannotator.repository.SubCategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper
public abstract class ObjectMapper {
    public static final ObjectMapper INSTANCE = Mappers.getMapper(ObjectMapper.class);

    public User registerRequestToUser(RegisterRequest registerRequest, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        var user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setRole(roleRepository.findById(RoleEnum.NORMAL.getValue()).orElseThrow(NotFoundException::new));
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setActive(false);
        return user;
    }

    public void updateRequestToUser(User user, UpdateUserRequest userRequest) {
        user.setFullName(userRequest.getFullName());
        user.setAddress(userRequest.getAddress());
        user.setPhone(userRequest.getPhone());
        user.setIntroduction(userRequest.getIntroduction());
    }

    @Mapping(target = "roles", ignore = true)
    public abstract UserDetailResponse userDetailToDetailResponse(UserDetailsImpl userDetails);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "token", ignore = true)
    public abstract LoginResponse userDetailToLoginResponse(UserDetailsImpl userDetails);

    public abstract UserListResponse userToListResponse(User user);

    public abstract SegmentResponse segmentToListResponse(VideoSegment videoSegments);

    @Mapping(target = "segments", ignore = true)
    @Mapping(target = "assignedUsers", ignore = true)
    public abstract VideoResponse videoToVideoResponse(Video video);

    public void requestToVideo(Video video, VideoRequest request, SubCategoryRepository subCategoryRepository) {
        video.setName(request.getName());
        video.setDescription(request.getDescription());
        video.setUrl(request.getUrl());
        video.setThumbnail(request.getThumbnail());
        if (null != request.getSubcategoryId()) {
            SubCategory subCategory = subCategoryRepository.findById(request.getSubcategoryId()).orElseThrow(NotFoundException::new);
            video.setSubCategory(subCategory);
        }
    }

    public void requestToSegment(VideoSegment videoSegment, SegmentRequest request) {
        videoSegment.setLabel(request.getLabel());
        videoSegment.setStartFrame(request.getStartFrame());
        videoSegment.setEndFrame(request.getEndFrame());
        videoSegment.setThumbnail(request.getThumbnail());
    }

    public abstract CategoryResponse categoryToResponse(Category category);

    public abstract List<CategoryResponse> categoryToListResponse(List<Category> categories);

    public abstract SubCategoryResponse subCategoryToResponse(SubCategory subCategory);

    public abstract List<SubCategoryResponse> subCategoryToListResponse(List<SubCategory> subCategory);
}
