package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.dao.TrainingDAO;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    @Autowired
    private TrainingDAO trainingDAO;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Override
    public void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Integer duration) {
        // Check that both trainer and trainee exist.
        Trainer trainer = trainerService.getTrainerById(trainerId);
        if(trainer == null){
            logger.error("Error creating training, Trainer id not found");
            throw new IllegalArgumentException("Trainer with Id " + trainerId + " not found.");
        }

        Trainee trainee = traineeService.getTraineeById(traineeId);
        if(trainee == null){
            logger.error("Error creating training, Trainee id not found");
            throw new IllegalArgumentException("Trainee with Id " + traineeId + " not found.");
        }
        // If both exist, proceed
        Training training = new Training();
        training.setTrainerId(trainerId);
        training.setTraineeId(traineeId);
        training.setTrainingName(trainingName);
        training.setTrainingType(trainingType);
        training.setTrainingDate(trainingDate);
        training.setDuration(duration);

        trainingDAO.save(training);
        logger.debug("Saved Training: {}", training);

    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingDAO.findAll();
    }

    @Override
    public Training getTrainingById(Long id) {
        Training training = trainingDAO.findById(id);
        if(training == null){
            logger.error("Training id not found");
            throw new IllegalArgumentException("Training with Id " + id + " not found.");
        }
        return training;
    }
}
