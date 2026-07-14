package com.well.tech.task.manager.dto.response;

import com.well.tech.task.manager.common.enums.Role;

import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String name,
        String email,
        Role role,
        boolean enabled
) {}