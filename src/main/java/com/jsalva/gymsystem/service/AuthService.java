package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.User;

public interface AuthService {
    String login(String username, String password);
    void updatePassword(String username, String oldPassword, String newPassword);
    boolean validateCredentials(String username, String password);
    User findByUsername(String username);
}
