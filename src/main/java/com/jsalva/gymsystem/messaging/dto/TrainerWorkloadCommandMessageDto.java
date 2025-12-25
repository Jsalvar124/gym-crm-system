package com.jsalva.gymsystem.messaging.dto;

import java.time.LocalDate;

public record TrainerWorkloadCommandMessageDto(
        String username,
        String firstName,
        String lastName,
        Boolean isActive,
        LocalDate trainingDate,
        Integer trainingDuration
        // removed Action Type from payload, Using it as Header Action-type instead
) {
}