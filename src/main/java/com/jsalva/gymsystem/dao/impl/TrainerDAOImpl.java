package com.jsalva.gymsystem.dao.impl;

import com.jsalva.gymsystem.dao.TrainerDAO;
import com.jsalva.gymsystem.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDAOImpl implements TrainerDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDAOImpl.class);

    private Map<Long, Object> trainers;

    // Setter Injection
    @Autowired
    public void setCommonStorage(Map<String, Map<Long, Object>> commonStorage) {
        logger.debug("SetCommonStorage() inside TrainerDAO with setter Injection Called.");
        this.trainers = commonStorage.get("trainers"); // only pass the trainers to the class to avoid multiple responsibility.
    }

    // TODO REMOVE, THIS IS JUT FOR MAIN TEST
    public Map<Long, Object> getTrainers() {
        return trainers;
    }

    @Override
    public void save(Trainer trainer) {
        logger.info("Saving trainer with ID: {}", trainer.getUserId());
        trainers.put(trainer.getUserId(), trainer);
        logger.debug("Trainer saved successfully: {}", trainer.getUsername());
    }

    @Override
    public List<Trainer> findAll() {
        List<Trainer> trainerList = new ArrayList<>();
        logger.info("Retriving all trainers");
        trainers.forEach((id, trainer)->{
            trainerList.add((Trainer) trainer);
        });
        logger.debug("Found {} trainers", trainerList.size());
        return trainerList;
    }

    @Override
    public Trainer findById(Long id) {
        if (id == null) {
            logger.error("Attempted to find trainer with null ID");
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        logger.info("Searching for trainer with id {}", id);
        return (Trainer) trainers.get(id);
    }

    @Override
    public void update(Trainer trainer) {
        if (trainer == null || trainer.getUserId() == null) {
            logger.error("Attempted to update null trainer or trainer with null ID");
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        logger.info("Updating trainer with id {}", trainer.getUserId());
        trainers.put(trainer.getUserId(), trainer);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            logger.error("Attempted to remove trainer with null ID");
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        trainers.remove(id);
        logger.info("Deleting trainer with id {}", id);
    }
}
