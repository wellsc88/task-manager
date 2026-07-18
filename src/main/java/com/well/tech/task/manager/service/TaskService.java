package com.well.tech.task.manager.service;

import com.well.tech.task.manager.dto.request.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.TaskResponse;
import com.well.tech.task.manager.entity.Task;
import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.mapper.TaskMapper;
import com.well.tech.task.manager.repository.TaskRepository;
import com.well.tech.task.manager.repository.specification.TaskSpecification;
import com.well.tech.task.manager.security.AuthenticatedUserService;
import com.well.tech.task.manager.dto.request.TaskFilterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final AuthenticatedUserService authenticatedUserService;

    public TaskResponse create(CreateTaskRequest request) {

        UUID userId =
                authenticatedUserService
                        .getCurrentUser()
                        .getId();

        log.info(
                "Creating task. UserId={}, Title={}",
                userId,
                request.title()
        );

        Task task = mapper.toEntity(request);

        task.setUser(
                authenticatedUserService.getCurrentUser()
        );

        Task saved =
                repository.save(task);

        log.info(
                "Task created successfully. TaskId={}, UserId={}",
                saved.getId(),
                userId
        );

        return mapper.toResponse(saved);
    }

    public Page<TaskResponse> findAll(
            TaskFilterRequest filter,
            Pageable pageable) {

        UUID userId = authenticatedUserService
                .getCurrentUser()
                .getId();

        log.info(
                "Finding tasks. UserId={}, Page={}, Size={}",
                userId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Specification<Task> specification =
                TaskSpecification.filter(filter, userId);

        Page<TaskResponse> response =
                repository.findAll(
                                specification,
                                pageable
                        )
                        .map(mapper::toResponse);

        log.info(
                "Tasks found successfully. UserId={}, Count={}",
                userId,
                response.getTotalElements()
        );

        return response;
    }

    public TaskResponse findById(UUID id) {

        log.info(
                "Finding task by id. TaskId={}",
                id
        );

        return mapper.toResponse(findTaskById(id));
    }

    public TaskResponse update(
            UUID id,
            UpdateTaskRequest request) {

        log.info(
                "Updating task. TaskId={}",
                id
        );

        Task task = findTaskById(id);

        mapper.updateEntity(task, request);

        Task updated =
                repository.save(task);

        log.info(
                "Task updated successfully. TaskId={}",
                updated.getId()
        );

        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {

        log.info(
                "Deleting task. TaskId={}",
                id
        );

        Task task = findTaskById(id);

        repository.delete(task);

        log.info(
                "Task deleted successfully. TaskId={}",
                id
        );
    }

    private Task findTaskById(UUID id) {

        UUID userId = authenticatedUserService
                .getCurrentUser()
                .getId();

        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {

                    log.warn(
                            "Task not found. TaskId={}, UserId={}",
                            id,
                            userId
                    );

                    return new ResourceNotFoundException(
                            "Task not found with id: " + id
                    );
                });
    }
}