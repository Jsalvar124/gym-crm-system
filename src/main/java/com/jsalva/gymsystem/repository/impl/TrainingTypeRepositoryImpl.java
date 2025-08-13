package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.repository.GenericRepository;
import com.jsalva.gymsystem.repository.TrainerRepository;
import com.jsalva.gymsystem.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainingTypeRepositoryImpl extends GenericRepository<TrainingType, Long> {
    public TrainingTypeRepositoryImpl(Class<TrainingType> entityClass, EntityManager em) {
        super(entityClass, em);
    }
}
