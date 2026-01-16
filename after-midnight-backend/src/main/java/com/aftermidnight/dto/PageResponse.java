package com.aftermidnight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * API CONTRACT FROZEN
 * Backend API is LOCKED.
 * Changes require version bump (v2).
 * No breaking changes allowed in v1.
 */
@Getter
@Builder
@Schema(description = "Generic paginated response wrapper")
public class PageResponse<T> {
    @Schema(description = "List of items in the current page")
    private final List<T> content;

    @Schema(description = "Current page number (zero-based)", example = "0")
    private final int pageNumber;

    @Schema(description = "Number of items per page", example = "10")
    private final int pageSize;

    @Schema(description = "Total number of elements across all pages", example = "100")
    private final long totalElements;

    @Schema(description = "Total number of pages", example = "10")
    private final int totalPages;

    @Schema(description = "Indicates if this is the last page", example = "false")
    private final boolean last;

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}