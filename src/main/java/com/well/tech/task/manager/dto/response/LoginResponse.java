package com.well.tech.task.manager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(

        @Schema(
                description = "JWT access token used to authenticate API requests",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        String accessToken,

        @Schema(
                description = "Refresh token used to obtain a new access token",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        String refreshToken

) {
}
