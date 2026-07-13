package com.well.tech.task.manager.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
