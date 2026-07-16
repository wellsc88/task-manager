package com.well.tech.task.manager.controller;

import com.well.tech.task.manager.dto.request.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.TaskFilterRequest;
import com.well.tech.task.manager.dto.request.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.PageResponse;
import com.well.tech.task.manager.dto.response.TaskResponse;
import com.well.tech.task.manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(
            @Valid @RequestBody CreateTaskRequest request) {

        return taskService.create(request);
    }

    @GetMapping
    public PageResponse<TaskResponse> findAll(
            TaskFilterRequest filter,
            Pageable pageable) {

        Page<TaskResponse> tasks =
                taskService.findAll(filter, pageable);

        return PageResponse.from(tasks);
    }

    @GetMapping("/{id}")
    public TaskResponse findById(
            @PathVariable UUID id) {

        return taskService.findById(id);
    }

    @PatchMapping("/{id}")
    public TaskResponse patch(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request) {

        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        taskService.delete(id);
    }
}