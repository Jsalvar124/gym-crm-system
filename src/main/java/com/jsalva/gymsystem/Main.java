package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.dao.TrainerDAO;
import com.jsalva.gymsystem.dao.impl.TrainerDAOImpl;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.impl.TrainerServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        Trainer trainer = new Trainer("Juan", "Pérez", "Juan.Pérez", "pass231", true, TrainingType.BOULDERING);
//        Trainer trainer1 = new Trainer("Jorge", "Cifuentes", "Jorge.Cifuentes", "pass123", true, TrainingType.BOXING);
//        System.out.println(trainer);
//        System.out.println(trainer1);
        // Create Spring application context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get the common storage bean
        Map<String, Map<Long, Object>> commonStorage = (Map<String, Map<Long, Object>>) context.getBean("commonStorage");

        // Get the trainers storage from common storage
        Map<Long, Object> trainersStorage = commonStorage.get("trainers");

        // Print results
        System.out.println("=== Testing Common Storage - Trainers ===");
        System.out.println("Number of trainers loaded: " + trainersStorage.size());
        System.out.println();

        // Print each trainer
//        trainersStorage.forEach((id, trainer) -> {
//            System.out.println("Trainer ID: " + id);
//            System.out.println("Trainer: " + trainer);
//            System.out.println("---");
//        });

        // Get the trainees storage from common storage
        Map<Long, Object> traineesStorage = commonStorage.get("trainees");

        // Print results
        System.out.println("=== Testing Common Storage - Trainees ===");
        System.out.println("Number of trainees loaded: " + traineesStorage.size());
        System.out.println();

        // Print each trainee
//        traineesStorage.forEach((id, trainee) -> {
//            System.out.println("Trainee ID: " + id);
//            System.out.println("Trainee: " + trainee);
//            System.out.println("---");
//        });

        // Get the trainings storage from common storage
        Map<Long, Object> trainingStorage = commonStorage.get("trainings");

        // Print results
        System.out.println("=== Testing Common Storage - Trainings ===");
        System.out.println("Number of trainings loaded: " + trainingStorage.size());
        System.out.println();

        // Print each training
//        trainingStorage.forEach((id, training) -> {
//            System.out.println("Training ID: " + id);
//            System.out.println("Training: " + training);
//            System.out.println("---");
//        });


        TrainerDAO trainerDAO = context.getBean(TrainerDAOImpl.class); // if you use constructor, Spring does not take control of that instance.
        Map<Long, Object> trainersMap = trainerDAO.getTrainers();

        // is it the same instance?
        System.out.println("SAME INSTANCE: ");
        System.out.println(trainersMap == trainersStorage);

        Trainer trainer2 = new Trainer();
        trainer2.setFirstName("Juan");
        trainer2.setLastName("Pérez");
        trainer2.setSpecialization(TrainingType.ZUMBA);

        Trainer trainer3 = new Trainer();
        trainer3.setFirstName("Juan");
        trainer3.setLastName("Pérez");
        trainer3.setSpecialization(TrainingType.BOXING);

        TrainerService trainerService = context.getBean(TrainerService.class); // Get from context, otherwise, dependencies won't be injected!
        Trainer trainerFromService = trainerService.createTrainer(trainer2);
        Trainer trainerFromService2 = trainerService.createTrainer(trainer3);

        // Print each trainer
        trainersStorage.forEach((id, tr) -> {
            System.out.println("Trainer ID: " + id);
            System.out.println("Trainer: " + tr);
            System.out.println("---");
        });

    }
}