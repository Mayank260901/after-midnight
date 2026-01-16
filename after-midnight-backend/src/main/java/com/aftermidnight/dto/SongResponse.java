package com.aftermidnight.dto;

import com.aftermidnight.entity.PublicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@Getter
@Builder
@Schema(description = "Response DTO for Song")
public class SongResponse {
    @Schema(description = "Unique identifier of the song", example = "1")
    private final Long id;

    @Schema(description = "Title of the song", example = "Bohemian Rhapsody")
    private final String title;

    @Schema(description = "Lyrics of the song", example = "Is this the real life? Is this just fantasy?")
    private final String lyrics;

    @Schema(description = "URL to the audio file", example = "https://example.com/song.mp3")
    private final String audioUrl;

    @Schema(description = "Timestamp when the song was created")
    private final LocalDateTime createdAt;

    @Schema(description = "Publication status of the song", example = "PUBLISHED")
    private final PublicationStatus status;

    @Schema(description = "Timestamp when the song was published")
    private final LocalDateTime publishedAt;

    @Schema(description = "Number of views the song has received", example = "250")
    private final long viewCount;

    @Schema(description = "Number of likes the song has received", example = "42")
    private final long likeCount;

    @Schema(description = "ID of the user who created the song", example = "1")
    private final Long userId;
}
