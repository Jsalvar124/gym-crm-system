package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.User;

import java.util.Optional;

public interface UserService {
    String generateUniqueUsername(String firstName, String lastName);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    Long countUsers();

}
