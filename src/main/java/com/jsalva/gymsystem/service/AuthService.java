package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
    String login(String username, String password);

    // JWT management
    boolean isValidToken(String token);
    String getUsernameFromToken(String token);
    String getUserTypeFromToken(String token);

    // Authorization validations
    void validateLogin(String token);
    void validateTrainerAuth(Authentication authentication);
    void validateOwnerAuth(Authentication authentication, String targetUsername);
    void validateTrainerOrOwnerAuth(Authentication authentication, String targetUsername);

    void updatePassword(String username, String oldPassword, String newPassword);
    boolean validateCredentials(String username, String password);
    User findByUsername(String username);
}
