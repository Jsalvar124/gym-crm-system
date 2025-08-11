package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//        GymFacade gymFacade = context.getBean(GymFacade.class);


        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM - WITH DB ------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnit");
        // Trainers
    }
}