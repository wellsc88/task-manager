package com.well.tech.task.manager.mapper;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import com.well.tech.task.manager.dto.request.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.TaskResponse;
import com.well.tech.task.manager.entity.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    private final TaskMapper mapper = new TaskMapper();

    @Test
    void shouldMapCreateTaskRequestToEntity() {

        LocalDateTime dueDate = LocalDateTime.of(2026, 7, 20, 10, 0);

        CreateTaskRequest request =
                new CreateTaskRequest(
                        "Task title",
                        "Task description",
                        TaskPriority.HIGH,
                        dueDate
                );

        Task result = mapper.toEntity(request);

        assertThat(result)
                .isNotNull();

        assertThat(result.getTitle())
                .isEqualTo("Task title");

        assertThat(result.getDescription())
                .isEqualTo("Task description");

        assertThat(result.getPriority())
                .isEqualTo(TaskPriority.HIGH);

        assertThat(result.getStatus())
                .isEqualTo(TaskStatus.PENDING);

        assertThat(result.getDueDate())
                .isEqualTo(dueDate);
    }

    @Test
    void shouldUpdateTaskEntityWithRequestData() {

        Task task =
                Task.builder()
                        .id(UUID.randomUUID())
                        .title("Old title")
                        .description("Old description")
                        .priority(TaskPriority.LOW)
                        .status(TaskStatus.PENDING)
                        .dueDate(
                                LocalDateTime.of(2026, 7, 10, 10, 0)
                        )
                        .build();

        LocalDateTime dueDate =
                LocalDateTime.of(2026, 8, 1, 10, 0);

        UpdateTaskRequest request =
                new UpdateTaskRequest(
                        "Updated title",
                        "Updated description",
                        TaskPriority.MEDIUM,
                        TaskStatus.IN_PROGRESS,
                        dueDate
                );

        mapper.updateEntity(task, request);

        assertThat(task.getTitle())
                .isEqualTo("Updated title");

        assertThat(task.getDescription())
                .isEqualTo("Updated description");

        assertThat(task.getPriority())
                .isEqualTo(TaskPriority.MEDIUM);

        assertThat(task.getStatus())
                .isEqualTo(TaskStatus.IN_PROGRESS);

        assertThat(task.getDueDate())
                .isEqualTo(dueDate);
    }

    @Test
    void shouldMapTaskEntityToResponse() {

        UUID id = UUID.randomUUID();

        LocalDateTime dueDate =
                LocalDateTime.of(2026, 9, 1, 12, 0);

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 7, 1, 12, 0);

        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 7, 2, 12, 0);

        Task task =
                Task.builder()
                        .id(id)
                        .title("Task title")
                        .description("Task description")
                        .status(TaskStatus.COMPLETED)
                        .priority(TaskPriority.LOW)
                        .dueDate(dueDate)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .build();

        TaskResponse response =
                mapper.toResponse(task);

        assertThat(response.id())
                .isEqualTo(id);

        assertThat(response.title())
                .isEqualTo("Task title");

        assertThat(response.description())
                .isEqualTo("Task description");

        assertThat(response.status())
                .isEqualTo(TaskStatus.COMPLETED);

        assertThat(response.priority())
                .isEqualTo(TaskPriority.LOW);

        assertThat(response.dueDate())
                .isEqualTo(dueDate);

        assertThat(response.createdAt())
                .isEqualTo(createdAt);

        assertThat(response.updatedAt())
                .isEqualTo(updatedAt);
    }
}