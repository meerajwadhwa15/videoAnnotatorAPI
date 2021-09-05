package com.videoannotator.model.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private String address;
    private String phone;
    private String introduction;
    private String avatar;
}
