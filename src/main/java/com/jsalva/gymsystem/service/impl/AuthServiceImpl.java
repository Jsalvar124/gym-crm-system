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

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(String username, String password) {
        try{
            Optional<User> result = userRepository.findByUsername(username);
            if(result.isPresent()){
            User user = result.get();
            String userType;
            String hashedPassword = user.getPassword();
            if (user instanceof Trainer) {
                userType = "TRAINER";
            } else if (user instanceof Trainee) {
                userType = "TRAINEE";
            } else {
                userType = "USER";
            }
            logger.info("USER TYPE: {}", userType);
            return EncoderUtils.verifyPassword(password, hashedPassword)?"VERIFIED CREDENTIALS!":"INVALID CREDENTIALS!";
            }
        } catch (SecurityException e){
            logger.error("Error validating credentials");
            throw e;
        }
        return "INVALID CREDENTIALS!";
    }
}
