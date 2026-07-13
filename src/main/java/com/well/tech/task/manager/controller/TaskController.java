package com.well.tech.task.manager.controller;


import com.well.tech.task.manager.dto.request.task.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.task.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.task.TaskResponse;
import com.well.tech.task.manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(
            @Valid @RequestBody CreateTaskRequest request) {

        return service.create(request);
    }

    @GetMapping
    public List<TaskResponse> findAll() {

        return service.findAll();
    }

    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable UUID id) {

        return service.findById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request) {

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {

        service.delete(id);
    }
}