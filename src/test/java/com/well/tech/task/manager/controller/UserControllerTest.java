package com.well.tech.task.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.well.tech.task.manager.dto.request.UserRequest;
import com.well.tech.task.manager.dto.response.UserResponse;
import com.well.tech.task.manager.security.JwtAuthenticationFilter;
import com.well.tech.task.manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldCreateUser() throws Exception {

        UserRequest request =
                new UserRequest(
                        "John Doe",
                        "john@test.com",
                        "password123"
                );

        UserResponse response =
                new UserResponse(
                        UUID.randomUUID(),
                        "John Doe",
                        "john@test.com"
                );


        when(service.create(any(UserRequest.class)))
                .thenReturn(response);


        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("John Doe"))
                .andExpect(jsonPath("$.email")
                        .value("john@test.com"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidRequest() throws Exception {

        UserRequest request =
                new UserRequest(
                        "",
                        "invalid-email",
                        ""
                );


        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}