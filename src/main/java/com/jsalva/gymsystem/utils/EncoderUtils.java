package com.jsalva.gymsystem.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderUtils {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String password){
        return encoder.encode(password);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword){
        return encoder.matches(plainPassword, hashedPassword);
    }
}
