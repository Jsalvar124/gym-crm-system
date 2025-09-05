package com.jsalva.gymsystem.dto.request;

import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TraineeTrainingListRequestDto(
        @NotBlank(message = "Trainee Username is required")
        String traineeUsername,
        LocalDate fromDate,
        LocalDate toDate,
        String trainerUsername, // Changed to username, Name may generate confusion with homonyms.
        TrainingTypeEnum trainingType
) {
}
