package com.jsalva.gymsystem.facade;

import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface GymFacade {
    //Trainer
    void createTrainer(String firstName, String lastName, TrainingType trainingType);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive);
    void deleteTrainer(Long id);
    //Trainee
    void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth);
    void deleteTrainee(Long id);
    //Training
    void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Integer duration);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
}
