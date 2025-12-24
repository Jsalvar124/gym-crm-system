package com.jsalva.gymsystem.messaging.mapper;

import com.jsalva.gymsystem.messaging.enums.ActionType;
import com.jsalva.gymsystem.messaging.dto.TrainerWorkloadRequestDto;
import com.jsalva.gymsystem.entity.Training;

public class TrainerWorkloadRequestDtoMapper {

    private TrainerWorkloadRequestDtoMapper() {
        // Private constructor enforces utility intention
    }

    public static TrainerWorkloadRequestDto fromTraining(Training training, ActionType actionType){
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
