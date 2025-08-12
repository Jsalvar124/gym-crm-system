package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.repository.GenericRepository;
import jakarta.persistence.EntityManager;

public class TrainingTypeRepositoryImpl extends GenericRepository<TrainingType, Long> {
    public TrainingTypeRepositoryImpl(Class<TrainingType> entityClass, EntityManager em) {
        super(entityClass, em);
    }
}
