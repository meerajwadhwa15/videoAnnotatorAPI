package com.videoannotator.model.request;

import com.videoannotator.validation.LikeValidation;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@LikeValidation
public class UserLikeRequest {
    @NotNull
    private boolean isLike;
    @NotNull
    private boolean isDislike;
}
