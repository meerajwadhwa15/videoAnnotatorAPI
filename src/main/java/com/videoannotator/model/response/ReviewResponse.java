package com.videoannotator.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private Integer point;
    private String content;
    private String userName;
    private String avatar;
    private LocalDateTime createTime;
}
