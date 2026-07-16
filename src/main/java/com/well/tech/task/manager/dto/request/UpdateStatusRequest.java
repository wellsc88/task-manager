package com.well.tech.task.manager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(

        @Schema(
                description = "Defines whether the user is active or disabled",
                example = "true"
        )
        @NotNull
        Boolean enabled

) {
}