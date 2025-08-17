package com.jsalva.gymsystem.facade;

import com.jsalva.gymsystem.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface GymFacade {
    boolean login(String username, String password);

    //Trainer
    void createTrainer(String firstName, String lastName, TrainingTypeEnum trainingType);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive);
    void deleteTrainer(Long id);
    void toggleTrainerActiveState(Long id);
    Trainer findTrainerByUsername(String username);
    void updateTrainerPassword(Long id, String newPassword);

    //Trainee
    void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth);
    void deleteTrainee(Long id);
    void toggleTraineeActiveState(Long id);
    Trainee findTraineeByUsername(String username);
    void updateTraineePassword(Long id, String newPassword);
    void deleteTraineeByUsername(String username);
    List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername);

    //Training
    void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Integer duration);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
}
