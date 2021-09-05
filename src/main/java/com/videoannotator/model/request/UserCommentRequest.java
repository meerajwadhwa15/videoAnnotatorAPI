package com.videoannotator.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserCommentRequest {
    @NotBlank
    private String content;
}
