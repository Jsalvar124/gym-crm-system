package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.User;
import jakarta.persistence.EntityManager;

public class UserRepositoryImpl extends GenericRepository<User, Long> implements UserRepository{
    public UserRepositoryImpl(Class<User> entityClass, EntityManager em) {
        super(entityClass, em);
    }
}
