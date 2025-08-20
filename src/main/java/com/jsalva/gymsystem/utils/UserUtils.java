package com.jsalva.gymsystem.utils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class UserUtils {

    public static String generateRandomPassword(){
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(10);

        while (password.length() < 10) {
            int codePoint = random.nextInt(75) + 48; // generates [48, 122]
            if (Character.isLetterOrDigit(codePoint)) {
                password.append((char) codePoint);
            }
        }
        return password.toString();
    }
}
