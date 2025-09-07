package com.jsalva.gymsystem.dto.response;

import com.jsalva.gymsystem.entity.TrainingTypeEnum;

import java.util.List;

public record TrainerResponseDto(
        String username,
        String firstName,
        String lastName,
        TrainingTypeEnum specialization,
        Boolean active,
        String email,
        List<TraineeSummaryDto> trainees
) {
}
