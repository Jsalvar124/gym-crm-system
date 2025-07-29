package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dao.TrainingDAO;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    // Dependencies
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    // Actual Class being tested
    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    @DisplayName("Create training successfully when trainer and trainee exist")
    void testCreateTrainingSuccessfully() {

        Long trainerId = 1L;
        Long traineeId = 2L;

        Trainer trainer = new Trainer(
                "Carlos", "Lopez", "carlos123", "pass123", true, TrainingType.BOXING
        );
        trainer.setUserId(trainerId);

        Trainee trainee = new Trainee(
                "Ana", "Diaz", "ana456", "pass456", true, "Street 123", LocalDate.of(2000, 5, 10)
        );

        trainee.setUserId(traineeId);

        when(trainerService.getTrainerById(trainerId)).thenReturn(trainer);
        when(traineeService.getTraineeById(traineeId)).thenReturn(trainee);

        String trainingName = "Morning Boxing";
        TrainingType type = TrainingType.BOXING;
        LocalDate date = LocalDate.of(2025, 7, 29);
        int duration = 60;

        // When
        trainingService.createTraining(trainerId, traineeId, trainingName, type, date, duration);

        // Then - verify that DAO was called with the correct object
        verify(trainingDAO).save(argThat(training ->
                training.getTrainerId().equals(trainerId) &&
                        training.getTraineeId().equals(traineeId) &&
                        training.getTrainingName().equals(trainingName) &&
                        training.getTrainingType().equals(type) &&
                        training.getTrainingDate().equals(date) &&
                        training.getDuration()==duration
        ));
    }

    @Test
    void testCreateTrainingWithInvalidTrainerId() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer(
                "Carlos", "Lopez", "carlos123", "pass123", true, TrainingType.BOXING
        );

        Long traineeId = 2L;

        when(trainerService.getTrainerById(trainerId)).thenReturn(trainer); // Return actual trainer
        when(traineeService.getTraineeById(traineeId)).thenReturn(null); // Simulate triner not found

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trainingService.createTraining(trainerId, traineeId, "trainingName", TrainingType.BOXING, LocalDate.now(), 50)
        );

        assertEquals("Trainee with Id 2 not found.", exception.getMessage());
    }

    @Test
    void testGetTrainingById(){
        // Arrange
        Long trainingId = 100L;
        Training training = new Training();
        training.setTrainingId(trainingId);
        training.setTrainerId(1L);
        training.setTraineeId(2L);
        training.setTrainingName("Strength Basics");
        training.setTrainingType(TrainingType.PILATES);
        training.setTrainingDate(LocalDate.of(2025, 8, 10));
        training.setDuration(45);

        when(trainingDAO.findById(trainingId)).thenReturn(training);

        // Act
        Training result = trainingService.getTrainingById(trainingId);

        // Assert
        assertNotNull(result);
        assertEquals(trainingId, result.getTrainingId());
        assertEquals("Strength Basics", result.getTrainingName());
        assertEquals(TrainingType.PILATES, result.getTrainingType());
        assertEquals(45, result.getDuration());
        verify(trainingDAO).findById(trainingId);
    }

}
