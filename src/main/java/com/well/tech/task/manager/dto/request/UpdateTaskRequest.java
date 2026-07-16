package com.well.tech.task.manager.dto.request;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateTaskRequest(

        @Schema(
                description = "Task title",
                example = "Implement JWT authentication",
                minLength = 1
        )
        @NotBlank
        String title,

        @Schema(
                description = "Detailed task description",
                example = "Implement authentication flow using JWT tokens"
        )
        String description,

        @Schema(
                description = "Task priority level",
                example = "HIGH"
        )
        @NotNull
        TaskPriority priority,

        @Schema(
                description = "Current task status",
                example = "IN_PROGRESS"
        )
        @NotNull
        TaskStatus status,

        @Schema(
                description = "Task due date and time",
                example = "2026-07-20T18:00:00"
        )
        LocalDateTime dueDate
) {
}