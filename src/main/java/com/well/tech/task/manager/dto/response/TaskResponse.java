package com.well.tech.task.manager.dto.response;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(

        UUID id,

        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        LocalDateTime dueDate,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
