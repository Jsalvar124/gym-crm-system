package com.jsalva.gymsystem.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncoderUtils {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String password){
        return encoder.encode(password);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword){
        return encoder.matches(plainPassword, hashedPassword);
    }
}
