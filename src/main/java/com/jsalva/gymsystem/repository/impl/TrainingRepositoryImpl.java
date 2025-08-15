package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.repository.GenericRepository;
import com.jsalva.gymsystem.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingRepositoryImpl extends GenericRepository<Training, Long> implements TrainingRepository {
    public TrainingRepositoryImpl(Class<Training> entityClass, EntityManager em) {
        super(entityClass, em);
    }

    @Override
    public List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        StringBuilder sb = new StringBuilder("SELECT DISTINCT t.trainer FROM Training t WHERE t.trainee.username = :username");

        if(fromDate != null){
            sb.append("AND t.training_date >= :fromDate");
        }

        if(toDate != null){
            sb.append("AND t.training_date <= :toDate");
        }

        try {
            TypedQuery<Trainer> typedQuery = em.createQuery(sb.toString(), Trainer.class);
            typedQuery.setParameter("username", username);
            if(fromDate != null){
                typedQuery.setParameter("fromDate", fromDate);
            }
            if(toDate != null){
                typedQuery.setParameter("toDate", toDate);
            }
            return typedQuery.getResultList();
        } catch (NoResultException e){
            System.out.println("No results for given username!");
            return List.of();
        }
    }

    @Override
    public List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        return List.of();
    }
}
