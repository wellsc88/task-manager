package com.well.tech.task.manager.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(

        @NotNull
        Boolean enabled

) {
}