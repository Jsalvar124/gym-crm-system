package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;

public interface TrainingTypeService {
    TrainingType findTrainingTypeByName(TrainingTypeEnum typeEnum);
}
