package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.repository.TraineeRepository;
import com.jsalva.gymsystem.utils.EncoderUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeRepositoryImpl extends GenericRepositoryImpl<Trainee, Long> implements TraineeRepository {

    private final Logger logger = LoggerFactory.getLogger(TraineeRepositoryImpl.class);


    public TraineeRepositoryImpl(EntityManager em) {
        super(Trainee.class, em);
    }

    public void toggleActiveState(Long id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Trainee trainee = em.find(Trainee.class, id);
            if(trainee != null){
                boolean current = trainee.getActive();
                trainee.setActive(!current);
                em.merge(trainee);
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
        try {
            Optional<Trainee> trainee = findByUsername(username);
            if (trainee.isPresent()) {
                String hashedPassword = trainee.get().getPassword();
                return EncoderUtils.verifyPassword(password, hashedPassword);
            } else {
                return false;
            }
        } catch (SecurityException e){
            logger.error("Error validating credentials");
            throw e;
        }
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        try {
            TypedQuery<Trainee> typedQuery = em.createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class);
            typedQuery.setParameter("username", username);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e){
            logger.error("No results found!");
            return Optional.empty();
        }
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Trainee trainee = em.find(Trainee.class, id);
            if (trainee != null) {
                trainee.setPassword(newPassword);
                em.merge(trainee);
                tx.commit();
            } else {
                tx.rollback();
                logger.warn("Trainer with id {} not found.", id);
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteByUsername(String username) {
        try{
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Optional<Trainee> trainee = findByUsername(username);
            if(trainee.isPresent()){
                delete(trainee.get().getId());
                tx.commit();
            }else {
                tx.rollback();
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
        TypedQuery<Trainer> typedQuery = em.createQuery(
                """
                      SELECT t FROM Trainer t
                      WHERE t NOT IN (
                        SELECT DISTINCT tr.trainer FROM Training tr
                        WHERE tr.trainee.username = :traineeUsername
                        )
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
