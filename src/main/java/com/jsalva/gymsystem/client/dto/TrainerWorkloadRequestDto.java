package com.jsalva.gymsystem.client.dto;

import java.time.LocalDate;

public record TrainerWorkloadRequestDto(
        String username,
        String firstName,
        String lastName,
        Boolean isActive,
        LocalDate trainingDate,
        Integer trainingDuration,
        ActionType actionType
) {
    public enum ActionType {
        ADD,
        DELETE
    }
}