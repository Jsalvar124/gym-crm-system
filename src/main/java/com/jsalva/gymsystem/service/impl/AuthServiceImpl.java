package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.User;
import com.jsalva.gymsystem.repository.UserRepository;
import com.jsalva.gymsystem.service.AuthService;
import com.jsalva.gymsystem.utils.EncoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public String login(String username, String password) {
        try{
            User user = findByUsername(username);
            String userType;
            if (user instanceof Trainer) {
                userType = "TRAINER";
            } else if (user instanceof Trainee) {
                userType = "TRAINEE";
            } else {
                userType = "USER";
            }
            logger.info("USER TYPE: {}", userType);

            return validateCredentials(user, password)?"VERIFIED CREDENTIALS!":"INVALID CREDENTIALS!";
        } catch (RuntimeException e){
            logger.error("Error validating credentials");
            throw e;
        }
    }

    @Override
    @Transactional
    public void updatePassword(String username, String oldPassword, String newPassword) {
        try{
            User user = findByUsername(username);
            boolean result = validateCredentials(user, oldPassword);
            if(result){
                String newHashedPassword = EncoderUtils.encryptPassword(newPassword);
                user.setPassword(newHashedPassword); //Dirty Check update
                logger.info("Password Updated!");
            } else {
                logger.error("Invalid Credentials, unable to update password!");
            }
        } catch (RuntimeException e) {
            logger.error("Error updating password");
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCredentials(String username, String password) {
        try {
            User user = findByUsername(username);
            String hashedPassword = user.getPassword();
            return EncoderUtils.verifyPassword(password, hashedPassword);
        } catch (SecurityException e){
            logger.error("Error validating credentials");
            throw e;
        }
    }
    // Oveload, to avoid calling findByUsername multiple times
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
            throw new IllegalArgumentException("User with username " + username + " not found.");
        }
        logger.info("User found: {}", user.get());
        return user.get();
    }


}
