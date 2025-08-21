package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.repository.TrainingTypeRepository;
import com.jsalva.gymsystem.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final Logger logger = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingType findTrainingTypeByName(TrainingTypeEnum typeEnum) {
        Optional<TrainingType> trainingType = trainingTypeRepository.findTrainingTypeByName(typeEnum);
        if(trainingType.isEmpty()){
            logger.error("Training type with name {} not found", typeEnum.name());
            throw new IllegalArgumentException("Training type with name " + typeEnum.name() + " not found.");
        }
        logger.debug("TrainingType with name {} found", typeEnum.name());
        return trainingType.get();
    }
}
