package com.videoannotator.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VideoAssignRequest {
    @NotNull
    private Long id;
    private List<Long> userId;
}
