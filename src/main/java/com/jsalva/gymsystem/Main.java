package com.jsalva.gymsystem;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.repository.impl.TrainerRepositoryImpl;
import com.jsalva.gymsystem.repository.impl.TrainingTypeRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//        GymFacade gymFacade = context.getBean(GymFacade.class);


        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM - WITH DB ------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistenceUnit");
        EntityManager em = emf.createEntityManager();

        try {
            TrainingTypeRepositoryImpl trainingTypeRepository = new TrainingTypeRepositoryImpl(TrainingType.class, em);
            // Pupulate Training Type Table.
            for(TrainingTypeEnum tp : TrainingTypeEnum.values()){
                TrainingType trainingType = new TrainingType();
                trainingType.setTrainingTypeName(tp);
                trainingTypeRepository.create(trainingType);
            }

            // Add trainers
            TrainerRepositoryImpl trainerRepository = new TrainerRepositoryImpl(Trainer.class, em);

            Trainer trainer = new Trainer();
            TrainingType trainingType = trainingTypeRepository.findById(6L).get(); // BOULDERING MANAGED ENTITY
            trainer.setSpecialization(trainingType);
            trainer.setActive(true);
            trainer.setFirstName("Juan");
            trainer.setLastName("Perez");
            trainer.setUsername("Juan.Perez");
            trainer.setPassword("xxxxxxx");

            trainerRepository.create(trainer);

            Trainer trainer2 = new Trainer();
            TrainingType trainingType2 = trainingTypeRepository.findById(5L).get(); // BOULDERING MANAGED ENTITY
            trainer2.setSpecialization(trainingType2);
            trainer2.setActive(true);
            trainer2.setFirstName("Ana");
            trainer2.setLastName("Gomez");
            trainer2.setUsername("Ana.Gomez");
            trainer2.setPassword("yyyyyy");

            trainerRepository.create(trainer2);

            trainer2.setFirstName("Elvis");

            trainerRepository.update(trainer2);

//            trainerRepository.delete(1L);


            List<Trainer> trainers = trainerRepository.findAll();
            System.out.println(trainers.size());

            System.out.println(trainers);

            System.out.println(trainerRepository.validateCredentials("Juan.Perez", "xxxxxxx"));
            System.out.println(trainerRepository.validateCredentials("Ana.Gomez", "xxxxxxx"));
            System.out.println(trainerRepository.validateCredentials("Ana.Gomez", "yyyyyy"));






        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}