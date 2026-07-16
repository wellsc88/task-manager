package com.well.tech.task.manager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Paginated response")
public record PageResponse<T>(

        @Schema(description = "List of items in the current page")
        List<T> content,

        @Schema(
                description = "Current page number (zero-based)",
                example = "0"
        )
        int page,

        @Schema(
                description = "Number of items per page",
                example = "10"
        )
        int size,

        @Schema(
                description = "Total number of available items",
                example = "57"
        )
        long totalElements,

        @Schema(
                description = "Total number of available pages",
                example = "6"
        )
        int totalPages

) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}