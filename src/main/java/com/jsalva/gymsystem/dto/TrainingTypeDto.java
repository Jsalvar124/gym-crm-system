package com.jsalva.gymsystem.dto;

import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;

public record TrainingTypeDto(
        Long id,
        TrainingTypeEnum trainingTypeName
) {
    public static TrainingTypeDto from(TrainingType entity) {
        return new TrainingTypeDto(entity.getId(), entity.getTrainingTypeName());
    }
}
