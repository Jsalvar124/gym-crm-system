package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;

import java.util.Optional;

public interface TrainingTypeRepository extends GenericRepository<TrainingType, Long>{
    Optional<TrainingType> findTrainingTypeByName(TrainingTypeEnum typeEnum);
}
