package com.well.tech.task.manager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshTokenResponse(

        @Schema(
                description = "New JWT access token used to authenticate API requests",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        String accessToken

) {
}