package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.repository.TrainerRepository;
import com.jsalva.gymsystem.utils.EncoderUtils;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepositoryImpl extends GenericRepositoryImpl<Trainer, Long> implements TrainerRepository {

    private final Logger logger = LoggerFactory.getLogger(TrainerRepositoryImpl.class);

    public TrainerRepositoryImpl(EntityManager em) {
        super(Trainer.class, em);
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
                logger.debug("Active status for id {} set to: {}",id, !current);
            }else{
                logger.info("Id {} not found", id);
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean validateCredentials(String username, String password) {
        try {
            Optional<Trainer> trainer = findByUsername(username);
            if (trainer.isPresent()) {
                String hashedPassword = trainer.get().getPassword();
                return EncoderUtils.verifyPassword(password, hashedPassword);
            } else {
                return false; // Trainer not found, password is false.
            }
        } catch (SecurityException e){
            logger.error("Error validating credentials");
            throw e;
        }
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        try {
            TypedQuery<Trainer> typedQuery = em.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class);
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
            Trainer trainer = em.find(Trainer.class, id);
            if (trainer != null) {
                String hashedPassword = EncoderUtils.encryptPassword(newPassword);
                trainer.setPassword(hashedPassword);
                em.merge(trainer);
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
