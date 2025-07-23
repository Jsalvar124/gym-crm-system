package com.jsalva.gymsystem.dao.impl;

import com.jsalva.gymsystem.dao.TraineeDAO;
import com.jsalva.gymsystem.model.Trainee;
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
    public Trainee save(Trainee trainee) {
        logger.info("Saving trainee with ID: {}", trainee.getUserId());
        trainees.put(trainee.getUserId(), trainee);
        logger.info("Saving trainee with ID: {}", trainee.getUserId());
        return null;
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
        return null;
    }

    @Override
    public Trainee update(Trainee trainee) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
