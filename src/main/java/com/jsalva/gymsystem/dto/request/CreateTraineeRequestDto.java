package com.jsalva.gymsystem.dto.request;

import java.time.LocalDate;

public record CreateTraineeRequestDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address
) {
}
