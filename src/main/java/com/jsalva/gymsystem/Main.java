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

            // Try to create a training without login
//            gymFacade.createTraining(1L, 4L, "Bouldering Training", TrainingTypeEnum.BOULDERING, LocalDate.now(), 90);

            // Login
//            gymFacade.login("Ana.Gomez","1jJlQ2300e");

            // Create a training after Login
//             gymFacade.createTraining(1L, 4L, "Bouldering Training", TrainingTypeEnum.BOULDERING, LocalDate.now(), 90);

            // Get and modify trainee infromation.
//            Trainee trainee = gymFacade.findTraineeByUsername("Sergio.Hernandez");
//            gymFacade.toggleTraineeActiveState(trainee.getId());
//
             // Set from Many to Many Relation.
//            Set<Trainer> trainerSet = trainee.getTrainers();
//            System.out.println(trainerSet);

            // try to update someone else's password
//            gymFacade.updateTrainerPassword(2L, "changePassword");

            // Update owners password
//            gymFacade.updateTrainerPassword(1L,"newPassword");

//            List<Trainer> list = gymFacade.getTrainerListByTraineeUsernameOrDateSpan("Juan.Perez2", null, null);

            //delete trainee with cascade training deletion
//            gymFacade.deleteTrainee(trainee.getId());

//            gymFacade.logout();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            System.out.println("Gym System Closed!");
        }
    }
}