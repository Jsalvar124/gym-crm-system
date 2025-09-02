package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainerService {
    CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto requestDto);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive);
    void deleteTrainer(Long id);
    void toggleActiveState(Long id);
    boolean validateCredentials(String username, String password);
    Trainer findByUsername(String username);
    void updatePassword(Long id, String newPassword);
    Set<Trainee> getTraineeSetForTriner(Long id);

}
