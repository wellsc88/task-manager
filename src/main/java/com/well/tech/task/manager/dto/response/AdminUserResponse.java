package com.well.tech.task.manager.dto.response;

import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String name,
        String email,
        String role,
        boolean enabled
) {}