package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.repository.TrainingRepository;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

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
        Training training = new Training.Builder()
                .setTrainer(trainer)
                .setTrainee(trainee)
                .setTrainingName(trainingName)
                .setTrainingType(trainingType)
                .setTrainingDate(trainingDate)
                .setDuration(duration)
                .build();

        logger.debug("Saved Training: {}", training);

    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Training getTrainingById(Long id) {
        Optional<Training> training = trainingRepository.findById(id);
        if(training.isEmpty()){
            logger.error("Training id not found");
            throw new IllegalArgumentException("Training with Id " + id + " not found.");
        }
        return training.get();
    }

    @Override
    public List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        return trainingRepository.getTrainerListByTraineeUsernameOrDateSpan(username,fromDate,toDate);
    }

    @Override
    public List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        return trainingRepository.getTraineeListByTrainerUsernameOrDateSpan(username,fromDate,toDate);
    }
}
