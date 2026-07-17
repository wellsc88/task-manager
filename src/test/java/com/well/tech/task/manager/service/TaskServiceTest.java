package com.well.tech.task.manager.service;

import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.common.enums.TaskStatus;
import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.dto.request.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.TaskFilterRequest;
import com.well.tech.task.manager.dto.request.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.TaskResponse;
import com.well.tech.task.manager.entity.Task;
import com.well.tech.task.manager.entity.User;
import com.well.tech.task.manager.mapper.TaskMapper;
import com.well.tech.task.manager.repository.TaskRepository;
import com.well.tech.task.manager.security.AuthenticatedUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper mapper;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private TaskService service;

    private User user;
    private UUID userId;
    private UUID taskId;
    private Task task;
    private TaskResponse response;

    @BeforeEach
    void setup() {

        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        user = new User();
        user.setId(userId);

        task = new Task();
        task.setId(taskId);
        task.setUser(user);

        response = new TaskResponse(
                taskId,
                "Task",
                "Description",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(authenticatedUserService.getCurrentUser())
                .thenReturn(user);
    }

    @Test
    void shouldCreateTask() {

        CreateTaskRequest request =
                new CreateTaskRequest(
                        "Task",
                        "Description",
                        TaskPriority.HIGH,
                        LocalDateTime.now()
                );

        when(mapper.toEntity(request)).thenReturn(task);
        when(repository.save(task)).thenReturn(task);
        when(mapper.toResponse(task)).thenReturn(response);

        TaskResponse result = service.create(request);

        assertNotNull(result);
        assertEquals(taskId, result.id());

        verify(repository).save(task);
        verify(mapper).toEntity(request);
        verify(mapper).toResponse(task);

        assertEquals(user, task.getUser());
    }

    @Test
    void shouldFindAllTasks() {

        Page<Task> page =
                new PageImpl<>(List.of(task));

        Pageable pageable =
                PageRequest.of(0,10);

        when(repository.findAll(
                ArgumentMatchers.<Specification<Task>>any(),
                eq(pageable)
        )).thenReturn(page);

        when(mapper.toResponse(task))
                .thenReturn(response);

        Page<TaskResponse> result =
                service.findAll(
                        new TaskFilterRequest(),
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        verify(authenticatedUserService)
                .getCurrentUser();

        verify(repository).findAll(
                ArgumentMatchers.<Specification<Task>>any(),
                eq(pageable)
        );
    }

    @Test
    void shouldFindTaskById() {

        when(repository.findByIdAndUserId(taskId,userId))
                .thenReturn(Optional.of(task));

        when(mapper.toResponse(task))
                .thenReturn(response);

        TaskResponse result =
                service.findById(taskId);

        assertEquals(taskId,result.id());
    }

    @Test
    void shouldThrowWhenTaskNotFound() {

        when(repository.findByIdAndUserId(taskId,userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(taskId)
        );
    }

    @Test
    void shouldUpdateTask() {

        UpdateTaskRequest request =
                new UpdateTaskRequest(
                        "Updated",
                        "Description",
                        TaskPriority.LOW,
                        TaskStatus.COMPLETED,
                        LocalDateTime.now()
                );

        when(repository.findByIdAndUserId(taskId,userId))
                .thenReturn(Optional.of(task));

        when(repository.save(task))
                .thenReturn(task);

        when(mapper.toResponse(task))
                .thenReturn(response);

        TaskResponse result =
                service.update(taskId,request);

        assertNotNull(result);

        verify(mapper).updateEntity(task,request);
        verify(repository).save(task);
    }

    @Test
    void shouldThrowWhenUpdatingTaskNotFound() {

        UpdateTaskRequest request =
                new UpdateTaskRequest(
                        "Updated",
                        null,
                        TaskPriority.HIGH,
                        TaskStatus.PENDING,
                        null
                );

        when(repository.findByIdAndUserId(taskId,userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(taskId,request)
        );
    }

    @Test
    void shouldDeleteTask() {

        when(repository.findByIdAndUserId(taskId,userId))
                .thenReturn(Optional.of(task));

        service.delete(taskId);

        verify(repository).delete(task);
    }

    @Test
    void shouldThrowWhenDeletingTaskNotFound() {

        when(repository.findByIdAndUserId(taskId,userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(taskId)
        );
    }
}