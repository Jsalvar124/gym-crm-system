package com.jsalva.gymsystem;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        GymFacade gymFacade = context.getBean(GymFacade.class);

        gymFacade.createTrainer("Juan", "Pérez", TrainingType.BOULDERING);
        gymFacade.createTrainer("Juan", "Pérez", TrainingType.BOXING);

        // Initial preloaded data goes up to id 15, 5 trainers, 10 trainees.
        Trainer t1 = gymFacade.getTrainerById(16L);
        Trainer t2 = gymFacade.getTrainerById(17L);

        System.out.println(t1);
        // Error, not existing id.
        gymFacade.getTrainerById(100L);

        System.out.println(t2);

        gymFacade.updateTrainer(17L, "Juan", "Pulgarín", TrainingType.BOULDERING, null, null);

        // Print updated trainer
        System.out.println(t2);



    }
}