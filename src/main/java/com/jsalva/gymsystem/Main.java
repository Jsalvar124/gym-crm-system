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
//            gymFacade.createTrainer("Juan", "Jorge", TrainingTypeEnum.ZUMBA);

            Trainee trainee = gymFacade.findTraineeByUsername("Mariana.Cañas");

            System.out.println("Does this happen?");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("Finish!");
        }
    }
}