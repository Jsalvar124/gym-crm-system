package com.jsalva.gymsystem.dto.response;

import java.time.LocalDate;
import java.util.List;

public record TraineeResponseDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        Boolean active,
        List<TrainerSummaryDto> trainers
) {
}
