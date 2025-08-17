package com.jsalva.gymsystem.utils;

import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Stream;

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
