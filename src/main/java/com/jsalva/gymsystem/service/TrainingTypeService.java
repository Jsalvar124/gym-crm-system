package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.response.TrainingTypeResponseDto;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;

import java.util.List;

public interface TrainingTypeService {
    TrainingType findTrainingTypeByName(TrainingTypeEnum typeEnum);
    List<TrainingTypeResponseDto> getAllTrainingTypes();
}
