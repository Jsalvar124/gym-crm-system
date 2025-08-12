package com.jsalva.gymsystem;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.repository.impl.TraineeRepositoryImpl;
import com.jsalva.gymsystem.repository.impl.TrainerRepositoryImpl;
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


        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            em.close();
        }
    }
}