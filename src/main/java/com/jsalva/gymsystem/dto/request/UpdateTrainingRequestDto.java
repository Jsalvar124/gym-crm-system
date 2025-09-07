package com.jsalva.gymsystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateTrainingRequestDto(
        @NotBlank(message = "Trainee username is required")
        String traineeUsername,
        @NotBlank(message = "Trainer username is required")
        String trainerUsername,
        @NotBlank(message = "Training name is required")
        String trainingName,
        @NotNull(message = "Training date is required")
        LocalDate trainingDate,
        @NotNull(message = "Training duration is required")
        @Min(value = 30, message = "Training duration must be at least 30 minutes")
        @Max(value = 240, message = "Training duration must not exceed 240 minutes")
        Integer trainingDuration
)
{ }
