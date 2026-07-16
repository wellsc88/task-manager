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
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.well.tech.task.manager.dto.request.TaskFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final AuthenticatedUserService authenticatedUserService;

    public TaskResponse create(CreateTaskRequest request) {

        Task task = mapper.toEntity(request);

        task.setUser(
                authenticatedUserService.getCurrentUser()
        );

        return mapper.toResponse(repository.save(task));
    }

    public Page<TaskResponse> findAll(
            TaskFilterRequest filter,
            Pageable pageable) {

        UUID userId = authenticatedUserService
                .getCurrentUser()
                .getId();

        Specification<Task> specification =
                TaskSpecification.filter(filter, userId);

        return repository.findAll(
                        specification,
                        pageable
                )
                .map(mapper::toResponse);
    }

    public TaskResponse findById(UUID id) {

        return mapper.toResponse(findTaskById(id));
    }

    public TaskResponse update(UUID id, UpdateTaskRequest request) {

        Task task = findTaskById(id);

        mapper.updateEntity(task, request);

        return mapper.toResponse(repository.save(task));
    }

    public void delete(UUID id) {

        repository.delete(findTaskById(id));
    }

    private Task findTaskById(UUID id) {

        UUID userId = authenticatedUserService
                .getCurrentUser()
                .getId();

        return repository.findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id
                        )
                );
    }
}