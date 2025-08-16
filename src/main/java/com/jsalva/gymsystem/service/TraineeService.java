package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.Trainee;

import java.time.LocalDate;
import java.util.List;

public interface TraineeService {
    void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth);
    void deleteTrainee(Long id);
    void toggleActiveState(Long id);
    boolean validateCredentials(String username, String password);
    Trainee findByUsername(String username);
    void updatePassword(Long id, String newPassword);
}
