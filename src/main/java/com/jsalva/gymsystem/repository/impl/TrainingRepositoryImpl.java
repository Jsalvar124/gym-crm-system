package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.repository.GenericRepository;
import com.jsalva.gymsystem.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainingRepositoryImpl extends GenericRepository<Training, Long> implements TrainingRepository {
    public TrainingRepositoryImpl(Class<Training> entityClass, EntityManager em) {
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
