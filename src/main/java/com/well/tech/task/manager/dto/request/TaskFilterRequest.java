package com.well.tech.task.manager.dto.request;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskFilterRequest {

    @Schema(
            description = "Filter tasks by status",
            example = "PENDING"
    )
    private TaskStatus status;

    @Schema(
            description = "Filter tasks by priority",
            example = "HIGH"
    )
    private TaskPriority priority;

    @Schema(
            description = "Filter tasks by title containing the informed text",
            example = "JWT"
    )
    private String title;
}