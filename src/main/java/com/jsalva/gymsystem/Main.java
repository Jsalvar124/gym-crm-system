package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM -------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");
        try {
            // trainer creation without login
//            gymFacade.createTrainer("Ana", "Gomez", TrainingTypeEnum.BOULDERING);
//            gymFacade.createTrainer("Juan", "Perez", TrainingTypeEnum.FUNCTIONAL);
            // Auto assign username for homonyms
//            gymFacade.createTrainer("Juan", "Perez", TrainingTypeEnum.BOXING);

            //Trainee creation whitout login
//            gymFacade.createTrainee("Sergio", "Hernandez", "CR 32 # 45S - 15", LocalDate.of(1990,12,12));
//            gymFacade.createTrainee("Juan", "Perez", "CR 45 # 25 - 20", LocalDate.of(1992,8,6));

            // Login
            gymFacade.login("Ana.Gomez","8kCNlowpaB");

//            gymFacade.createTraining(1L,5L, "Training for deletion Test", TrainingTypeEnum.ZUMBA, LocalDate.now(), 60);

            Trainee t = gymFacade.findTraineeByUsername("Juan.Perez2");

            Set<Trainer> trainerList = t.getTrainers();

            Trainer tr = gymFacade.getTrainerById(1L);

            System.out.println(tr.getTrainees());

            System.out.println(trainerList);

            gymFacade.deleteTrainer(1L);


//
//            gymFacade.createTrainee("Lucas", "Jhones", "Address 1", LocalDate.now());
//            gymFacade.createTrainee("Lucas", "Jhones", "Address 2", LocalDate.now());
//            gymFacade.createTrainee("Lucas", "Jhones", "Address 3", LocalDate.now());

//            gymFacade.createTrainer("Temistocles", "Hernandez", TrainingTypeEnum.PILATES);
//
//
//            gymFacade.login("Temistocles.Hernandez", "45wVfmgSoM");
//            gymFacade.createTraining(3L,8L, "Bouldering Advaned", TrainingTypeEnum.BOULDERING, LocalDate.now(), 180);
//
//            gymFacade.logout();
//
//            gymFacade.login("Pedro.Salva","newPassword");
//
//            Trainer trainer = gymFacade.findTrainerByUsername("Temistocles.Hernandez");
//
//            Trainee trainee = gymFacade.findTraineeByUsername("Julian.Salva");


//            Trainee trainee = gymFacade.findTraineeByUsername("Mariana.Cañas");
//
//            gymFacade.logout();
//
//            trainee = gymFacade.findTraineeByUsername("Mariana.Cañas");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            System.out.println("Gym System Closed!");
        }
    }
}