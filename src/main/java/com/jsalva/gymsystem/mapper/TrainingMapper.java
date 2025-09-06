package com.jsalva.gymsystem.mapper;

import com.jsalva.gymsystem.dto.response.TraineeTrainingListResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerTrainingListResponseDto;
import com.jsalva.gymsystem.dto.response.TrainingResponseDto;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    @Mapping(target = "traineeName", expression = "java(training.getTrainee().getFirstName() + \" \" + training.getTrainee().getLastName())")
    TrainerTrainingListResponseDto toTrainerResponseDto(Training training);

    List<TrainerTrainingListResponseDto> toTrainerResponseDtoList(List<Training> training);


    @Mapping(target = "trainerName", expression = "java(training.getTrainer().getFirstName() + \" \" + training.getTrainer().getLastName())")
    TraineeTrainingListResponseDto toTraineeResponseDto(Training training);

    List<TraineeTrainingListResponseDto> toTraineeResponseDtoList(List<Training> training);

    // new TrainingResponseDto mapping
    @Mapping(target = "trainee", source = "trainee") // will use TraineeMapper.toSummaryDto
    @Mapping(target = "trainer", source = "trainer") // will use TrainerMapper.toSummaryDto
    @Mapping(target = "trainingType", expression = "java(map(training.getTrainingType()))")
    TrainingResponseDto toTrainingResponseDto(Training training);

    List<TrainingResponseDto> toTrainingResponseDtoList(List<Training> trainings);

    default TrainingTypeEnum map(TrainingType trainingType) {
        return trainingType != null ? trainingType.getTrainingTypeName() : null;
    }
}


