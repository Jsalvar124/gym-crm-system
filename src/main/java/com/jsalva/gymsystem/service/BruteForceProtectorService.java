package com.jsalva.gymsystem.service;

public interface BruteForceProtectorService {
    void registerFailedLogin(String username);

    void registerSuccessfulLogin(String username);

    boolean isBlocked(String username);

    Long calculateRemainingMinutes(String username);
}
