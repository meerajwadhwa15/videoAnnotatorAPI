package com.videoannotator.controller;

import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.ErrorResponse;
import com.videoannotator.model.response.LoginResponse;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = {"/{locale:en|vi}/user", "/user"})
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
                    schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/current")
    ResponseEntity<LoginResponse> currentUser();

    @Operation(summary = "Get list normal User")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list user successful", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/list")
    ResponseEntity<List<UserResponse>> listUser();
}
