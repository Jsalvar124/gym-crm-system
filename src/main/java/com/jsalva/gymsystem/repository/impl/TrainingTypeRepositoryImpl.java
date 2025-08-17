package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.repository.TrainingTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeRepositoryImpl extends GenericRepositoryImpl<TrainingType, Long> implements TrainingTypeRepository {
    public TrainingTypeRepositoryImpl(EntityManager em) {
        super(TrainingType.class, em);
    }

    @Override
    public Optional<TrainingType> findTrainingTypeByName(TrainingTypeEnum typeEnum) {
        try {
            TypedQuery<TrainingType> typedQuery = em.createQuery("SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :typeEnum", TrainingType.class);
            typedQuery.setParameter("typeEnum", typeEnum);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e){
            System.out.println("No result!");
            return Optional.empty();
        }
    }
}
