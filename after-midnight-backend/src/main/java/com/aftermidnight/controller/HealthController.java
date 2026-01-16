package com.aftermidnight.controller;

import com.aftermidnight.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Endpoint for service health check")
public class HealthController {

    @GetMapping
    @Operation(summary = "Check service health")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service is healthy")
    })
    public ResponseEntity<com.aftermidnight.dto.ApiResponse<Map<String, String>>> getHealth() {
        log.info("Health check requested");
        Map<String, String> data = Map.of(
            "status", "UP",
            "message", "After Midnight backend is running"
        );
        return ResponseEntity.ok(com.aftermidnight.dto.ApiResponse.success(data, "Service is healthy"));
    }
}
