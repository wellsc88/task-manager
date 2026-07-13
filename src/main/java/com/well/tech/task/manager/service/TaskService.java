package com.well.tech.task.manager.service;

import com.well.tech.task.manager.dto.request.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.TaskResponse;
import com.well.tech.task.manager.entity.Task;
import com.well.tech.task.manager.common.exceptions.resource.ResourceNotFoundException;
import com.well.tech.task.manager.mapper.TaskMapper;
import com.well.tech.task.manager.repository.TaskRepository;
import com.well.tech.task.manager.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<TaskResponse> findAll() {

        UUID userId = authenticatedUserService
                .getCurrentUser()
                .getId();

        return repository.findAllByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
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