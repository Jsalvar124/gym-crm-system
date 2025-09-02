package com.jsalva.gymsystem.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateTraineeRequestDto(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        LocalDate dateOfBirth,
        String address,
        @NotBlank(message = "Status is required")
        Boolean isActive
) {
}
