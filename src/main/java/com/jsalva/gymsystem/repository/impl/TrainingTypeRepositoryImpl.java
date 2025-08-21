package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.repository.TrainingTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeRepositoryImpl extends GenericRepositoryImpl<TrainingType, Long> implements TrainingTypeRepository {

    @PersistenceContext  // Add persistence context to entity manager
    private EntityManager em;

    public TrainingTypeRepositoryImpl() {
        super(TrainingType.class);
    }

    private final Logger logger = LoggerFactory.getLogger(TrainingTypeRepositoryImpl.class);

    @Override
    public Optional<TrainingType> findTrainingTypeByName(TrainingTypeEnum typeEnum) {
        try {
            TypedQuery<TrainingType> typedQuery = em.createQuery("SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :typeEnum", TrainingType.class);
            typedQuery.setParameter("typeEnum", typeEnum);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e){
            logger.error("No results found for training type {}", typeEnum.name());
            return Optional.empty();
        }
    }
}
