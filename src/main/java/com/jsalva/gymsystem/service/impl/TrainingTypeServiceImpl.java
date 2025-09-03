package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.dto.response.TrainingTypeResponseDto;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.mapper.TrainingTypeMapper;
import com.jsalva.gymsystem.repository.TrainingTypeRepository;
import com.jsalva.gymsystem.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final Logger logger = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

    private final TrainingTypeRepository trainingTypeRepository;

    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingTypeMapper = trainingTypeMapper;
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

    @Override
    @Transactional(readOnly = true)
    public List<TrainingTypeResponseDto> getAllTrainingTypes() {
        logger.info("Fetching training types");
        List<TrainingType> trainingTypeList = trainingTypeRepository.findAll();
        return trainingTypeMapper.toTrainingTypeDtoList(trainingTypeList);
    }
}
