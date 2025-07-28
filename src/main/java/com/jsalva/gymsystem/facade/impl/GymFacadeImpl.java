package com.jsalva.gymsystem.facade.impl;

import com.jsalva.gymsystem.facade.GymFacade;
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
public class GymFacadeImpl implements GymFacade {
    private final Logger logger = LoggerFactory.getLogger(GymFacadeImpl.class);

    private final TrainerService trainerService;

    private final TraineeService traineeService;

    private final TrainingService trainingService;

    @Autowired // optional annotation: Constructor based injection.
    public GymFacadeImpl(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    @Override
    public void createTrainer(String firstName, String lastName, TrainingType trainingType) {
        try {
            trainerService.createTrainer(firstName,lastName,trainingType);
        }catch (IllegalArgumentException e){
            logger.error("Error in trainer creation: {}", e.getMessage());
        }
    }

    @Override
    public List<Trainer> getAllTrainers() {
        try {
            return trainerService.getAllTrainers();
        }catch (Exception e){
            logger.error("Error fetching trainers: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Trainer getTrainerById(Long id) {
        try {
            return trainerService.getTrainerById(id);
        }catch (IllegalArgumentException e){
            logger.error("Error fetching trainer: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive) {
        try {
            trainerService.updateTrainer(userId, firstName, lastName ,trainingType, newPassword, isActive);
        }catch (IllegalArgumentException e){
            logger.error("Error in trainer update: {}", e.getMessage());
        }
    }

    @Override
    public void deleteTrainer(Long id) {
        try {
            trainerService.deleteTrainer(id);
        }catch (IllegalArgumentException e){
            logger.error("Error deleting trainer: {}", e.getMessage());
        }
    }

    @Override
    public void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        try {
            traineeService.createTrainee(firstName, lastName, address, dateOfBirth);
        } catch (IllegalArgumentException e) {
            logger.error("Error in trainee creation: {}", e.getMessage());
        }
    }

    @Override
    public List<Trainee> getAllTrainees() {
        try {
            return traineeService.getAllTrainees();
        } catch (Exception e) {
            logger.error("Error fetching trainees: {}", e.getMessage());
            return List.of();
        }    }

    @Override
    public Trainee getTraineeById(Long id) {
        try {
            return traineeService.getTraineeById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching trainee: {}", e.getMessage());
            return null;
        }    }

    @Override
    public void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth) {
        try {
            traineeService.updateTrainee(userId, firstName, lastName, newPassword, isActive, address, dateOfBirth);
        } catch (IllegalArgumentException e) {
            logger.error("Error in trainee update: {}", e.getMessage());
        }
    }

    @Override
    public void deleteTrainee(Long id) {
        try {
            traineeService.deleteTrainee(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting trainee: {}", e.getMessage());
        }
    }

    @Override
    public void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Integer duration) {
        try {
            trainingService.createTraining(trainerId, traineeId, trainingName, trainingType, trainingDate, duration);
        } catch (IllegalArgumentException e) {
            logger.error("Error in training creation: {}", e.getMessage());
        }
    }

    @Override
    public List<Training> getAllTrainings() {
        try {
            return trainingService.getAllTrainings();
        } catch (Exception e) {
            logger.error("Error fetching trainings: {}", e.getMessage());
            return List.of();
        }    }

    @Override
    public Training getTrainingById(Long id) {
        try {
            return trainingService.getTrainingById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching training: {}", e.getMessage());
            return null;
        }    }
}
