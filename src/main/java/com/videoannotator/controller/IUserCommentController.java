package com.videoannotator.controller;

import com.videoannotator.model.request.UserCommentRequest;
import com.videoannotator.model.response.ErrorResponse;
import com.videoannotator.model.response.UserCommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping(value = {"video/"})
public interface IUserCommentController {
    @Operation(summary = "Add a comment")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add comment Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserCommentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/comment/{videoId}")
    ResponseEntity<UserCommentResponse> addComment(@Valid @PathVariable Long videoId, @Valid @RequestBody UserCommentRequest request);

    @Operation(summary = "Edit a comment")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit comment Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserCommentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PutMapping("/comment/{commentId}")
    ResponseEntity<UserCommentResponse> editComment(@Valid @PathVariable Long commentId, @Valid @NotNull @RequestBody UserCommentRequest request);

    @Operation(summary = "Delete a comment")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete comment Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserCommentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @DeleteMapping("/comment/{commentId}")
    ResponseEntity<UserCommentResponse> deleteComment(@Valid @PathVariable Long commentId);
}
