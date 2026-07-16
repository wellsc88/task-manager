package com.well.tech.task.manager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(
                description = "User email address",
                example = "john.doe@email.com",
                format = "email",
                minLength = 1
        )
        @NotBlank
        @Email
        String email,

        @Schema(
                description = "User password",
                example = "StrongPassword123",
                minLength = 1
        )
        @NotBlank
        String password

) {
}