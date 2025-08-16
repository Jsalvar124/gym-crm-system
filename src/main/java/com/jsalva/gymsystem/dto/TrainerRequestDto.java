package com.jsalva.gymsystem.dto;


public record TrainerRequestDto(
        String firstName,
        String lastName,
        TrainingTypeDto trainingType
) {

}
