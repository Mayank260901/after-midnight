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
@Schema(description = "Request DTO for creating/updating a Thought")
public class ThoughtRequest {
    @NotBlank(message = "Content is required")
    @Schema(description = "Content of the thought", example = "The stars look beautiful tonight.")
    private final String content;
    
    @Schema(description = "Publication status of the thought", example = "DRAFT")
    private final PublicationStatus status;
}
