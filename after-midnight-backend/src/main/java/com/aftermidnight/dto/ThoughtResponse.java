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
@Schema(description = "Response DTO for Thought")
public class ThoughtResponse {
    @Schema(description = "Unique identifier of the thought", example = "1")
    private final Long id;

    @Schema(description = "Content of the thought", example = "The stars look beautiful tonight.")
    private final String content;

    @Schema(description = "Timestamp when the thought was created")
    private final LocalDateTime createdAt;

    @Schema(description = "Publication status of the thought", example = "PUBLISHED")
    private final PublicationStatus status;

    @Schema(description = "Timestamp when the thought was published")
    private final LocalDateTime publishedAt;

    @Schema(description = "Number of views the thought has received", example = "50")
    private final long viewCount;

    @Schema(description = "Number of likes the thought has received", example = "5")
    private final long likeCount;

    @Schema(description = "ID of the user who created the thought", example = "1")
    private final Long userId;
}
