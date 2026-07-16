package com.well.tech.task.manager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @Schema(
                description = "Refresh token used to generate a new access token",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        @NotBlank
        String refreshToken
) {
}
