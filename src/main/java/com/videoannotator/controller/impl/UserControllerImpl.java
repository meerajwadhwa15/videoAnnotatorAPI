package com.videoannotator.controller.impl;


import com.videoannotator.controller.IUserController;
import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.LoginResponse;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.model.response.UserResponse;
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
    public ResponseEntity<LoginResponse> currentUser() {
        LoginResponse response = userService.currentUser();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<UserResponse>> listUser() {
        List<UserResponse> userResponses = userService.listUser();
        return ResponseEntity.ok(userResponses);
    }

}
