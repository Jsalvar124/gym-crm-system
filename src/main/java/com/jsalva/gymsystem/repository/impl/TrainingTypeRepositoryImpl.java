package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.TrainingType;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeRepositoryImpl extends GenericRepositoryImpl<TrainingType, Long> {
    public TrainingTypeRepositoryImpl(Class<TrainingType> entityClass, EntityManager em) {
        super(entityClass, em);
    }
}
