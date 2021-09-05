package com.videoannotator.controller;

import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.PasswordRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.request.UpdateUserRequest;
import com.videoannotator.model.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = {"/user"})
public interface IUserController {
    @Operation(summary = "Register User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegisterResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/signup")
    ResponseEntity<RegisterResponse<Long>> registerUser(@Valid @RequestBody RegisterRequest registerRequest);

    @Operation(summary = "User Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/signin")
    ResponseEntity<LoginResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest);

    @Operation(summary = "Get current User (User already login)")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get current user successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDetailResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/profile")
    ResponseEntity<UserDetailResponse> currentUser();

    @Operation(summary = "Get list normal User")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list user successful", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserListResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/list")
    ResponseEntity<List<UserListResponse>> listUser();

    @Operation(summary = "Reset password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Generate token successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/resetPassword")
    ResponseEntity<String> resetPassword(@Valid @RequestParam String email);


    @Operation(summary = "Update new password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update password successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/newPassword")
    ResponseEntity<String> newPassword(@Valid @RequestParam String token, @Valid @RequestBody PasswordRequest passwordRequest);

    @Operation(summary = "Change password")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update password successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/changePassword")
    ResponseEntity<String> changePassword(@Valid @RequestBody PasswordRequest passwordRequest);

    @Operation(summary = "Verify token reset password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token valid"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/resend")
    ResponseEntity<String> resendCode(@Valid @RequestParam String email);

    @Operation(summary = "Update user profile")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDetailResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PutMapping("/update")
    ResponseEntity<UserDetailResponse> updateUser(@Valid @RequestBody UpdateUserRequest request);

    @Operation(summary = "Confirm register email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/confirmEmail")
    ResponseEntity<String> confirmEmail(@Valid @RequestParam String token);
}

