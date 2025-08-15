package com.jsalva.gymsystem.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderUtils {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encryptPassword(String password){
        return encoder.encode(password);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword){
        return encoder.matches(plainPassword, hashedPassword);
    }
}
