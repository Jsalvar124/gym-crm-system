package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.repository.GenericRepository;
import com.jsalva.gymsystem.repository.TrainerRepository;
import com.jsalva.gymsystem.repository.TrainingRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class TrainingTypeRepositoryImpl extends GenericRepository<TrainingType, Long> implements TrainingRepository {
    public TrainingTypeRepositoryImpl(Class<TrainingType> entityClass, EntityManager em) {
        super(entityClass, em);
    }

    @Override
    public List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        return List.of();
    }

    @Override
    public List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        return List.of();
    }
}
