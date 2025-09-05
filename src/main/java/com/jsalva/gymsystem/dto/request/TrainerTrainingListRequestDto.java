package com.jsalva.gymsystem.dto.request;

import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TrainerTrainingListRequestDto(
        @NotBlank(message = "Trainer Username is required")
        String trainerUsername, // Changed to username, Name may generate confusion with homonyms.
        LocalDate fromDate,
        LocalDate toDate,
        String traineeUsername
) {
}
