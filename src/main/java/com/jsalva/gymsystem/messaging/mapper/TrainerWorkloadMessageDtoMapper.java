package com.jsalva.gymsystem.messaging.mapper;

import com.jsalva.gymsystem.messaging.dto.TrainerWorkloadCommandMessageDto;
import com.jsalva.gymsystem.entity.Training;

public class TrainerWorkloadMessageDtoMapper {

    private TrainerWorkloadMessageDtoMapper() {
        // Private constructor enforces utility intention
    }

    public static TrainerWorkloadCommandMessageDto fromTraining(Training training){
        return new TrainerWorkloadCommandMessageDto(
                training.getTrainer().getUsername(),
                training.getTrainer().getFirstName(),
                training.getTrainer().getLastName(),
                true, // is active default value
                training.getTrainingDate(),
                training.getDuration()
                );
    }
}
