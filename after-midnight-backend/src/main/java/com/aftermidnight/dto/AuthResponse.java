package com.aftermidnight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@Getter
@Builder
@Schema(description = "Response containing authentication token")
public class AuthResponse {
    @Schema(description = "Success message")
    private final String message;

    @Schema(description = "JWT authentication token")
    private final String token;
}
