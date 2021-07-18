package com.videoannotator.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SegmentRequest {
    @NotBlank
    private String label;
    @NotNull
    private Long startFrame;
    @NotNull
    private Long endFrame;
}
