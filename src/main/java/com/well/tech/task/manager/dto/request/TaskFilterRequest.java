package com.well.tech.task.manager.dto.request;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskFilterRequest {

    private TaskStatus status;

    private TaskPriority priority;

    private String title;
}