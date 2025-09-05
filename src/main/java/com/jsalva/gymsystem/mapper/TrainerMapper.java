package com.jsalva.gymsystem.mapper;

import com.jsalva.gymsystem.dto.response.TrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerSummaryDto;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",  uses = TraineeMapper.class)
public interface TrainerMapper {
    TrainerResponseDto toResponseDto(Trainer trainer);

    TrainerSummaryDto toSummaryDto(Trainer trainer);

    List<TrainerSummaryDto> toSummaryDtoList(List<Trainer> trainers);

    default TrainingTypeEnum map(TrainingType trainingType) {
        return trainingType.getTrainingTypeName();
    }
}
