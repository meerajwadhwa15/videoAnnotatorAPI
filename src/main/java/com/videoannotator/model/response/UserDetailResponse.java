package com.videoannotator.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailResponse {
    private Long id;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private String introduction;
    private List<String> roles;
}
