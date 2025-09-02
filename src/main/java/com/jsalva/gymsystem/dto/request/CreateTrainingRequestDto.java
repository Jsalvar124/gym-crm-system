package com.jsalva.gymsystem.dto.request;

import java.time.LocalDate;

public record CreateTrainingRequestDto(
        String traineeUsername,
        String trainerUsername,
        String trainingName,
        LocalDate trainingDate,
        Integer trainingDuration
) {
}
