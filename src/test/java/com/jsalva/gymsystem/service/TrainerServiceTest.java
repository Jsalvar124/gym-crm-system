package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfig.class)
@DisplayName("Trainer Servcie Tests")
public class TrainerServiceTest {
    @Autowired
    private Map<String, Map<Long, Object>> commonStorage;

    @Autowired
    TrainerService trainerService;
//
    private Map<Long, Object> trainers;

    @BeforeEach
    void setUp() {
        trainers = commonStorage.get("trainers");
        trainers.clear(); // Reset the state before each test, start with 0 trainers.
        User.setIdCount(1L); // reset ID static count
    }

    @Test
    @DisplayName("Trainer creation test")
    void testTrainerCreation() {
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);
        assertEquals(1, trainers.size(), "Expected number of loaded trainers is 1.");

        trainerService.createTrainer("Hector", "Pérez", TrainingType.PILATES);
        assertEquals(2, trainers.size(), "Expected number of loaded trainers is 2.");


        boolean juanPerezFound = trainers.values().stream()
                .filter(Trainer.class::isInstance)
                .map(Trainer.class::cast)
                .anyMatch(trainer ->
                        trainer.getFirstName().equals("Juan") &&
                                trainer.getLastName().equals("Pérez") &&
                                trainer.getUsername().equals("Juan.Pérez") &&
                                trainer.getSpecialization() == TrainingType.BOXING
                );

        assertTrue(juanPerezFound, "Expected trainer 'Juan Pérez'");
    }

    @Test
    @DisplayName("Trainer find by id service")
    void testTrainerGetTrainerByIdService() {
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);
        assertEquals(1, trainers.size(), "Expected number of loaded trainers is 1.");

        trainerService.createTrainer("Hector", "Pérez", TrainingType.PILATES);
        assertEquals(2, trainers.size(), "Expected number of loaded trainers is 2.");

        Trainer t1 = trainerService.getTrainerById(1L);
        Trainer t2 = trainerService.getTrainerById(2L);

        assertEquals(1L, t1.getUserId());
        assertEquals("Juan", t1.getFirstName());
        assertEquals("Hector.Pérez", t2.getUsername());
    }

    @Test
    @DisplayName("Trainer find by id service with invalid id")
    void testInvalidIdOnTrainerGetTrainerByIdService() {
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);
        assertEquals(1, trainers.size(), "Expected number of loaded trainers is 1.");

        Exception ex = assertThrows(IllegalArgumentException.class, ()->{
            trainerService.getTrainerById(1000L);
        } );

        assertEquals("Trainer with Id 1000 not found.", ex.getMessage());


        ex = assertThrows(IllegalArgumentException.class, ()->{
            trainerService.getTrainerById(null);
        } );

        assertEquals("Trainer ID cannot be null", ex.getMessage());
    }

    @Test
    @DisplayName("Trainer username with homonym test")
    void testUserNameWithHomonymCreation() {
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);
        trainerService.createTrainer("Juan", "Pérez", TrainingType.ZUMBA);
        trainerService.createTrainer("Juan", "Pérez", TrainingType.PILATES);

        Trainer t1 = trainerService.getTrainerById(1L);
        Trainer t2 = trainerService.getTrainerById(2L);
        Trainer t3 = trainerService.getTrainerById(3L);

        assertEquals("Juan.Pérez", t1.getUsername());
        assertEquals("Juan.Pérez1", t2.getUsername());
        assertEquals("Juan.Pérez2", t3.getUsername());
    }


    @Test
    @DisplayName("Trainer password generation test")
    void testRandomPasswordGeneration() {
        assertEquals(0, trainers.size(), "Expected number of loaded trainers is 0.");
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);
        trainerService.createTrainer("Juan", "Pérez", TrainingType.ZUMBA);
        trainerService.createTrainer("Juan", "Pérez", TrainingType.PILATES);

        Trainer t1 = trainerService.getTrainerById(1L);
        Trainer t2 = trainerService.getTrainerById(2L);
        Trainer t3 = trainerService.getTrainerById(3L);

        // Password has 10 characters
        assertEquals(10, t1.getPassword().length());

        // Password is different for each user.
        assertNotEquals(t1.getPassword(), t2.getPassword());
        assertNotEquals(t1.getPassword(), t3.getPassword());
        assertNotEquals(t2.getPassword(), t3.getPassword());

        // characters are random from 1-9 and a-zA-Z
        String password = t1.getPassword();
        assertTrue(password.matches("[a-zA-Z0-9]{10}"), "Password must contain only a-z, A-Z, 0-9 and be 10 characters long");
    }

    @Test
    @DisplayName("Trainer update service test")
    void testTrainerUpdateService() {
        //Start with 0 trainers
        assertEquals(0, trainers.size(), "Expected number of loaded trainers is 0.");

        //Add 1
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);

        //Update trainer
        trainerService.updateTrainer(1L, "Edgar", null, TrainingType.PILATES, "ChangedPassword", false);

        // Retrieve updated Trainer
        Trainer updatedTrainer = trainerService.getTrainerById(1L);


        assertEquals(1L, updatedTrainer.getUserId());
        assertEquals("Edgar", updatedTrainer.getFirstName());
        assertEquals("Pérez", updatedTrainer.getLastName());
        assertEquals(TrainingType.PILATES, updatedTrainer.getSpecialization());
        assertEquals("ChangedPassword", updatedTrainer.getPassword());
        assertEquals(false, updatedTrainer.isActive());
    }

    @Test
    @DisplayName("Trainer update service test with invalid id")
    void testInvalidIdOnTrainerUpdateService() {
        //Start with 0 trainers
        assertEquals(0, trainers.size(), "Expected number of loaded trainers is 0.");

        //Add 1
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);

        //Update trainer
        Exception ex = assertThrows(IllegalArgumentException.class, ()->{
            trainerService.updateTrainer(1000L, "Edgar", null, TrainingType.PILATES, "ChangedPassword", false);
        } );

        assertEquals("Trainer with Id 1000 not found.", ex.getMessage());


        ex = assertThrows(IllegalArgumentException.class, ()->{
            trainerService.updateTrainer(null, "Edgar", null, TrainingType.PILATES, "ChangedPassword", false);
        } );

        assertEquals("Trainer ID cannot be null", ex.getMessage());

    }


    @Test
    @DisplayName("Trainer delete service test")
    void testTrainerDeleteService() {
        //Start with 0 trainers
        assertEquals(0, trainers.size(), "Expected number of loaded trainers is 0.");

        //Add 1
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);

        assertEquals(1, trainers.size(), "Expected number of loaded trainers is 1.");

        trainerService.deleteTrainer(1L);

        assertEquals(0, trainers.size(), "Expected number of loaded trainers is 0.");
    }

    @Test
    @DisplayName("Trainer delete service test with invalid id")
    void testInvalidIdOnTrainerDeleteService() {
        //Start with 0 trainers
        assertEquals(0, trainers.size(), "Expected number of loaded trainers is 0.");

        //Add 1
        trainerService.createTrainer("Juan", "Pérez", TrainingType.BOXING);

        //Delete non existent trainer
        Exception ex = assertThrows(IllegalArgumentException.class, ()->{
            trainerService.deleteTrainer(1000L);
        } );

        assertEquals("Trainer with Id 1000 not found.", ex.getMessage());


        ex = assertThrows(IllegalArgumentException.class, ()->{
            trainerService.deleteTrainer(null);
        } );

        assertEquals("Trainer ID cannot be null", ex.getMessage());

    }
}
