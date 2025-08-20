package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

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
            gymFacade.login("Ana.Gomez","ChangePassword");

            Trainee trainee = gymFacade.findTraineeByUsername("Sergio.Hernandez");
            gymFacade.toggleTraineeActiveState(trainee.getId());

            List<Trainer> trainerList = gymFacade.getAllTrainers();

            System.out.println(trainerList);

//            gymFacade.toggleTrainerActiveState(1L);

//            gymFacade.findTrainerByUsername("Ana.Gomez");


//            Trainee t = gymFacade.findTraineeByUsername("Juan.Perez2");

            List<Trainer> list = gymFacade.getTrainerListByTraineeUsernameOrDateSpan("Juan.Perez2", null, null);

            System.out.println(list);

            gymFacade.logout();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            System.out.println("Gym System Closed!");
        }
    }
}