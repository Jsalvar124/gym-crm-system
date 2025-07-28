package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TraineeService {
    void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth);
    void deleteTrainee(Long id);
}
