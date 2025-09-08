package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerResponseDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;

import java.util.List;
import java.util.Set;

public interface TrainerService {
    CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto requestDto);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    TrainerResponseDto updateTrainer(UpdateTrainerRequestDto requestDto);
    void deleteTrainer(Long id);
    void updateActiveState(String username, Boolean isActive);
    TrainerResponseDto findByUsername(String username);
    Trainer findEntityByUsername(String username);
    Set<Trainee> getTraineeSetForTrainer(Long id);

}
