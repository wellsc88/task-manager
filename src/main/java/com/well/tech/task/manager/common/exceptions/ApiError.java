package com.well.tech.task.manager.common.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Standard API error response")
public record ApiError(

        @Schema(
                description = "HTTP status code",
                example = "400"
        )
        int status,

        @Schema(
                description = "HTTP status reason",
                example = "Bad Request"
        )
        String error,

        @Schema(
                description = "Detailed error message",
                example = "title: must not be blank"
        )
        String message,

        @Schema(
                description = "Request path that generated the error",
                example = "/api/tasks"
        )
        String path,

        @Schema(
                description = "Timestamp when the error occurred",
                example = "2026-07-16T15:42:10.123Z"
        )
        Instant timestamp
) {
}