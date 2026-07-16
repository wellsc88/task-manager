package com.well.tech.task.manager.security;

import com.well.tech.task.manager.dto.request.LoginRequest;
import com.well.tech.task.manager.dto.request.UserRequest;
import com.well.tech.task.manager.dto.response.LoginResponse;
import com.well.tech.task.manager.dto.response.UserResponse;
import com.well.tech.task.manager.service.AuthService;
import com.well.tech.task.manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setup() {

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(
                        new LoginResponse(
                                "access-token",
                                "refresh-token"
                        )
                );

        when(userService.create(any(UserRequest.class)))
                .thenReturn(
                        new UserResponse(
                                UUID.randomUUID(),
                                "John Doe",
                                "john@test.com"
                        )
                );
    }

    @Test
    void shouldAllowPublicUserEndpoint() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "John Doe",
                                "email": "john@test.com",
                                "password": "password123"
                            }
                            """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnForbiddenForUserWithoutAdminRole() throws Exception {

        mockMvc.perform(get("/api/admin/users")
                        .with(user("user@test.com").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowPublicAuthEndpoint() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "john@test.com",
                                    "password": "password123"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldBlockProtectedEndpointWithoutToken() throws Exception {

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldBlockAdminEndpointWithoutAuthentication() throws Exception {

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateBCryptPasswordEncoder() {

        String encoded =
                passwordEncoder.encode("password123");

        assert passwordEncoder.matches(
                "password123",
                encoded
        );
    }
}