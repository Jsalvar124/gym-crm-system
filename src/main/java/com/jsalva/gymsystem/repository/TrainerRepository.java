package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainer;

import java.util.List;

public interface TrainerRepository {
    void toggleActiveState(String username, Boolean isActive);
    boolean validateCredentials(String username, String password);
    Trainer findByUsername(String username);
    void updatePassword(String username, String oldPassword, String newPassword);
    List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername);
}
