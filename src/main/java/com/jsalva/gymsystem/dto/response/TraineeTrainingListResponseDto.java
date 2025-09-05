package com.jsalva.gymsystem.dto.response;

import com.jsalva.gymsystem.entity.TrainingTypeEnum;

import java.time.LocalDate;

public record TraineeTrainingListResponseDto(
        String trainingName,
        LocalDate trainingDate,
        TrainingTypeEnum trainingType,
        Integer duration,
        String trainerName
) {
}
