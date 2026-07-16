package com.well.tech.task.manager.controller;

import com.well.tech.task.manager.dto.request.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.TaskFilterRequest;
import com.well.tech.task.manager.dto.request.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.PageResponse;
import com.well.tech.task.manager.dto.response.TaskResponse;
import com.well.tech.task.manager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(
        name = "Tasks",
        description = "Endpoints for task management"
)
public class TaskController {

    private final TaskService taskService;


    @Operation(
            summary = "Create task",
            description = "Creates a new task for the authenticated user."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Task created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid task data"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(
            @Valid @RequestBody CreateTaskRequest request) {

        return taskService.create(request);
    }


    @Operation(
            summary = "Find tasks",
            description = """
                    Retrieves tasks using pagination and optional filters.
                    
                    Available filters:
                    - status
                    - priority
                    - title
                    - due date
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tasks retrieved successfully"
    )
    @GetMapping
    public PageResponse<TaskResponse> findAll(
            TaskFilterRequest filter,
            Pageable pageable) {

        Page<TaskResponse> tasks =
                taskService.findAll(filter, pageable);

        return PageResponse.from(tasks);
    }


    @Operation(
            summary = "Find task by id",
            description = "Retrieves a task by its unique identifier."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Task found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Task not found"
    )
    @GetMapping("/{id}")
    public TaskResponse findById(
            @PathVariable UUID id) {

        return taskService.findById(id);
    }


    @Operation(
            summary = "Update task",
            description = "Updates task fields partially."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Task updated successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Task not found"
    )
    @PatchMapping("/{id}")
    public TaskResponse patch(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request) {

        return taskService.update(id, request);
    }

    @Operation(
            summary = "Delete task",
            description = "Deletes a task by its identifier."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Task deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Task not found"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        taskService.delete(id);
    }
}