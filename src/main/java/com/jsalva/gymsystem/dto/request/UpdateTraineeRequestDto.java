package com.jsalva.gymsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateTraineeRequestDto(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        String address,

        @NotNull(message = "Status is required")
        Boolean isActive,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
