package com.jsalva.gymsystem.dto.response;

import com.jsalva.gymsystem.entity.TrainingTypeEnum;

public record TrainerSummaryDto(
        String username,
        String firstName,
        String lastName,
        TrainingTypeEnum specialization
) {
}
