package com.well.tech.task.manager.dto.request;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateTaskRequest(

        @NotBlank
        String title,

        String description,

        @NotNull
        TaskPriority priority,

        @NotNull
        TaskStatus status,

        LocalDateTime dueDate
) {
}
