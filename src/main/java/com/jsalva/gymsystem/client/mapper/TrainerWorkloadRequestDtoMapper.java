package com.jsalva.gymsystem.client.mapper;

import com.jsalva.gymsystem.client.dto.TrainerWorkloadRequestDto;
import com.jsalva.gymsystem.entity.Training;

public class TrainerWorkloadRequestDtoMapper {

    private TrainerWorkloadRequestDtoMapper() {
        // Private constructor enforces utility intention
    }

    public static TrainerWorkloadRequestDto fromTraining(Training training, TrainerWorkloadRequestDto.ActionType actionType){
        return new TrainerWorkloadRequestDto(
                training.getTrainer().getUsername(),
                training.getTrainer().getFirstName(),
                training.getTrainer().getLastName(),
                true, // is active default value
                training.getTrainingDate(),
                training.getDuration(),
                actionType
                );
    }
}
