package com.jsalva.gynsystem.service;

import com.jsalva.gymsystem.config.AppConfig;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.service.TrainerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(AppConfig.class)
@DisplayName("Trainer Servcie Tests")
public class TrainerServiceTest {
    @Autowired
    private Map<String, Map<Long, Object>> commonStorage;

    @Autowired
    TrainerService trainerService;

    @Test
    @DisplayName("Trainer creation test")
    void testTrainerCreation() {
        Map<Long, Object> trainers = commonStorage.get("trainers");

        trainerService.createTrainer("Juan","Pérez", TrainingType.BOXING);
        assertEquals(6, trainers.size(), "Expected number of loaded trainers is 6.");

        trainerService.createTrainer("Hector","Pérez", TrainingType.PILATES);
        assertEquals(7, trainers.size(), "Expected number of loaded trainers is 7.");




    }
}
