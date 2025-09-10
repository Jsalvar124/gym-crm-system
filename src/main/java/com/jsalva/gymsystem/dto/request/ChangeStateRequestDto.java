package com.jsalva.gymsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChangeStateRequestDto(
        @NotNull(message = "Active status is required")
        Boolean isActive
) {
}
