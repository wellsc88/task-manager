package com.well.tech.task.manager.controller;

import com.well.tech.task.manager.dto.request.LoginRequest;
import com.well.tech.task.manager.dto.request.LogoutRequest;
import com.well.tech.task.manager.dto.request.RefreshTokenRequest;
import com.well.tech.task.manager.dto.response.LoginResponse;
import com.well.tech.task.manager.dto.response.RefreshTokenResponse;
import com.well.tech.task.manager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Endpoints for user authentication and token management"
)
public class AuthController {

    private final AuthService service;


    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user using email and password and returns JWT tokens."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                service.login(request)
        );
    }


    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Token refreshed successfully"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid or expired refresh token"
    )
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                service.refreshToken(request)
        );
    }


    @Operation(
            summary = "Logout user",
            description = "Invalidates the refresh token and ends the user session."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Logout successful"
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest request) {

        service.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }
}