package com.jsalva.gynsystem.model;

import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrainerTest {
    @Test
    void testTrainerCreation() {
        Trainer trainer = new Trainer("Juan", "Pérez", "Juan.Pérez", "pass231", true, TrainingType.BOULDERING);

        assertEquals("Juan", trainer.getFirstName());
        assertEquals("Pérez", trainer.getLastName());
        assertEquals(trainer.getFirstName()+"."+trainer.getLastName(), trainer.getUsername());
        assertEquals(TrainingType.BOULDERING, trainer.getSpecialization());
        assertNotNull(trainer.getUserId()); // Auto-generated
        assertTrue(trainer.isActive()); // Default value
    }

    @Test
    void testUserIdGeneration() {
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        Trainer trainer3 = new Trainer();

        assertNotEquals(trainer1.getUserId(), trainer2.getUserId());
        assertTrue(trainer2.getUserId() == trainer1.getUserId() + 1);
        assertTrue(trainer3.getUserId() == trainer2.getUserId() + 1);

    }

    @Test
    void loadTrainersFromCSV(){

    }
}

