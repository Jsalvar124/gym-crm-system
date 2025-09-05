package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainingRepositoryImpl extends GenericRepositoryImpl<Training, Long> implements TrainingRepository {

    @PersistenceContext  // Add persistence context to entity manager
    private EntityManager em;

    public TrainingRepositoryImpl() {
        super(Training.class);
    }

    @Override
    public List<Training> getTrainersTrainingListByTraineeUsernameOrDateSpan(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername) {
        StringBuilder sb = new StringBuilder("SELECT t FROM Training t WHERE t.trainer.username = :trainerUsername");

        if(traineeUsername != null){
            sb.append(" AND t.trainee.username = :traineeUsername");
        }
        if(fromDate != null){
            sb.append(" AND t.trainingDate >= :fromDate");
        }
        if(toDate != null){
            sb.append(" AND t.trainingDate <= :toDate");
        }

        try {
            TypedQuery<Training> typedQuery = em.createQuery(sb.toString(), Training.class);
            typedQuery.setParameter("trainerUsername", trainerUsername);
            if(traineeUsername != null){
                typedQuery.setParameter("traineeUsername", traineeUsername);
            }
            if(fromDate != null){
                typedQuery.setParameter("fromDate", fromDate);
            }
            if(toDate != null){
                typedQuery.setParameter("toDate", toDate);
            }
            return typedQuery.getResultList();
        } catch (NoResultException e){
            System.out.println("No results for given trainer username and filters!");
            return List.of();
        }
    }

    @Override
    public List<Training> getTraineesTrainingListByTrainerUsernameOrDateSpan(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, TrainingTypeEnum trainingType) {
        StringBuilder sb = new StringBuilder("SELECT t FROM Training t WHERE t.trainee.username = :traineeUsername");

        if(trainerUsername != null){
            sb.append(" AND t.trainer.username = :trainerUsername");
        }
        if(fromDate != null){
            sb.append(" AND t.trainingDate >= :fromDate");
        }

        if(toDate != null){
            sb.append(" AND t.trainingDate <= :toDate");
        }

        if (trainingType != null) {
            sb.append(" AND t.trainingType.trainingTypeName = :trainingType");
        }

        try {
            TypedQuery<Training> typedQuery = em.createQuery(sb.toString(), Training.class);
            typedQuery.setParameter("traineeUsername", traineeUsername);
            if(trainerUsername != null){
                typedQuery.setParameter("trainerUsername", trainerUsername);
            }
            if(fromDate != null){
                typedQuery.setParameter("fromDate", fromDate);
            }
            if(toDate != null){
                typedQuery.setParameter("toDate", toDate);
            }
            if(trainingType != null){
                typedQuery.setParameter("trainingType", trainingType);
            }
            return typedQuery.getResultList();
        } catch (NoResultException e){
            System.out.println("No results for given trainer username and filters!");
            return List.of();
        }    }
}
