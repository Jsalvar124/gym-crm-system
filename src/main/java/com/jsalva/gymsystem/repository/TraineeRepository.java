package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    void toggleActiveState(Long id);
    boolean validateCredentials(String username, String password);
    Optional<Trainee> findByUsername(String username);
    void updatePassword(Long id, String newPassword);
    void deleteByUsername(String username);
    List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername);
}
