package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndIsActiveTrue(String username);
}
