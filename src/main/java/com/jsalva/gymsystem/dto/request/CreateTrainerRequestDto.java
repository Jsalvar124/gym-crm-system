package com.jsalva.gymsystem.dto.request;

import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateTrainerRequestDto(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotNull(message = "Specialization is required")
        TrainingTypeEnum specialization,
        @Email(message = "Invalid email format")
        String email
) {
}
