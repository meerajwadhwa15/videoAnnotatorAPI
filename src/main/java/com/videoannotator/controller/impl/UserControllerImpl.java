package com.videoannotator.controller.impl;


import com.videoannotator.controller.IUserController;
import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.PasswordRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.request.UpdateUserRequest;
import com.videoannotator.model.response.LoginResponse;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.model.response.UserDetailResponse;
import com.videoannotator.model.response.UserListResponse;
import com.videoannotator.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserControllerImpl implements IUserController {
    private final IUserService userService;

    @Override
    public ResponseEntity<RegisterResponse<Long>> registerUser(RegisterRequest registerRequest) {
        RegisterResponse<Long> result = userService.registerUser(registerRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest) {
        LoginResponse response = userService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserDetailResponse> currentUser() {
        UserDetailResponse response = userService.currentUser();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<UserListResponse>> listUser() {
        List<UserListResponse> userListRespons = userService.listUser();
        return ResponseEntity.ok(userListRespons);
    }

    @Override
    public ResponseEntity<String> resetPassword(String email) {
        String response = userService.resetPassword(email);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> newPassword(String token, PasswordRequest passwordRequest) {
        String response = userService.newPassword(token, passwordRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> changePassword(PasswordRequest passwordRequest) {
        String response = userService.changePassword(passwordRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> resendCode(String email) {
        String response = userService.resendCode(email);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserDetailResponse> updateUser(UpdateUserRequest request) {
        UserDetailResponse response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> confirmEmail(String token) {
        String response = userService.confirmEmail(token);
        return ResponseEntity.ok(response);
    }


}
