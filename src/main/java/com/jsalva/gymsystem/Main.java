package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.dao.TrainerDAO;
import com.jsalva.gymsystem.dao.impl.TrainerDAOImpl;
import com.jsalva.gymsystem.facade.GymFacade;
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
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        GymFacade gymFacade = context.getBean(GymFacade.class);

        gymFacade.createTrainer("Juan", "Pérez", TrainingType.BOULDERING);
        gymFacade.createTrainer("Juan", "Pérez", TrainingType.BOXING);

        Trainer t1 = gymFacade.getTrainerById(16L);
        Trainer t2 = gymFacade.getTrainerById(17L);

        System.out.println(t1);
        // Error, not existing id.
        gymFacade.getTrainerById(100L);

        System.out.println(t2);

        gymFacade.updateTrainer(17L, "Juan", "Pulgarín", TrainingType.BOULDERING, null, null);

        System.out.println(t2);

    }
}