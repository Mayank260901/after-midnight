package com.aftermidnight.dto;

import com.aftermidnight.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
@Schema(description = "Standard API Response contract")
public class ApiResponse<T> {
    @Schema(description = "Indicates whether the operation was successful", example = "true")
    private final boolean success;

    @Schema(description = "A message describing the result of the operation", example = "Operation successful")
    private final String message;

    @Schema(description = "Error code if the operation failed", example = "VALIDATION_ERROR")
    private final ErrorCode errorCode;

    @Schema(description = "The data returned by the operation")
    private final T data;

    @Schema(description = "The timestamp when the response was generated", example = "2024-01-16T10:00:00")
    private final LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
