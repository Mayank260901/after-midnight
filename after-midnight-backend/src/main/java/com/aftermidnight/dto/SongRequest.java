package com.aftermidnight.dto;

import com.aftermidnight.entity.PublicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@Getter
@Builder
@Jacksonized
@Schema(description = "Request DTO for creating/updating a Song")
public class SongRequest {
    @NotBlank(message = "Title is required")
    @Schema(description = "Title of the song", example = "Bohemian Rhapsody")
    private final String title;
    
    @NotBlank(message = "Lyrics are required")
    @Schema(description = "Lyrics of the song", example = "Is this the real life? Is this just fantasy?")
    private final String lyrics;
    
    @Schema(description = "URL to the audio file", example = "https://example.com/song.mp3")
    private final String audioUrl;
    
    @Schema(description = "Publication status of the song", example = "DRAFT")
    private final PublicationStatus status;
}
