package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends GenericRepository<Trainer, Long>{
    void toggleActiveState(Long id);
    boolean validateCredentials(String username, String password);
    Optional<Trainer> findByUsername(String username);
    void updatePassword(Long id, String newPassword);
    String generateUniqueUsername(String firstName, String lastName);
}
