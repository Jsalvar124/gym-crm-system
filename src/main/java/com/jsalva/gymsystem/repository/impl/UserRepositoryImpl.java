package com.jsalva.gymsystem.repository.impl;

import com.jsalva.gymsystem.entity.User;
import com.jsalva.gymsystem.repository.GenericRepository;
import com.jsalva.gymsystem.repository.UserRepository;
import jakarta.persistence.EntityManager;

// TODO CHECK IF CAN BE DELETED
public class UserRepositoryImpl extends GenericRepository<User, Long> implements UserRepository {
    public UserRepositoryImpl(Class<User> entityClass, EntityManager em) {
        super(entityClass, em);
    }
}
