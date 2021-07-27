package com.videoannotator.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VideoRequest {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String url;
    private String thumbnail;
    private Long subcategoryId;
}
