package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.User;
import com.jsalva.gymsystem.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Logger logger = LoggerFactory.getLogger(TraineeRepositoryImpl.class);

    @PersistenceContext  // Add persistence context to entity manager
    private EntityManager em;

    public UserRepositoryImpl() {
    }


    @Override
    public Optional<User> findByUsername(String username) {
        try {
            TypedQuery<User> typedQuery = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            typedQuery.setParameter("username", username);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e){
            logger.error("No results found for username {}", username);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsernameAndIsActiveTrue(String username) {
        try {
            TypedQuery<User> typedQuery = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.isActive = true", User.class);
            typedQuery.setParameter("username", username);
            return Optional.of(typedQuery.getSingleResult());
        } catch (NoResultException e){
            logger.error("No active users found for username {}", username);
            return Optional.empty();
        }
    }
}
