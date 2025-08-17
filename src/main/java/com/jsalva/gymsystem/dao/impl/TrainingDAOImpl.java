package com.jsalva.gymsystem.dao.impl;

import com.jsalva.gymsystem.dao.TrainingDAO;
import com.jsalva.gymsystem.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrainingDAOImpl implements TrainingDAO {

    private final Logger logger = LoggerFactory.getLogger(TrainingDAOImpl.class);

    private Map<Long, Object> trainings;

    // Setter Injection
    public void setCommonStorage(Map<String, Map<Long, Object>> commonStorage) {
        logger.debug("SetCommonStorage() inside TrainingDAO with setter Injection Called.");
        this.trainings = commonStorage.get("trainings"); // only pass the trainings to the class to avoid multiple responsibility.
    }

    @Override
    public void save(Training training) {
        logger.info("Saving training with ID: {}", training.getTrainingId());
        trainings.put(training.getTrainingId(), training);
        logger.info("Training saved successfully: {}", training.getTrainingName());
    }

    @Override
    public List<Training> findAll() {
        List<Training> trainingList = new ArrayList<>();
        logger.info("Retrieving all trainings");
        for(Object training: trainings.values()){
            trainingList.add((Training) training);
        }
        logger.info("Found {} trainings", trainingList.size());
        return trainingList;
    }

    @Override
    public Training findById(Long id) {
        if(id == null){
            logger.error("Attempted to find training with null ID");
            throw new IllegalArgumentException("Training ID cannot be null");
        }
        logger.info("Searching for training with id {}", id);
        return (Training) trainings.get(id);
    }
}
