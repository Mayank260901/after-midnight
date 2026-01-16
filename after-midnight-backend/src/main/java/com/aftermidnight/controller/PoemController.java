package com.aftermidnight.controller;

import com.aftermidnight.dto.ApiResponse;
import com.aftermidnight.dto.PageResponse;
import com.aftermidnight.dto.PoemRequest;
import com.aftermidnight.dto.PoemResponse;
import com.aftermidnight.entity.Poem;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import com.aftermidnight.security.CustomUserDetails;
import com.aftermidnight.service.PoemService;
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
import java.util.stream.Collectors;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@RestController
@RequestMapping("/api/v1/poems")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Poems", description = "Endpoints for managing poems")
public class PoemController {

    private final PoemService poemService;

    private PoemResponse mapToResponse(Poem poem) {
        return PoemResponse.builder()
                .id(poem.getId())
                .title(poem.getTitle())
                .content(poem.getContent())
                .createdAt(poem.getCreatedAt())
                .status(poem.getStatus())
                .publishedAt(poem.getPublishedAt())
                .viewCount(poem.getViewCount())
                .likeCount(poem.getLikeCount())
                .userId(poem.getUser() != null ? poem.getUser().getId() : null)
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new poem")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Poem created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<PoemResponse>> create(@jakarta.validation.Valid @RequestBody PoemRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Creating poem for user: {}", userDetails.getUsername());
        Poem poem = Poem.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(request.getStatus() != null ? request.getStatus() : PublicationStatus.DRAFT)
                .user(userDetails.getUser())
                .build();
        Poem createdPoem = poemService.create(poem);
        return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(createdPoem), "Poem created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all poems for current user", description = "Supports pagination, sorting and filtering by status")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Poems retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<PageResponse<PoemResponse>>> getAllByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "Filter by publication status") @RequestParam(required = false) PublicationStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching poems with status: {} and pageable: {}", status, pageable);
        Page<Poem> poems;
        if (userDetails != null) {
            if (status != null) {
                poems = poemService.getAllByUserAndStatus(userDetails.getUser(), status, pageable);
            } else {
                poems = poemService.getAllByUser(userDetails.getUser(), pageable);
            }
        } else {
            // For public access, only show PUBLISHED items
            poems = poemService.getAllPublished(pageable);
        }
        Page<PoemResponse> responsePage = poems.map(this::mapToResponse);
        return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(PageResponse.fromPage(responsePage), "Poems retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get poem by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Poem retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden access to draft content",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Poem not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<PoemResponse>> getById(@Parameter(description = "ID of the poem to retrieve") @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Fetching poem with id: {}", id);
        return poemService.getById(id)
                .map(poem -> {
                    if (poem.getStatus() == PublicationStatus.DRAFT) {
                        if (userDetails == null || !poem.getUser().getId().equals(userDetails.getUser().getId())) {
                            log.warn("Unauthorized access attempt to draft poem id: {} by unauthenticated user or non-owner", id);
                            return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<PoemResponse>error("Unauthorized access to draft content", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
                        }
                    }
                    return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(poem), "Poem retrieved successfully"));
                })
                .orElseGet(() -> {
                    log.warn("Poem with id: {} not found", id);
                    return ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Poem not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND));
                });
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update poem status (DRAFT/PUBLISHED)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Poem status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the owner",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Poem not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<PoemResponse>> updateStatus(
            @Parameter(description = "ID of the poem to update") @PathVariable Long id,
            @Parameter(description = "New status for the poem") @RequestParam PublicationStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Updating status for poem with id: {} to {}", id, status);
        return poemService.getById(id).map(poem -> {
            if (!poem.getUser().getId().equals(userDetails.getUser().getId())) {
                return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<PoemResponse>error("Unauthorized to update status", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
            }
            Poem updatedPoem = poemService.updateStatus(id, status);
            return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(updatedPoem), "Poem status updated successfully"));
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Poem not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a poem (soft delete)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Poem deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the owner",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Poem not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<Void>> delete(@Parameter(description = "ID of the poem to delete") @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Deleting poem with id: {}", id);
        return poemService.getById(id).map(poem -> {
            if (!poem.getUser().getId().equals(userDetails.getUser().getId())) {
                return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<Void>error("Unauthorized to delete poem", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
            }
            poemService.delete(id);
            com.aftermidnight.dto.ApiResponse<Void> response = com.aftermidnight.dto.ApiResponse.success(null, "Poem deleted successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Poem not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }

    @PostMapping("/{id}/view")
    @Operation(summary = "Increment view count for a poem")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "View count incremented"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Poem not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<Void>> incrementViewCount(@Parameter(description = "ID of the poem to increment view count") @PathVariable Long id) {
        log.info("Incrementing view count for poem id: {}", id);
        return poemService.getById(id).map(poem -> {
            poemService.incrementViewCount(id);
            return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.<Void>success(null, "View count incremented"));
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Poem not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }
}
