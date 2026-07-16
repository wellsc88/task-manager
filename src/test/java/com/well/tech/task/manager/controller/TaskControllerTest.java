package com.well.tech.task.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.well.tech.task.manager.common.enums.TaskStatus;
import com.well.tech.task.manager.dto.request.CreateTaskRequest;
import com.well.tech.task.manager.dto.request.UpdateTaskRequest;
import com.well.tech.task.manager.dto.response.TaskResponse;
import com.well.tech.task.manager.common.enums.TaskPriority;
import com.well.tech.task.manager.security.JwtAuthenticationFilter;
import com.well.tech.task.manager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldCreateTask() throws Exception {

        CreateTaskRequest request =
                new CreateTaskRequest(
                        "Study Spring Boot",
                        "Create controller tests",
                        TaskPriority.HIGH,
                        LocalDateTime.now().plusDays(7)
                );

        TaskResponse response =
                new TaskResponse(
                        UUID.randomUUID(),
                        "Study Spring Boot",
                        "Create controller tests",
                        TaskStatus.PENDING,
                        TaskPriority.HIGH,
                        LocalDateTime.now().plusDays(7),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );


        when(taskService.create(any(CreateTaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Study Spring Boot"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"));
    }

    @Test
    void shouldReturnBadRequestWhenCreateTaskWithInvalidTitle()
            throws Exception {

        CreateTaskRequest request =
                new CreateTaskRequest(
                        "",
                        "Description",
                        TaskPriority.HIGH,
                        LocalDateTime.now().plusDays(7)
                );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindTasks() throws Exception {

        TaskResponse response =
                new TaskResponse(
                        UUID.randomUUID(),
                        "Task 1",
                        "Description",
                        TaskStatus.PENDING,
                        TaskPriority.MEDIUM,
                        LocalDateTime.now().plusDays(3),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );

        when(taskService.findAll(any(), any()))
                .thenReturn(
                        new PageImpl<>(
                                List.of(response),
                                PageRequest.of(0, 10),
                                1
                        )
                );

        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title")
                        .value("Task 1"));
    }

    @Test
    void shouldFindTaskById() throws Exception {

        UUID id = UUID.randomUUID();

        TaskResponse response =
                new TaskResponse(
                        id,
                        "Task Test",
                        "Description",
                        TaskStatus.COMPLETED,
                        TaskPriority.LOW,
                        LocalDateTime.now().plusDays(5),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );

        when(taskService.findById(id))
                .thenReturn(response);

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Task Test"))
                .andExpect(jsonPath("$.priority")
                        .value("LOW"));
    }

    @Test
    void shouldUpdateTask() throws Exception {

        UUID id = UUID.randomUUID();

        UpdateTaskRequest request =
                new UpdateTaskRequest(
                        "Updated title",
                        "Updated description",
                        TaskPriority.HIGH,
                        TaskStatus.IN_PROGRESS,
                        LocalDateTime.now().plusDays(10)
                );

        TaskResponse response =
                new TaskResponse(
                        id,
                        "Updated title",
                        "Updated description",
                        TaskStatus.COMPLETED,
                        TaskPriority.HIGH,
                        LocalDateTime.now().plusDays(10),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );

        when(taskService.update(
                eq(id),
                any(UpdateTaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Updated title"));
    }

    @Test
    void shouldDeleteTask() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(taskService)
                .delete(id);

        mockMvc.perform(delete("/api/tasks/{id}", id))
                .andExpect(status().isNoContent());

        verify(taskService)
                .delete(id);
    }
}