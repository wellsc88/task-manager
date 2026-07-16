package com.well.tech.task.manager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(

        @Schema(
                description = "User role assigned to the account",
                example = "ADMIN"
        )
        @NotNull
        String role
) {}
