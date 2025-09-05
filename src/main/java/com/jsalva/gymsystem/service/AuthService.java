package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.User;

public interface AuthService {
    String login(String username, String password);
    void logout(String sessionId);

    // Session management
    boolean isValidSession(String sessionId);
    String getUsernameFromSession(String sessionId);
    String getUserTypeFromSession(String sessionId);

    // Authorization validations
    void validateLogin(String sessionId);
    void validateTrainerAuth(String sessionId);
    void validateOwnerAuth(String sessionId, String targetUsername);
    void validateTrainerOrOwnerAuth(String sessionId, String targetUsername);

    void updatePassword(String username, String oldPassword, String newPassword);
    boolean validateCredentials(String username, String password);
    User findByUsername(String username);
}
