package com.jsalva.gymsystem.utils;

import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Stream;


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

    public static String generateUniqueUsername(String name, String lastName, List<Trainer> trainers, List<Trainee> trainees){
        //Counts how many Homonyms are inside Trainer and Trainee storage and creates a username as Name.LastnameX for X>1
        // rest of the logic
        String baseUsername = name+ "." + lastName;
        Long count = Stream.concat(
                trainers.stream().map(trainer -> trainer.getFirstName() + "." + trainer.getLastName()),
                trainees.stream().map(trainee -> trainee.getFirstName()+ "." +trainee.getLastName())
                )
                .filter(username -> baseUsername.equals(username))
                .count();

        if(count>0){
            return baseUsername + count; // Juan.Perez1
        }
        return baseUsername; // Juan.Perez
    }
}
