package com.aftermidnight.controller;

import com.aftermidnight.dto.ApiResponse;
import com.aftermidnight.dto.PageResponse;
import com.aftermidnight.dto.SongRequest;
import com.aftermidnight.dto.SongResponse;
import com.aftermidnight.entity.Song;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.security.CustomUserDetails;
import com.aftermidnight.service.SongService;
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
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Songs", description = "Endpoints for managing songs")
public class SongController {

    private final SongService songService;

    private SongResponse mapToResponse(Song song) {
        return SongResponse.builder()
                .id(song.getId())
                .title(song.getTitle())
                .lyrics(song.getLyrics())
                .audioUrl(song.getAudioUrl())
                .createdAt(song.getCreatedAt())
                .status(song.getStatus())
                .publishedAt(song.getPublishedAt())
                .viewCount(song.getViewCount())
                .likeCount(song.getLikeCount())
                .userId(song.getUser() != null ? song.getUser().getId() : null)
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new song")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Song created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<SongResponse>> create(@jakarta.validation.Valid @RequestBody SongRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Creating song for user: {}", userDetails.getUsername());
        Song song = Song.builder()
                .title(request.getTitle())
                .lyrics(request.getLyrics())
                .audioUrl(request.getAudioUrl())
                .status(request.getStatus() != null ? request.getStatus() : PublicationStatus.DRAFT)
                .user(userDetails.getUser())
                .build();
        Song createdSong = songService.create(song);
        return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(createdSong), "Song created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all songs for current user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Songs retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<PageResponse<SongResponse>>> getAllByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "Filter by publication status") @RequestParam(required = false) PublicationStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching songs with status: {} and pageable: {}", status, pageable);
        Page<Song> songs;
        if (userDetails != null) {
            if (status != null) {
                songs = songService.getAllByUserAndStatus(userDetails.getUser(), status, pageable);
            } else {
                songs = songService.getAllByUser(userDetails.getUser(), pageable);
            }
        } else {
            // For public access, only show PUBLISHED items
            songs = songService.getAllPublished(pageable);
        }
        Page<SongResponse> responsePage = songs.map(this::mapToResponse);
        return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(PageResponse.fromPage(responsePage), "Songs retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get song by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Song retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden access to draft content",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Song not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<SongResponse>> getById(@Parameter(description = "ID of the song to retrieve") @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Fetching song with id: {}", id);
        return songService.getById(id)
                .map(song -> {
                    if (song.getStatus() == PublicationStatus.DRAFT) {
                        if (userDetails == null || !song.getUser().getId().equals(userDetails.getUser().getId())) {
                            log.warn("Unauthorized access attempt to draft song id: {} by unauthenticated user or non-owner", id);
                            return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<SongResponse>error("Unauthorized access to draft content", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
                        }
                    }
                    return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(song), "Song retrieved successfully"));
                })
                .orElseGet(() -> {
                    log.warn("Song with id: {} not found", id);
                    return ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Song not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND));
                });
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update song status")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Song status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the owner",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Song not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<SongResponse>> updateStatus(
            @Parameter(description = "ID of the song to update") @PathVariable Long id,
            @Parameter(description = "New status for the song") @RequestParam PublicationStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Updating status for song with id: {} to {}", id, status);
        return songService.getById(id).map(song -> {
            if (!song.getUser().getId().equals(userDetails.getUser().getId())) {
                return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<SongResponse>error("Unauthorized to update status", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
            }
            Song updatedSong = songService.updateStatus(id, status);
            return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(mapToResponse(updatedSong), "Song status updated successfully"));
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Song not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a song")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Song deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not the owner",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Song not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<Void>> delete(@Parameter(description = "ID of the song to delete") @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Deleting song with id: {}", id);
        return songService.getById(id).map(song -> {
            if (!song.getUser().getId().equals(userDetails.getUser().getId())) {
                return ResponseEntity.status(403).body(com.aftermidnight.dto.ApiResponse.<Void>error("Unauthorized to delete song", com.aftermidnight.exception.ErrorCode.UNAUTHORIZED));
            }
            songService.delete(id);
            com.aftermidnight.dto.ApiResponse<Void> response = com.aftermidnight.dto.ApiResponse.success(null, "Song deleted successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Song not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }

    @PostMapping("/{id}/view")
    @Operation(summary = "Increment view count for a song")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "View count incremented"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Song not found",
                     content = @Content(schema = @Schema(implementation = com.aftermidnight.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<Void>> incrementViewCount(@Parameter(description = "ID of the song to increment view count") @PathVariable Long id) {
        log.info("Incrementing view count for song id: {}", id);
        return songService.getById(id).map(song -> {
            songService.incrementViewCount(id);
            return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.<Void>success(null, "View count incremented"));
        }).orElseGet(() -> ResponseEntity.status(404).body(com.aftermidnight.dto.ApiResponse.error("Song not found", com.aftermidnight.exception.ErrorCode.RESOURCE_NOT_FOUND)));
    }
}
