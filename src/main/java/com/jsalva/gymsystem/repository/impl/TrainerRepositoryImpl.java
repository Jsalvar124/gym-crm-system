package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.repository.GenericRepository;
import com.jsalva.gymsystem.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TrainerRepositoryImpl extends GenericRepository<Trainer, Long> implements TrainerRepository {
    public TrainerRepositoryImpl(Class<Trainer> entityClass, EntityManager em) {
        super(entityClass, em);
    }

    @Override
    public void toggleActiveState(String username, Boolean isActive) {
        em.createQuery("UPDATE Trainer t SET t.isActive = :isActive WHERE t.username = :username")
                .setParameter("isActive", isActive)
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        TypedQuery<Long> typedQuery = em.createQuery(
                "SELECT COUNT(t) FROM Trainer t WHERE t.username = :username AND t.password = :password",
                Long.class);
                typedQuery.setParameter("username", username);
                typedQuery.setParameter("password", password);
                return typedQuery.getSingleResult() > 0;
    }

    @Override
    public Trainer findByUsername(String username) {
        return null;
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {

    }

    @Override
    public List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername) {
        return List.of();
    }
}
