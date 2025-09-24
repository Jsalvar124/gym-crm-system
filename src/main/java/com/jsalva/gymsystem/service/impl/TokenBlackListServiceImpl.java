package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.service.TokenBlackListService;
import com.jsalva.gymsystem.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlackListServiceImpl implements TokenBlackListService {
    private static final Logger logger = LoggerFactory.getLogger(TokenBlackListServiceImpl.class);

    private final JwtUtils jwtUtils;

    // <Token, ExpDate>
    private final Map<String, LocalDateTime> blackList = new ConcurrentHashMap<>();

    public TokenBlackListServiceImpl(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void blackListToken(String token){
        LocalDateTime expiration = jwtUtils.getExpirationDateFromJwtToken(token);
        blackList.put(token, expiration);
        logger.info("Token blacklisted, expires at: {}", expiration);
    }

    @Override
    public boolean isBlackListed(String token){
        LocalDateTime expiration = blackList.get(token);
        if(expiration==null){
            return false;
        }
        if(LocalDateTime.now().isAfter(expiration)){
            //already expired token, remove it
            blackList.remove(token);
            return false;
        }
        return true;
    }
}
