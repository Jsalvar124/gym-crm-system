package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM -------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");
        try {
//            gymFacade.createTrainer("Lucas", "Jhones", TrainingTypeEnum.FUNCTIONAL);
//            gymFacade.createTrainer("Lucas", "Jhones", TrainingTypeEnum.BOULDERING);
//            gymFacade.createTrainer("Lucas", "Jhones", TrainingTypeEnum.ZUMBA);
//
//            gymFacade.createTrainee("Lucas", "Jhones", "Address 1", LocalDate.now());
//            gymFacade.createTrainee("Lucas", "Jhones", "Address 2", LocalDate.now());
//            gymFacade.createTrainee("Lucas", "Jhones", "Address 3", LocalDate.now());

            gymFacade.createTrainer("Temistocles", "Hernandez", TrainingTypeEnum.PILATES);


            gymFacade.login("Temistocles.Hernandez", "45wVfmgSoM");
            gymFacade.createTraining(1L,6L, "Bouldering Advaned", TrainingTypeEnum.BOULDERING, LocalDate.now(), 180);

            gymFacade.logout();

            gymFacade.login("Pedro.Salva","newPassword");

            Trainer trainer = gymFacade.findTrainerByUsername("Temistocles.Hernandez");

            Trainee trainee = gymFacade.findTraineeByUsername("Julian.Salva");




//            Trainee trainee = gymFacade.findTraineeByUsername("Mariana.Cañas");
//
//            gymFacade.logout();
//
//            trainee = gymFacade.findTraineeByUsername("Mariana.Cañas");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("Finish!");
        }
    }
}