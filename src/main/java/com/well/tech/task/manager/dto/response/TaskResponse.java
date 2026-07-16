package com.well.tech.task.manager.dto.response;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Task details")
public record TaskResponse(

        @Schema(
                description = "Unique task identifier",
                example = "fa2f6a08-c392-403b-9d0a-4cba51bcca75"
        )
        UUID id,

        @Schema(
                description = "Task title",
                example = "Implement JWT authentication"
        )
        String title,

        @Schema(
                description = "Task description",
                example = "Implement authentication flow using JWT access and refresh tokens"
        )
        String description,

        @Schema(
                description = "Current task status",
                example = "PENDING"
        )
        TaskStatus status,

        @Schema(
                description = "Task priority",
                example = "HIGH"
        )
        TaskPriority priority,

        @Schema(
                description = "Task due date and time",
                example = "2026-07-20T18:00:00"
        )
        LocalDateTime dueDate,

        @Schema(
                description = "Task creation date and time",
                example = "2026-07-16T10:52:31"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Last task update date and time",
                example = "2026-07-16T14:30:15"
        )
        LocalDateTime updatedAt

) {
}