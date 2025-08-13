package com.jsalva.gymsystem;

import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.repository.impl.TraineeRepositoryImpl;
import com.jsalva.gymsystem.repository.impl.TrainerRepositoryImpl;
import com.jsalva.gymsystem.repository.impl.TrainingRepositoryImpl;
import com.jsalva.gymsystem.repository.impl.TrainingTypeRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
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
//            // Populate Training Type
//            Table.
//            for(TrainingTypeEnum tp : TrainingTypeEnum.values()){
//                TrainingType trainingType = new TrainingType();
//                trainingType.setTrainingTypeName(tp);
//                trainingTypeRepository.create(trainingType);
//            }

            // Populate Trainers Table.
            TrainerRepositoryImpl trainerRepository = new TrainerRepositoryImpl(Trainer.class, em);

//            Trainer trainer = new Trainer();
//            TrainingType trainingType = trainingTypeRepository.findById(4L).get(); // BOULDERING MANAGED ENTITY
//            trainer.setSpecialization(trainingType);
//            trainer.setActive(true);
//            trainer.setFirstName("Elena");
//            trainer.setLastName("Vega");
//            trainer.setUsername("Elena.Vega");
//            trainer.setPassword("oooooo");
//
//            trainerRepository.create(trainer);
//

//            Trainer trainer = trainerRepository.findByUsername("Juan.Perez").get();
//            TrainingType trainingType = trainingTypeRepository.findById(2L).get(); // BOULDERING MANAGED ENTITY
//
//            trainer.setSpecialization(trainingType);
//
//            trainerRepository.update(trainer);


            // Populate Trainees Table.
            TraineeRepositoryImpl traineeRepository = new TraineeRepositoryImpl(Trainee.class, em);
//
//            Trainee trainee = new Trainee();
//            trainee.setAddress("21 Strasse, Berlin");
//            trainee.setDateOfBirth(LocalDate.of(1854,2,15));
//            trainee.setActive(true);
//            trainee.setFirstName("Gustav");
//            trainee.setLastName("Mahler");
//            trainee.setUsername("Gustav.Mahler");
//            trainee.setPassword("eeeeee");
//
//            traineeRepository.create(trainee);

            // Populate trainings
            TrainingRepositoryImpl trainingRepository = new TrainingRepositoryImpl(Training.class, em);

//            Training training = new Training();
//            Trainee trainee = traineeRepository.findByUsername("Julian.Salva").get();
//            Trainer trainer = trainerRepository.findByUsername("Juan.Perez").get();
//
//            training.setTrainee(trainee);
//            training.setTrainer(trainer);
//            training.setTrainingDate(LocalDate.of(2025, 8,14));
//            training.setTrainingName("Advanced Functional");
//            training.setDuration(90);
//            TrainingType trainingType = trainingTypeRepository.findById(3L).get();
//            training.setTrainingType(trainingType);
//
//            trainingRepository.create(training);

            List<Trainer> trainerList = traineeRepository.findUnassignedTrainersByTrainee("Julian.Salva");
            System.out.println(trainerList);
            trainerList = traineeRepository.findUnassignedTrainersByTrainee("Chacho.Apá");
            System.out.println(trainerList);




        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            em.close();
        }
    }
}