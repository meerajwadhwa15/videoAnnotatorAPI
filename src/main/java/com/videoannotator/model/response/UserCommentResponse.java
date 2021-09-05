package com.videoannotator.model.response;

import lombok.Data;

import java.util.List;

@Data
public class UserCommentResponse {
    private Integer numberOfComment;
    private List<CommentResponse> commentList;
}
