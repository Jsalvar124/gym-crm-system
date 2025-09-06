package com.jsalva.gymsystem.dto.response;

import com.jsalva.gymsystem.entity.TrainingTypeEnum;

import java.time.LocalDate;

public record TrainingResponseDto(
        String trainingName,
        LocalDate trainingDate,
        TrainingTypeEnum trainingType,
        Integer duration,
        TraineeSummaryDto trainee,
        TrainerSummaryDto trainer
) {

}
