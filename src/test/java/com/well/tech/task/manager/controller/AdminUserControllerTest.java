package com.well.tech.task.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.well.tech.task.manager.dto.request.UpdateRoleRequest;
import com.well.tech.task.manager.dto.request.UpdateStatusRequest;
import com.well.tech.task.manager.dto.response.AdminUserResponse;
import com.well.tech.task.manager.security.JwtAuthenticationFilter;
import com.well.tech.task.manager.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminUserService service;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldFindAllUsers() throws Exception {

        AdminUserResponse response =
                new AdminUserResponse(
                        UUID.randomUUID(),
                        "John Doe",
                        "john@test.com",
                        "ADMIN",
                        true
                );

        when(service.findAll())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUserRole() throws Exception {

        UUID userId = UUID.randomUUID();

        UpdateRoleRequest request =
                new UpdateRoleRequest(
                        "ADMIN"
                );

        doNothing()
                .when(service)
                .updateRole(
                        userId,
                        request
                );

        mockMvc.perform(
                        patch("/api/admin/users/{id}/role", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isNoContent());

        verify(service)
                .updateRole(
                        userId,
                        request
                );
    }

    @Test
    void shouldUpdateUserStatus() throws Exception {

        UUID userId = UUID.randomUUID();

        UpdateStatusRequest request =
                new UpdateStatusRequest(
                        true
                );

        doNothing()
                .when(service)
                .updateStatus(
                        userId,
                        request
                );

        mockMvc.perform(
                        patch("/api/admin/users/{id}/status", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isNoContent());

        verify(service)
                .updateStatus(
                        userId,
                        request
                );
    }
}