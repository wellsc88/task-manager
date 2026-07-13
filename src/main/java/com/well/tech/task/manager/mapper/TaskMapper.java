package com.well.tech.task.manager.mapper;

import com.well.tech.task.manager.common.enums.TaskStatus;
import com.well.tech.task.manager.dto.request.task.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.task.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.task.TaskResponse;
import com.well.tech.task.manager.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequest request) {

        return Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .status(TaskStatus.PENDING)
                .dueDate(request.dueDate())
                .build();
    }

    public void updateEntity(Task task, UpdateTaskRequest request) {

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setStatus(request.status());
        task.setDueDate(request.dueDate());
    }

    public TaskResponse toResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}