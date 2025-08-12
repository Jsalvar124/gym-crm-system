package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.repository.GenericRepository;
import com.jsalva.gymsystem.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class TrainerRepositoryImpl extends GenericRepository<Trainer, Long> implements TrainerRepository {
    public TrainerRepositoryImpl(Class<Trainer> entityClass, EntityManager em) {
        super(entityClass, em);
    }

    @Override
    public void toggleActiveState(Long id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Trainer trainer = em.find(Trainer.class, id);
            if(trainer != null){
                boolean current = trainer.getActive();
                trainer.setActive(!current);
                em.merge(trainer);
                tx.commit();
                System.out.println("Active status for id "+ id + " set to: " +!current);
            }else{
                System.out.println("Id not found");
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
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
    public Optional<Trainer> findByUsername(String username) {
        try {
            TypedQuery<Trainer> typedQuery = em.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class);
            typedQuery.setParameter("username", username);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e){
            System.out.println("No result!");
            return Optional.empty();
        }
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Trainer trainer = em.find(Trainer.class, id);
            if (trainer != null) {
                trainer.setPassword(newPassword);
                em.merge(trainer);
                tx.commit();
            } else {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

}
