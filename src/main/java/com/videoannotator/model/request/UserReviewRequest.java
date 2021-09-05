package com.videoannotator.model.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UserReviewRequest {
    @NotNull
    @Max(value = 5)
    @Min(value = 0)
    private Integer point;
    private String content;
}
