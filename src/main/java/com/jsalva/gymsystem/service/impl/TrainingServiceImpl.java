package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.repository.TrainingRepository;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.TrainingService;
import com.jsalva.gymsystem.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    private final TrainingTypeService trainingTypeService;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    @Transactional
    public void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingTypeEnum trainingType, LocalDate trainingDate, Integer duration) {
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

        TrainingType type = trainingTypeService.findTrainingTypeByName(trainingType);
        if(type == null){
            logger.error("Error creating training, TrainingType not found");
            throw new IllegalArgumentException("TrainingType not found.");
        }
        // If both exist, proceed
        Training training = new Training.Builder()
                .setTrainer(trainer)
                .setTrainee(trainee)
                .setTrainingName(trainingName)
                .setTrainingType(type)
                .setTrainingDate(trainingDate)
                .setDuration(duration)
                .build();

        trainingRepository.create(training);
        logger.debug("Saved Training: {}", training);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Training getTrainingById(Long id) {
        Optional<Training> training = trainingRepository.findById(id);
        if(training.isEmpty()){
            logger.error("Training id not found");
            throw new IllegalArgumentException("Training with Id " + id + " not found.");
        }
        return training.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        return trainingRepository.getTrainerListByTraineeUsernameOrDateSpan(username,fromDate,toDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        return trainingRepository.getTraineeListByTrainerUsernameOrDateSpan(username,fromDate,toDate);
    }
}
