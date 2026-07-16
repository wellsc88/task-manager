package com.well.tech.task.manager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AdminUserResponse(

        @Schema(
                description = "Unique user identifier",
                example = "fa2f6a08-c392-403b-9d0a-4cba51bcca75"
        )
        UUID id,

        @Schema(
                description = "User full name",
                example = "John Doe"
        )
        String name,

        @Schema(
                description = "User email address",
                example = "john.doe@email.com",
                format = "email"
        )
        String email,

        @Schema(
                description = "User assigned role",
                example = "ADMIN"
        )
        String role,

        @Schema(
                description = "Defines whether the user account is enabled",
                example = "true"
        )
        boolean enabled
) {}