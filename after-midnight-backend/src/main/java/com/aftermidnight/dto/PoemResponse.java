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
@Schema(description = "Response DTO for Poem")
public class PoemResponse {
    @Schema(description = "Unique identifier of the poem", example = "1")
    private final Long id;

    @Schema(description = "Title of the poem", example = "The Road Not Taken")
    private final String title;

    @Schema(description = "Content of the poem", example = "Two roads diverged in a yellow wood...")
    private final String content;

    @Schema(description = "Timestamp when the poem was created")
    private final LocalDateTime createdAt;

    @Schema(description = "Publication status of the poem", example = "PUBLISHED")
    private final PublicationStatus status;

    @Schema(description = "Timestamp when the poem was published")
    private final LocalDateTime publishedAt;

    @Schema(description = "Number of views the poem has received", example = "100")
    private final long viewCount;

    @Schema(description = "Number of likes the poem has received", example = "10")
    private final long likeCount;

    @Schema(description = "ID of the user who created the poem", example = "1")
    private final Long userId;
}
