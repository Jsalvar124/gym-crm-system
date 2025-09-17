package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerSummaryDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TraineeService {
    CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    TraineeResponseDto updateTrainee(UpdateTraineeRequestDto requestDto);
    void updateActiveState(String username, Boolean isActive);
    TraineeResponseDto findByUsername(String username);
    Trainee findEntityByUsername(String username);
    void deleteTraineeByUsername(String username);
    List<TrainerSummaryDto> findUnassignedTrainersByTrainee(String traineeUsername);
    Set<Trainer> getTrainersSetForTrainee(Long id);
}
