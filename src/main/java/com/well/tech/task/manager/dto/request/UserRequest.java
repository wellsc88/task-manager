package com.well.tech.task.manager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(

        @Schema(
                description = "User full name",
                example = "John Doe"
        )
        @NotBlank
        String name,

        @Schema(
                description = "User email address",
                example = "john.doe@email.com"
        )
        @NotBlank
        @Email
        String email,

        @Schema(
                description = "User password",
                example = "Password@123",
                minLength = 8
        )
        @NotBlank
        String password
) {
}