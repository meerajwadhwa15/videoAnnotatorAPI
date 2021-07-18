package com.videoannotator.model.response;

import lombok.Data;

@Data
public class UserListResponse {
    private Long id;
    private String email;
    private String fullName;
}
