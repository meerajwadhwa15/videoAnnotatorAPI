package com.videoannotator.service;

import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.PasswordRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.request.UpdateUserRequest;
import com.videoannotator.model.response.LoginResponse;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.model.response.UserDetailResponse;
import com.videoannotator.model.response.UserListResponse;

import java.util.List;

/**
 * Registration APIs to register and update user
 */
public interface IUserService {
    /**
     * Register User ( store ) and returns the user object
     *
     * @param user - User information request
     * @return response      - ID user
     */
    RegisterResponse<Long> registerUser(RegisterRequest user);

    /**
     * User login return user data and token
     *
     * @param request - Account information request
     * @return response         - User data and token
     */
    LoginResponse loginUser(LoginRequest request);

    /**
     * Get current login user data
     *
     * @return response         - User data
     */
    UserDetailResponse currentUser();

    /**
     * Get list user data
     *
     * @return list         - List of user data
     */
    List<UserListResponse> listUser();

    /**
     * Reset password, generate token and send to user's email
     *
     * @param email             - User's email
     * @return string           - Ok message
     */
    String resetPassword(String email);

    /**
     * Update new password when reset
     *
     * @param token             - Token reset password
     * @param passwordRequest   - password request data
     * @return string           - Ok message
     */
    String newPassword(String token, PasswordRequest passwordRequest);

    /**
     * Update new password when reset
     *
     * @param passwordRequest   - password request data
     * @return string           - Ok message
     */
    String changePassword(PasswordRequest passwordRequest);

    /**
     * Update new password when reset
     *
     * @param token             - Token reset password
     * @return string           - Ok message
     */
    String verifyToken(String token);

    /**
     * Update new password when reset
     *
     * @param userRequest       - User's information
     * @return response         - User's updated information
     */
    UserDetailResponse updateUser(UpdateUserRequest userRequest);

    /**
     * Confirm email register
     *
     * @param token             - Token reset password
     * @return string           - Ok message
     */
    String confirmEmail(String token);
}
