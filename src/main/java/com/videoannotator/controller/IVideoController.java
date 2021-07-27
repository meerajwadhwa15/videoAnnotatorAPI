package com.videoannotator.controller;

import com.videoannotator.model.request.SegmentRequest;
import com.videoannotator.model.request.VideoAssignRequest;
import com.videoannotator.model.request.VideoRequest;
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

@RequestMapping(value = {"/video"})
public interface IVideoController {

    @Operation(summary = "List video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list Successful", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = VideoResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping
    ResponseEntity<List<VideoResponse>> listVideo();

    @Operation(summary = "Details video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get detail video Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/{videoId}")
    ResponseEntity<VideoResponse> detailVideo(@Valid @PathVariable Long videoId);

    @Operation(summary = "Assign video for user")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assign Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PatchMapping("/{videoId}")
    ResponseEntity<VideoResponse> assignVideo(@RequestBody VideoAssignRequest assignRequest, @Valid @PathVariable Long videoId);

    @Operation(summary = "Add new video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add video Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping
    ResponseEntity<VideoResponse> addVideo(@Valid @RequestBody VideoRequest videoRequest);

    @Operation(summary = "Update video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update video Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PutMapping("/{videoId}")
    ResponseEntity<VideoResponse> updateVideo(@Valid @PathVariable Long videoId, @Valid @RequestBody VideoRequest videoRequest);

    @Operation(summary = "Delete video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete video Successful"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @DeleteMapping("/{videoId}")
    ResponseEntity<String> deleteVideo(@Valid @PathVariable Long videoId);

    @Operation(summary = "Add annotation video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add annotation Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping("/{videoId}/segment")
    ResponseEntity<VideoResponse> addSegment(@Valid @PathVariable Long videoId, @Valid @RequestBody SegmentRequest request);

    @Operation(summary = "Edit annotation video")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit annotation Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PutMapping("/{videoId}/segment/{segmentId}")
    ResponseEntity<VideoResponse> editSegment(@Valid @PathVariable Long videoId, @Valid @RequestBody SegmentRequest request, @PathVariable Long segmentId);

    @Operation(summary = "Delete a video segment")
    @Parameter(in = ParameterIn.HEADER, description = "Access token required", name = "Authorization"
            , content = @Content(), example = "Bearer xxxxx...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @DeleteMapping("{videoId}/segment/{segmentId}")
    ResponseEntity<VideoResponse> deleteSegment(@Valid @PathVariable Long videoId, @Valid @PathVariable Long segmentId);

    @Operation(summary = "List video for public user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list Successful", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = VideoResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/public")
    ResponseEntity<List<VideoResponse>> listVideoPublic();

    @Operation(summary = "Details video public")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get detail video Successful", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = VideoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("public/{videoId}")
    ResponseEntity<VideoResponse> detailVideoPublic(@Valid @PathVariable Long videoId);

}
