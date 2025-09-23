package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.service.BruteForceProtectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtectorServiceImpl implements BruteForceProtectorService {
    private static final Logger logger = LoggerFactory.getLogger(BruteForceProtectorServiceImpl.class);

    private static final Integer MAX_ATTEMPTS = 3;
    private static final Integer LOGOUT_TIME_MIN = 5;

    // store number of attempts. (Username, Attempts)
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    // store locked usernames for 5 minutes (Username, LocalDateTime: start end time)
    private final Map<String, LocalDateTime> lockedUsernames = new ConcurrentHashMap<>();

    @Override
    public void registerFailedLogin(String username){
        Integer updatedAttempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, updatedAttempts);

        if(updatedAttempts >= MAX_ATTEMPTS){
            lockedUsernames.put(username, LocalDateTime.now().plusMinutes(LOGOUT_TIME_MIN)); // locked end time
            logger.warn("Username {} locked for {} minutes, reached maximum number of login attempts ({})", username, LOGOUT_TIME_MIN, MAX_ATTEMPTS);
        } else {
            logger.info("Failed login attempt number {} for user {}", updatedAttempts, username);
        }
    }
    @Override
    public void registerSuccessfulLogin(String username){
        loginAttempts.remove(username);
        lockedUsernames.remove(username);
        logger.info("Successful login for user {}, cleared failed attempts", username);
    }

    @Override
    public boolean isBlocked(String username){

        if(!lockedUsernames.containsKey(username)){
            //Not blocked
            return false;
        }

        if(lockedUsernames.get(username).isBefore(LocalDateTime.now())){
            // was blocked, but now it's released, remove from lockedUsernames
            loginAttempts.remove(username);
            lockedUsernames.remove(username);
            logger.info("Locked time for {} finished, cleared failed attempts", username);
            return false;
        }
        return true; // locked time exists, and has not yet expired.
    }
    @Override
    public Long calculateRemainingMinutes(String username){
        LocalDateTime endLockTime = lockedUsernames.get(username);
        long remainingMinutes = Duration.between(LocalDateTime.now(), endLockTime).toMinutes();
        return Math.max(0, remainingMinutes);
    }
}
