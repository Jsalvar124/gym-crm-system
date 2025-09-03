package com.jsalva.gymsystem.mapper;

import com.jsalva.gymsystem.dto.response.TrainingTypeResponseDto;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingTypeResponseDto toTrainingTypeDto(TrainingType trainingType);

    List<TrainingTypeResponseDto> toTrainingTypeDtoList(List<TrainingType> trainingTypeList);

    default TrainingTypeEnum map(TrainingType trainingType) {
        return trainingType.getTrainingTypeName();
    }

}
