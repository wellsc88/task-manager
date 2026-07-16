package com.well.tech.task.manager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @Schema(
                description = "Refresh token to invalidate during logout",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        @NotBlank
        String refreshToken
) {
}
