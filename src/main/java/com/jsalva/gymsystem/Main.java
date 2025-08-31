package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.facade.GymFacade;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        GymFacade gymFacade = context.getBean(GymFacade.class);

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM -------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");
        try {

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);

            String contextPath = "";
            String docBase = new File(".").getAbsolutePath();

            // This will automatically find and use your WebApplicationInitializer!
            tomcat.addWebapp(contextPath, docBase);

            tomcat.start();

            System.out.println("GYM - REST - API STARTED ON: http://localhost:8080" + contextPath + "/api/");
            tomcat.getServer().await();
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
//            gymFacade.login("Ana.Gomez","zflg5IzoEA");

            // Create a training after Login
//            gymFacade.createTraining(1L, 4L, "Bouldering Training", TrainingTypeEnum.BOULDERING, LocalDate.now(), 90);
//            gymFacade.createTraining(1L, 5L, "Bouldering Training", TrainingTypeEnum.BOULDERING, LocalDate.now(), 90);
//            gymFacade.createTraining(2L, 4L, "Bouldering Training", TrainingTypeEnum.BOULDERING, LocalDate.now(), 90);
//            gymFacade.createTraining(2L, 5L, "Bouldering Training", TrainingTypeEnum.BOULDERING, LocalDate.now(), 90);



            // Get and modify trainee infromation.
//
//            Trainer trainer = gymFacade.findTrainerByUsername("Ana.Gomez");
             // Set from Many to Many Relation.

//            Set<Trainee> traineeSet = gymFacade.getTraineeListForTrainer(trainer.getId());

//            System.out.println(traineeSet);

            // try to update someone else's password
//            gymFacade.updateTrainerPassword(2L, "changePassword");

            // Update owners password
//            gymFacade.updateTrainerPassword(1L,"newPassword1");

//            List<Trainer> list = gymFacade.getTrainerListByTraineeUsernameOrDateSpan("Juan.Perez2", null, null);

//            System.out.println(list);
            //delete trainee with cascade training deletion
//            gymFacade.deleteTraineeByUsername("Juan.Perez5");

//            gymFacade.logout();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            System.out.println("Gym System Closed!");
        }
    }
}