package com.jsalva.gymsystem.dto;

import com.jsalva.gymsystem.entity.Trainer;

public record TrainerResponseDto(
        Long id,
        String firstName,
        String lastName,
        String username,
        TrainingTypeDto specialization,  // Full object in response
        boolean isActive
) {
    public static TrainerResponseDto from(Trainer trainer) {
        return new TrainerResponseDto(
                trainer.getId(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getUsername(),
                TrainingTypeDto.from(trainer.getSpecialization()),
                trainer.getActive()
        );
    }
}
