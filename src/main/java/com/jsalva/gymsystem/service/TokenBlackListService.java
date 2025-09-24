package com.jsalva.gymsystem.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface TokenBlackListService {
    void blackListToken(String token);
    boolean isBlackListed(String token);
}
