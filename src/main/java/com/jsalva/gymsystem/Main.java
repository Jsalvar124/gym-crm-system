package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.dao.TrainerDAO;
import com.jsalva.gymsystem.dao.impl.TrainerDAOImpl;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.TrainingService;
import com.jsalva.gymsystem.service.impl.TrainerServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        Trainer trainer = new Trainer("Juan", "Pérez", "Juan.Pérez", "pass231", true, TrainingType.BOULDERING);
//        Trainer trainer1 = new Trainer("Jorge", "Cifuentes", "Jorge.Cifuentes", "pass123", true, TrainingType.BOXING);
//        System.out.println(trainer);
//        System.out.println(trainer1);
        // Create Spring application context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get the common storage bean
        Map<String, Map<Long, Object>> commonStorage = (Map<String, Map<Long, Object>>) context.getBean("commonStorage");

        // Get the trainers storage from common storage
        Map<Long, Object> trainersStorage = commonStorage.get("trainers");

        // Print results
        System.out.println("=== Testing Common Storage - Trainers ===");
        System.out.println("Number of trainers loaded: " + trainersStorage.size());
        System.out.println();

        // Print each trainer
//        trainersStorage.forEach((id, trainer) -> {
//            System.out.println("Trainer ID: " + id);
//            System.out.println("Trainer: " + trainer);
//            System.out.println("---");
//        });

        // Get the trainees storage from common storage
        Map<Long, Object> traineesStorage = commonStorage.get("trainees");

        // Print results
        System.out.println("=== Testing Common Storage - Trainees ===");
        System.out.println("Number of trainees loaded: " + traineesStorage.size());
        System.out.println();

        // Print each trainee
//        traineesStorage.forEach((id, trainee) -> {
//            System.out.println("Trainee ID: " + id);
//            System.out.println("Trainee: " + trainee);
//            System.out.println("---");
//        });

        // Get the trainings storage from common storage
        Map<Long, Object> trainingStorage = commonStorage.get("trainings");

        // Print results
        System.out.println("=== Testing Common Storage - Trainings ===");
        System.out.println("Number of trainings loaded: " + trainingStorage.size());
        System.out.println();

        // Print each training
//        trainingStorage.forEach((id, training) -> {
//            System.out.println("Training ID: " + id);
//            System.out.println("Training: " + training);
//            System.out.println("---");
//        });


        TrainerDAO trainerDAO = context.getBean(TrainerDAOImpl.class); // if you use constructor, Spring does not take control of that instance.
        Map<Long, Object> trainersMap = trainerDAO.getTrainers();

        // is it the same instance?
        System.out.println("SAME INSTANCE: ");
        System.out.println(trainersMap == trainersStorage);

        TrainerService trainerService = context.getBean(TrainerService.class); // Get from context, otherwise, dependencies won't be injected!
        trainerService.createTrainer("Juan","Pérez", TrainingType.BOXING);
        trainerService.createTrainer("Hector","Pérez", TrainingType.PILATES);


        // Update Trainer 17
        trainerService.updateTrainer(17L, null, null, null, "NewPassword", null);

        trainerService.updateTrainer(17L, "David", "Brown", TrainingType.ZUMBA, null, null);

        trainerService.deleteTrainer(17L);

        TraineeService traineeService = context.getBean(TraineeService.class);

        List<Trainee> traineeList = traineeService.getAllTrainees();

        for (Trainee trnee : traineeList){
            System.out.println(trnee);
        }

        traineeService.createTrainee("Carlos", "Ramos", "CR 43 # 56-14", LocalDate.of(1990, 5, 27));
        traineeService.createTrainee("Carlos", "Ramirez", "CL 52 # 12-22", LocalDate.of(1990, 5, 27));

        traineeService.updateTrainee(19L, "José", null, null, false, null, null);

        traineeService.deleteTrainee(19L);

        TrainingService trainingService = context.getBean(TrainingService.class);

        List<Training> trainingList = trainingService.getAllTrainings();

        System.out.println(trainingList.getFirst());

        trainingService.createTraining(1L,8L,"Leg Day Killer Workout", TrainingType.FUNCTIONAL, LocalDate.now(), 40);

    }
}