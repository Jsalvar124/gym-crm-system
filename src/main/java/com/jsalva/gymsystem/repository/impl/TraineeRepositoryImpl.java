package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.exception.ResourceNotFoundException;
import com.jsalva.gymsystem.repository.TraineeRepository;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TraineeRepositoryImpl extends GenericRepositoryImpl<Trainee, Long> implements TraineeRepository {

    private final Logger logger = LoggerFactory.getLogger(TraineeRepositoryImpl.class);

    @PersistenceContext  // Add persistence context to entity manager
    private EntityManager em;

    public TraineeRepositoryImpl() {
        super(Trainee.class);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        try {
            TypedQuery<Trainee> typedQuery = em.createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class);
            typedQuery.setParameter("username", username);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e){
            logger.error("Trainee not found");
            return Optional.empty();
        }
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        try {
            Trainee trainee = em.find(Trainee.class, id);
            if (trainee != null) {
                trainee.setPassword(newPassword);
                em.merge(trainee);
            } else {
                logger.warn("Trainer with id {} not found.", id);
                throw new ResourceNotFoundException("Trainer with id " + id + " not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void deleteByUsername(String username) {
        try{
            Optional<Trainee> result = findByUsername(username);
            if(result.isPresent()){
                Trainee trainee = result.get();
                Set<Trainer> trainers = trainee.getTrainers();
                for (Trainer trainer : trainers) {
                    trainer.getTrainees().remove(trainee); // Remove trainee from trainer's collection
                }
                em.remove(trainee);
                logger.info("Trainee with username {} deleted", username);
            }else {
                logger.warn("Unable to delete, Trainee not found with username {}",username);
            }
        } catch (Exception e) {
            logger.error("Error deleting trainee: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername) {
        // Check that trainee Username exists.
        Optional<Trainee> trainee = findByUsername(traineeUsername);
        if(trainee.isEmpty()){
            logger.error("Username does not exist");
            return List.of();
        }
        // ADDING ONLY ACTIVE TRAINERS FILTER
        TypedQuery<Trainer> typedQuery = em.createQuery(
                """
                      SELECT t FROM Trainer t
                      WHERE t NOT IN (
                        SELECT DISTINCT tr.trainer FROM Training tr
                        WHERE tr.trainee.username = :traineeUsername
                        )
                      AND t.isActive = true
                   """, Trainer.class);
        typedQuery.setParameter("traineeUsername", traineeUsername);
        return typedQuery.getResultList();
    }

    @Override
    public String generateUniqueUsername(String firstName, String lastName) {
        try {
            String baseUsername = firstName + "." + lastName;
            String jpql = "SELECT u.username FROM User u WHERE u.username LIKE :baseUsername";
            List<String> usernames = em.createQuery(jpql, String.class)
                    .setParameter("baseUsername", baseUsername + "%")
                    .getResultList();
            // if it's empty or there are no coincidences, return base
            if(usernames.isEmpty() || !usernames.contains(baseUsername)){
                return baseUsername;
            }

            int counter = 1;
            String uniqueUserName;
            do {
                uniqueUserName = baseUsername + counter;
                counter++;
            } while(usernames.contains(uniqueUserName));

            return uniqueUserName;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
