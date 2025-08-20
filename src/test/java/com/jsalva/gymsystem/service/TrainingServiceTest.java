package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.repository.TrainingRepository;
import com.jsalva.gymsystem.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    // Dependencies
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingTypeService trainingTypeService;

    // Actual Class being tested
    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    @DisplayName("Create training successfully when trainer and trainee exist")
    void testCreateTrainingSuccessfully() {

        Long trainerId = 1L;
        Long traineeId = 2L;

        Trainer trainer = new Trainer();
        trainer.setId(trainerId);

        Trainee trainee = new Trainee();

        trainee.setId(traineeId);

        TrainingType trainingTypeMock = new TrainingType();
        trainingTypeMock.setTrainingTypeName(TrainingTypeEnum.BOXING);

        when(trainerService.getTrainerById(trainerId)).thenReturn(trainer);
        when(traineeService.getTraineeById(traineeId)).thenReturn(trainee);
        when(trainingTypeService.findTrainingTypeByName(TrainingTypeEnum.BOXING)).thenReturn(trainingTypeMock);

        String trainingName = "Morning Boxing";
        LocalDate date = LocalDate.of(2025, 7, 29);
        TrainingTypeEnum type = TrainingTypeEnum.BOXING;
        int duration = 60;

        // Call
        trainingService.createTraining(trainerId, traineeId, trainingName, type, date, duration);

        // Then - verify that Repository was called with the correct object
        verify(trainingRepository).create(argThat(training ->
                training.getTrainer().equals(trainer) &&
                        training.getTrainee().equals(trainee) &&
                        training.getTrainingName().equals(trainingName) &&
                        training.getTrainingType().equals(trainingTypeMock) &&
                        training.getTrainingDate().equals(date) &&
                        training.getDuration()==duration
        ));
    }

    @Test
    void testCreateTrainingWithInvalidTrainerId() {
        Long trainerId = 999L;
        Long traineeId = 2L;


        Trainee trainee = new Trainee();

        trainee.setId(traineeId);

        TrainingType trainingTypeMock = new TrainingType();
        trainingTypeMock.setTrainingTypeName(TrainingTypeEnum.BOXING);

        when(trainerService.getTrainerById(trainerId)).thenReturn(null);

        String trainingName = "Morning Boxing";
        LocalDate date = LocalDate.of(2025, 7, 29);
        TrainingTypeEnum type = TrainingTypeEnum.BOXING;
        int duration = 60;



        // Then - verify that Repository was called with the correct object
        Exception ex = assertThrows(IllegalArgumentException.class, ()->{
            // Call
            trainingService.createTraining(trainerId, traineeId, trainingName, type, date, duration);
        });

        assertEquals("Trainer with Id 999 not found.", ex.getMessage());

        // Verify that only trainerService was called
        verify(trainerService).getTrainerById(trainerId);
        verify(traineeService, never()).getTraineeById(any());
        verify(trainingTypeService, never()).findTrainingTypeByName(any());
    }

    @Test
    void testGetTrainingById(){
        // Arrange
        Long trainingId = 100L;

        // Create trainer and trainee objects
        Trainer trainer = new Trainer();
        trainer.setId(1L);

        Trainee trainee = new Trainee();
        trainee.setId(2L);

        // Create training type object
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeEnum.PILATES);

        // Use Builder pattern to create Training
        Training training = new Training.Builder()
                .setTrainer(trainer)
                .setTrainee(trainee)
                .setTrainingName("Strength Basics")
                .setTrainingType(trainingType)
                .setTrainingDate(LocalDate.of(2025, 8, 10))
                .setDuration(45)
                .build();

        // Set the ID (usually done by database)
        training.setId(trainingId);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        // Act
        Training result = trainingService.getTrainingById(trainingId);

        // Assert
        assertNotNull(result);
        assertEquals(trainingId, result.getId());
        assertEquals("Strength Basics", result.getTrainingName());
        assertEquals(trainingType, result.getTrainingType());
        assertEquals(LocalDate.of(2025, 8, 10), result.getTrainingDate());
        assertEquals(45, result.getDuration());
        assertEquals(trainer, result.getTrainer());
        assertEquals(trainee, result.getTrainee());

        verify(trainingRepository).findById(trainingId);
    }

}
