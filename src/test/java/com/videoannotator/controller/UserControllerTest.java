package com.videoannotator.controller;

import com.videoannotator.controller.impl.UserControllerImpl;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserControllerImpl userController;

    @Test
    @DisplayName("Test register user successful")
    void registerUserOk() {
        RegisterRequest registerRequest = new RegisterRequest();
        when(userService.registerUser(registerRequest)).thenReturn(new RegisterResponse<>(1L));
        ResponseEntity<RegisterResponse<Long>> response = userController.registerUser(registerRequest);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
