package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TraineeService {
    CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth);
    void deleteTrainee(Long id);
    void toggleActiveState(Long id);
    boolean validateCredentials(String username, String password);
    Trainee findByUsername(String username);
    void updatePassword(Long id, String newPassword);
    void deleteTraineeByUsername(String username);
    List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername);
    Set<Trainer> getTrainersSetForTrainee(Long id);
}
