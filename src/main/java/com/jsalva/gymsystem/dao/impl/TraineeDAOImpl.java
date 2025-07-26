package com.jsalva.gymsystem.dao.impl;

import com.jsalva.gymsystem.dao.TraineeDAO;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDAOImpl.class);

    private Map<Long, Object> trainees;

    // Setter Injection
    @Autowired
    public void setCommonStorage(Map<String, Map<Long, Object>> commonStorage) {
        logger.debug("SetCommonStorage() inside TraineeDAO with setter Injection Called.");
        this.trainees = commonStorage.get("trainees"); // only pass the trainers to the class to avoid multiple responsibility.
    }


    @Override
    public void save(Trainee trainee) {
        logger.info("Saving trainee with ID: {}", trainee.getUserId());
        trainees.put(trainee.getUserId(), trainee);
        logger.info("Saving trainee with ID: {}", trainee.getUserId());
    }

    @Override
    public List<Trainee> findAll() {
        List<Trainee> traineeList = new ArrayList<>();
        logger.info("Retriving all trainees");
        trainees.forEach((id, trainee)->{
            traineeList.add((Trainee) trainee);
        });
        logger.debug("Found {} trainees", traineeList.size());
        return traineeList;    }

    @Override
    public Trainee findById(Long id) {
        if (id == null) {
            logger.error("Attempted to find trainee with null ID");
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }
        logger.info("Searching for trainee with id {}", id);
        return (Trainee) trainees.get(id);
    }

    @Override
    public void update(Trainee trainee) {
        if (trainee == null || trainee.getUserId() == null) {
            logger.error("Attempted to update null trainee or trainee with null ID");
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }
        logger.info("Updating trainee with id {}", trainee.getUserId());
        trainees.put(trainee.getUserId(), trainee);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            logger.error("Attempted to remove trainee with null ID");
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }
        trainees.remove(id);
        logger.info("Deleting trainee with id {}", id);
    }
}
