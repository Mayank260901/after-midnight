package com.aftermidnight.controller;

import com.aftermidnight.dto.ApiResponse;
import com.aftermidnight.dto.PageResponse;
import com.aftermidnight.dto.ThoughtRequest;
import com.aftermidnight.dto.ThoughtResponse;
import com.aftermidnight.entity.Thought;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.security.CustomUserDetails;
import com.aftermidnight.service.ThoughtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@RestController
@RequestMapping("/api/v1/thoughts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Thoughts", description = "Endpoints for managing thoughts")
public class ThoughtController {

    private final ThoughtService thoughtService;

    private ThoughtResponse mapToResponse(Thought thought) {
        return ThoughtResponse.builder()
                .id(thought.getId())
                .content(thought.getContent())
                .createdAt(thought.getCreatedAt())
                .status(thought.getStatus())
                .publishedAt(thought.getPublishedAt())
                .viewCount(thought.getViewCount())
                .likeCount(thought.getLikeCount())
                .userId(thought.getUser() != null ? thought.getUser().getId() : null)
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new thought")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thought created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<ThoughtResponse>> create(@jakarta.validation.Valid @RequestBody ThoughtRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Creating thought for user: {}", userDetails.getUsername());
        Thought thought = Thought.builder()
                .content(request.getContent())
                .status(request.getStatus() != null ? request.getStatus() : PublicationStatus.DRAFT)
                .user(userDetails.getUser())
                .build();
        Thought createdThought = thoughtService.create(thought);
        return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(createdThought), "Thought created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all thoughts for current user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thoughts retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<PageResponse<ThoughtResponse>>> getAllByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "Filter by publication status") @RequestParam(required = false) PublicationStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching thoughts with status: {} and pageable: {}", status, pageable);
        Page<Thought> thoughts;
        if (userDetails != null) {
            if (status != null) {
                thoughts = thoughtService.getAllByUserAndStatus(userDetails.getUser(), status, pageable);
            } else {
                thoughts = thoughtService.getAllByUser(userDetails.getUser(), pageable);
            }
        } else {
            // For public access, only show PUBLISHED items
            thoughts = thoughtService.getAllPublished(pageable);
        }
        Page<ThoughtResponse> responsePage = thoughts.map(this::mapToResponse);
        return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(PageResponse.fromPage(responsePage), "Thoughts retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get thought by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thought retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden access to draft content",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Thought not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<ThoughtResponse>> getById(@Parameter(description = "ID of the thought to retrieve") @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Fetching thought with id: {}", id);
        return thoughtService.getById(id)
                .map(thought -> {
                    if (thought.getStatus() == PublicationStatus.DRAFT) {
                        if (userDetails == null || !thought.getUser().getId().equals(userDetails.getUser().getId())) {
                            log.warn("Unauthorized access attempt to draft thought id: {} by unauthenticated user or non-owner", id);
                            return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<ThoughtResponse>error("Unauthorized access to draft content", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
                        }
                    }
                    return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(thought), "Thought retrieved successfully"));
                })
                .orElseGet(() -> {
                    log.warn("Thought with id: {} not found", id);
                    return ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Thought not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND));
                });
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update thought status")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thought status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the owner",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Thought not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<ThoughtResponse>> updateStatus(
            @Parameter(description = "ID of the thought to update") @PathVariable Long id,
            @Parameter(description = "New status for the thought") @RequestParam PublicationStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Updating status for thought with id: {} to {}", id, status);
        return thoughtService.getById(id).map(thought -> {
            if (!thought.getUser().getId().equals(userDetails.getUser().getId())) {
                return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<ThoughtResponse>error("Unauthorized to update status", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
            }
            Thought updatedThought = thoughtService.updateStatus(id, status);
            return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(updatedThought), "Thought status updated successfully"));
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Thought not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a thought")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thought deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the owner",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Thought not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<Void>> delete(@Parameter(description = "ID of the thought to delete") @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Deleting thought with id: {}", id);
        return thoughtService.getById(id).map(thought -> {
            if (!thought.getUser().getId().equals(userDetails.getUser().getId())) {
                return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<Void>error("Unauthorized to delete thought", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
            }
            thoughtService.delete(id);
            com.aftermidnight.dto.ApiResponse<Void> response = com.aftermidnight.dto.ApiResponse.success(null, "Thought deleted successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Thought not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }

    @PostMapping("/{id}/view")
    @Operation(summary = "Increment view count for a thought")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "View count incremented"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Thought not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<Void>> incrementViewCount(@Parameter(description = "ID of the thought to increment view count") @PathVariable Long id) {
        log.info("Incrementing view count for thought id: {}", id);
        return thoughtService.getById(id).map(thought -> {
            thoughtService.incrementViewCount(id);
            return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.<Void>success(null, "View count incremented"));
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Thought not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }
}
