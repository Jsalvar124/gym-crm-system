package com.jsalva.gymsystem.utils;

import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Stream;


public class UserUtils {

    public static String generateRandomPassword(){
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
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
