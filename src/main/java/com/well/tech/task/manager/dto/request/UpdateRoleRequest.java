package com.well.tech.task.manager.dto.request;

import com.well.tech.task.manager.common.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(
        @NotNull
        Role role
) {}
