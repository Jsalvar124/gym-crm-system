package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;

import java.util.List;

public interface TrainerService {
    void createTrainer(String firstName, String lastName, TrainingType trainingType);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive);
    void deleteTrainer(Long id);
}
