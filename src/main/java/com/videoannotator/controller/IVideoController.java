package com.videoannotator.controller;

import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.response.ErrorResponse;
import com.videoannotator.model.response.VideoResponse;
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

@RequestMapping(value = {"/{locale:en|vi}/video", "/video"})
public interface IVideoController {

    @Operation(summary = "List video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register Successful", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = VideoResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/list")
    ResponseEntity<List<VideoResponse>> listVideo();

    @Operation(summary = "Details video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/detail/{id}")
    ResponseEntity<VideoResponse> detailVideo(@Valid @PathVariable Long id);

    @Operation(summary = "Assign video for user")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PatchMapping("/assign")
    ResponseEntity<VideoResponse> assignVideo(@Valid @RequestBody VideoAssignRequest assignRequest);
}
