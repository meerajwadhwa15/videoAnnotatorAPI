package com.videoannotator.service;

import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.LoginResponse;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.model.response.UserResponse;

import java.util.List;

/**
 * Registration APIs to register and update user
 */
public interface IUserService {
    /**
     * Register User ( store ) and returns the user object
     *
     * @param user           - User information request
     * @return response      - ID user
     */
    RegisterResponse<Long> registerUser(RegisterRequest user);

    /**
     * User login return user data and token
     *
     * @param request           - Account information request
     * @return response         - User data and token
     */
    LoginResponse loginUser(LoginRequest request);

    /**
     * Get current login user data
     *
     * @return response         - User data
     */
    LoginResponse currentUser();

    /**
     * Get list user data
     *
     * @return response         - List of user data
     */
    List<UserResponse> listUser();
}
