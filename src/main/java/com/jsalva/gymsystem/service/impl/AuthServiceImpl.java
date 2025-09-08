package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.User;
import com.jsalva.gymsystem.exception.ForbiddenException;
import com.jsalva.gymsystem.exception.InvalidCredentialsException;
import com.jsalva.gymsystem.exception.ResourceNotFoundException;
import com.jsalva.gymsystem.exception.UnauthorizedException;
import com.jsalva.gymsystem.repository.UserRepository;
import com.jsalva.gymsystem.service.AuthService;
import com.jsalva.gymsystem.utils.EncoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;

    private final Map<String, SessionInfo> activeSessions = new ConcurrentHashMap<>();

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static class SessionInfo {
        private final String username;
        private final String userType;

        public SessionInfo(String username, String userType) {
            this.username = username;
            this.userType = userType;
        }

        public String getUsername() { return username; }
        public String getUserType() { return userType; }
    }

    @Override
    @Transactional
    public String login(String username, String password) {
        try{
            User user = findByUsername(username);
            String userType;
            if (user instanceof Trainer) { // Polimorphic query
                userType = "TRAINER";
            } else if (user instanceof Trainee) {
                userType = "TRAINEE";
            } else {
                userType = "USER";
            }

            // Validate credentials
            if (validateCredentials(user, password)) {
                // Generate session ID and store session info
                String sessionId = UUID.randomUUID().toString();
                activeSessions.put(sessionId, new SessionInfo(username, userType));

                logger.info("Login successful - Session created for user: {} as {}", username, userType);
                return sessionId;
            } else {
                throw new InvalidCredentialsException("Invalid credentials for username "+ username);
            }
        } catch (ResourceNotFoundException e) {
            logger.warn("Login failed - user {} not found", username);
            throw new InvalidCredentialsException("Invalid credentials"); // mask as 401
        }
    }

    @Override
    public void logout(String sessionId) {
        SessionInfo removedSession = activeSessions.remove(sessionId);

        if (removedSession != null) {
            logger.info("Logout successful for user: {}", removedSession.getUsername());
        } else {
            logger.warn("Logout attempted with invalid session ID");
        }
    }

    @Override
    public boolean isValidSession(String sessionId) {
        return sessionId != null && activeSessions.containsKey(sessionId);
    }

    @Override
    public String getUsernameFromSession(String sessionId) {
        SessionInfo session = activeSessions.get(sessionId);
        return session != null ? session.getUsername() : null;
    }

    @Override
    public String getUserTypeFromSession(String sessionId) {
        SessionInfo session = activeSessions.get(sessionId);
        return session != null ? session.getUserType() : null;
    }

    @Override
    public void validateLogin(String sessionId) {
        if(!isValidSession(sessionId)){
            throw new UnauthorizedException("Invalid session - please login");
        }
    }

    @Override
    public void validateTrainerAuth(String sessionId) {
        validateLogin(sessionId);
        if(!getUserTypeFromSession(sessionId).equals("TRAINER")){
            throw new ForbiddenException("Access denied - trainer role required");
        }
    }

    @Override
    public void validateOwnerAuth(String sessionId, String targetUsername) {
        validateLogin(sessionId);
        String sessionUsername = getUsernameFromSession(sessionId);
        if(sessionUsername == null || !sessionUsername.equals(targetUsername)){
            throw new ForbiddenException("Access denied - you can only modify your own profile");
        }
    }

    @Override
    public void validateTrainerOrOwnerAuth(String sessionId, String targetUsername) {
        validateLogin(sessionId);
        String sessionUsername = getUsernameFromSession(sessionId);
        String sessionUserType = getUserTypeFromSession(sessionId);

        boolean isOwner = sessionUsername != null && sessionUsername.equals(targetUsername);
        boolean isTrainer = sessionUserType != null && sessionUserType.equals("TRAINER");


        if(!isOwner && !isTrainer){
            throw new ForbiddenException("Access denied - trainer or owner resource only");
        }
    }

    @Override
    @Transactional
    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = findByUsername(username);
        boolean result = validateCredentials(user, oldPassword);
        if(result){
            String newHashedPassword = EncoderUtils.encryptPassword(newPassword);
            user.setPassword(newHashedPassword); //Dirty Check update
            logger.info("Password updated for user: {}", username);
        } else {
            throw new InvalidCredentialsException("Invalid Credentials, unable to update password for username "+ username);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCredentials(String username, String password) {
        User user = findByUsername(username);
        String hashedPassword = user.getPassword();
        return EncoderUtils.verifyPassword(password, hashedPassword);
    }
    // Overload, to avoid calling findByUsername multiple times
    @Transactional(readOnly = true)
    public boolean validateCredentials(User user, String password) {
        String hashedPassword = user.getPassword();
        return EncoderUtils.verifyPassword(password, hashedPassword);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            logger.error("User with username {} not found", username);
            throw new ResourceNotFoundException("User with username " + username + " not found.");
        }
        logger.info("User {} found", username
        );
        return user.get();
    }
}
