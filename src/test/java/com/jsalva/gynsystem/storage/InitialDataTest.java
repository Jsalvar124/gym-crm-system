package com.jsalva.gynsystem.storage;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.storage.TrainerStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfig.class)
@DisplayName("Initial Data Load Tests")
public class InitialDataTest {

    @Autowired
    private Map<String, Map<Long, Object>> commonStorage;

    @Test
    @DisplayName("Trainer File Tests")
    void testTrainerDataIsLoadedCorrectly() {
        Map<Long, Object> trainers = commonStorage.get("trainers");

        assertNotNull(trainers, "Trainers map should not be null");
        assertFalse(trainers.isEmpty(), "Trainers map should not be empty");
        assertEquals(5, trainers.size(), "Expected number of loaded trainers is 5.");

        // Maria,Garcia,maria.garcia,xyz9876543,true,ZUMBA
        boolean mariaFound = trainers.values().stream()
                .filter(Trainer.class::isInstance)
                .map(Trainer.class::cast)
                .anyMatch(trainer ->
                        trainer.getFirstName().equals("Maria") &&
                                trainer.getLastName().equals("Garcia") &&
                                trainer.getUsername().equals("Maria.Garcia") &&
                                trainer.getSpecialization() == TrainingType.ZUMBA
                );


        assertTrue(mariaFound, "Expected trainer 'Maria Garcia' with ZUMBA specialization");
    }

    @Test
    @DisplayName("Trainee File Tests")
    void testTraineeDataIsLoadedCorrectly() {
        // Get the trainers storage from common storage
        Map<Long, Object> trainees = commonStorage.get("trainees");

        assertNotNull(trainees, "Trainees map should not be null");
        assertFalse(trainees.isEmpty(), "Trainees map should not be empty");
        assertEquals(10, trainees.size(), "Expected number of loaded trainers is 10.");

        // Felipe,Naranjo,Felipe.Naranjo,AmR3n8YtKx,true,Calle 80 #10-10,2003-04-20
        boolean felipeFound = trainees.values().stream()
                .filter(Trainee.class::isInstance)
                .map(Trainee.class::cast)
                .anyMatch(trainee ->
                        trainee.getFirstName().equals("Felipe") &&
                                trainee.getLastName().equals("Naranjo") &&
                                trainee.getUsername().equals("Felipe.Naranjo") &&
                                trainee.getAddress().equals("Calle 80 #10-10") &&
                                trainee.getDateOfBirth().equals(LocalDate.of(2003, 4, 20))
                );
        assertTrue(felipeFound, "Expected trainer 'Felipe Naranjo'");
    }

    @Test
    @DisplayName("Training File Tests")
    void testTrainingDataIsLoadedCorrectly() {
        // Get the trainings storage from common storage
        Map<Long, Object> trainings = commonStorage.get("trainings");

        assertNotNull(trainings, "Trainings map should not be null");
        assertFalse(trainings.isEmpty(), "Trainings map should not be empty");
        assertEquals(20, trainings.size(), "Expected number of loaded trainings is 20.");

        // 1,6,CrossFit Morning Blast,CROSSFIT,2025-07-01,45

        boolean crossfitTrainingFound = trainings.values().stream()
                .filter(Training.class::isInstance)
                .map(Training.class::cast)
                .anyMatch(training ->
                        training.getTrainerId() == 1 &&
                                training.getTraineeId() == 6 &&
                                training.getTrainingName().equals("CrossFit Morning Blast") &&
                                training.getTrainingType() == TrainingType.CROSSFIT &&
                                training.getTrainingDate()
                                        .equals(LocalDate.of(2025, 7, 1)) &&
                                training.getDuration() == 45
                );
        assertTrue(crossfitTrainingFound, "Expected Training 'CrossFit Morning Blast' on 2025/07/01 for 45 min, Trainer 1, Trainee 6.");
    }

}
