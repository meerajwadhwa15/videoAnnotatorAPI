package com.videoannotator.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String userName;
    private String avatar;
    private LocalDateTime createTime;
    private boolean canEdit;
}
