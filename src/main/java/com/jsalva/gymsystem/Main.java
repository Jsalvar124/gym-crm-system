package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        GymFacade gymFacade = context.getBean(GymFacade.class);

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM ------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");

        // Trainers
        gymFacade.createTrainer("Juan", "Pérez", TrainingType.BOULDERING);
        gymFacade.createTrainer("Juan", "Pérez", TrainingType.BOXING);

        // Initial preloaded data goes up to id 15, 5 trainers, 10 trainees.
        Trainer t1 = gymFacade.getTrainerById(16L);
        Trainer t2 = gymFacade.getTrainerById(17L);

        System.out.println(t1);
        // Error, not existing id.
        gymFacade.getTrainerById(100L);

        System.out.println(t2);

        gymFacade.updateTrainer(17L, "Juan", "Pulgarín", TrainingType.BOULDERING, null, null);

        // Print updated trainer
        System.out.println(t2);

        gymFacade.deleteTrainer(17L);

        List<Trainer> trainerList = gymFacade.getAllTrainers();

        System.out.println(trainerList);

        // Error, trainer deleted
        gymFacade.getTrainerById(17L);

        // Trainees

        List<Trainee> traineeList = gymFacade.getAllTrainees();

        gymFacade.createTrainee("Laura", "Gómez", "St 45-23", LocalDate.of(1990, 1,15));

        // Trainings

        List<Training> trainingList = gymFacade.getAllTrainings();

        // Error, Trainer 17 no longer exists.
        gymFacade.createTraining(17L, 18L, "Java Training", TrainingType.FUNCTIONAL, LocalDate.now(), 120);

        gymFacade.createTraining(16L, 18L, "Java Training", TrainingType.FUNCTIONAL, LocalDate.now(), 120);

        Training training = gymFacade.getTrainingById(21L);

        System.out.println(training);


    }
}