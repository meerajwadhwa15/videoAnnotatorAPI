package com.videoannotator.controller;

import com.videoannotator.model.request.UserLikeRequest;
import com.videoannotator.model.response.ErrorResponse;
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

@RequestMapping(value = {"/like"})
public interface IUserLikeController {

    @Operation(summary = "Like a video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like video Successful"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/{videoId}")
    ResponseEntity<String> likeVideo(@Valid @PathVariable Long videoId, @Valid @RequestBody UserLikeRequest request);
}
