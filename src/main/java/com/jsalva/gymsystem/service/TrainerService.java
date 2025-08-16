package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void createTrainer(String firstName, String lastName, TrainingType trainingType);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive);
    void deleteTrainer(Long id);
    void toggleActiveState(Long id);
    boolean validateCredentials(String username, String password);
    Trainer findByUsername(String username);
    void updatePassword(Long id, String newPassword);
}
