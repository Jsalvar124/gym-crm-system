package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.exception.ResourceNotFoundException;
import com.jsalva.gymsystem.exception.UnprocessableEntityException;
import com.jsalva.gymsystem.repository.TrainingRepository;
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

    // Actual Class being tested
    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    @DisplayName("Create training successfully when trainer and trainee exist and are active")
    void testCreateTrainingSuccessfully() {
        // Given
        String trainerUsername = "Juan.Perez";
        String traineeUsername = "Ana.Gomez";
        String trainingName = "Morning Boxing";
        LocalDate trainingDate = LocalDate.of(2025, 7, 29);
        int duration = 60;

        // Create training type
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeEnum.BOXING);

        // Create active trainer with specialization
        Trainer trainer = new Trainer();
        trainer.setUsername(trainerUsername);
        trainer.setActive(true);
        trainer.setSpecialization(trainingType); // This is crucial!

        // Create active trainee
        Trainee trainee = new Trainee();
        trainee.setUsername(traineeUsername);
        trainee.setActive(true);

        // Create request DTO
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto(
                traineeUsername,
                trainerUsername,
                trainingName,
                trainingDate,
                duration
        );

        // Mock service calls
        when(trainerService.findEntityByUsername(trainerUsername)).thenReturn(trainer);
        when(traineeService.findEntityByUsername(traineeUsername)).thenReturn(trainee);

        // When
        trainingService.createTraining(requestDto);

        // Then
        verify(trainerService).findEntityByUsername(trainerUsername);
        verify(traineeService).findEntityByUsername(traineeUsername);

        verify(trainingRepository).create(argThat(training ->
                training.getTrainer().equals(trainer) &&
                        training.getTrainee().equals(trainee) &&
                        training.getTrainingName().equals(trainingName) &&
                        training.getTrainingType().equals(trainingType) &&
                        training.getTrainingDate().equals(trainingDate) &&
                        training.getDuration() == duration
        ));
    }

    @Test
    @DisplayName("Should throw exception when trainer is inactive")
    void testCreateTraining_InactiveTrainer() {
        // Given
        String trainerUsername = "Juan.Perez";
        String traineeUsername = "Ana.Gomez";

        Trainer inactiveTrainer = new Trainer();
        inactiveTrainer.setUsername(trainerUsername);
        inactiveTrainer.setActive(false); // Inactive trainer

        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto(
                traineeUsername,  // traineeUsername first
                trainerUsername,  // trainerUsername second
                "Morning Training",
                LocalDate.now(),
                60
        );

        when(trainerService.findEntityByUsername(trainerUsername)).thenReturn(inactiveTrainer);

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> trainingService.createTraining(requestDto));

        assertTrue(exception.getMessage().contains("Trainer Juan.Perez is inactive"));
        verify(traineeService, never()).findEntityByUsername(anyString());
        verify(trainingRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when trainee is inactive")
    void testCreateTraining_InactiveTrainee() {
        // Given
        String trainerUsername = "Juan.Perez";
        String traineeUsername = "Ana.Gomez";

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeEnum.BOULDERING);

        Trainer activeTrainer = new Trainer();
        activeTrainer.setUsername(trainerUsername);
        activeTrainer.setActive(true);
        activeTrainer.setSpecialization(trainingType);

        Trainee inactiveTrainee = new Trainee();
        inactiveTrainee.setUsername(traineeUsername);
        inactiveTrainee.setActive(false); // Inactive trainee

        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto(
                traineeUsername,  // traineeUsername first
                trainerUsername,  // trainerUsername second
                "Evening Training",
                LocalDate.now(),
                90
        );

        when(trainerService.findEntityByUsername(trainerUsername)).thenReturn(activeTrainer);
        when(traineeService.findEntityByUsername(traineeUsername)).thenReturn(inactiveTrainee);

        // When & Then
        UnprocessableEntityException exception = assertThrows(
                UnprocessableEntityException.class,
                () -> trainingService.createTraining(requestDto)
        );

        assertTrue(exception.getMessage().contains("Trainee Ana.Gomez is inactive"));
        verify(trainingRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when trainer is not found")
    void testCreateTraining_TrainerNotFound() {
        // Given
        String trainerUsername = "NonExistent.Trainer";
        String traineeUsername = "Ana.Gomez";

        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto(
                traineeUsername,  // traineeUsername first
                trainerUsername,  // trainerUsername second
                "Cardio Training",
                LocalDate.now(),
                45
        );

        when(trainerService.findEntityByUsername(trainerUsername))
                .thenThrow(new ResourceNotFoundException("Trainer with username " + trainerUsername + " not found."));

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> trainingService.createTraining(requestDto)
        );

        assertTrue(exception.getMessage().contains("Trainer with username NonExistent.Trainer not found"));
        verify(traineeService, never()).findEntityByUsername(anyString());
        verify(trainingRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should throw exception when trainee is not found")
    void testCreateTraining_TraineeNotFound() {
        // Given
        String trainerUsername = "Juan.Perez";
        String traineeUsername = "NonExistent.Trainee";

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeEnum.ZUMBA);

        Trainer trainer = new Trainer();
        trainer.setUsername(trainerUsername);
        trainer.setActive(true);
        trainer.setSpecialization(trainingType);

        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto(
                traineeUsername,  // traineeUsername first
                trainerUsername,  // trainerUsername second
                "Yoga Session",
                LocalDate.now(),
                75
        );

        when(trainerService.findEntityByUsername(trainerUsername)).thenReturn(trainer);
        when(traineeService.findEntityByUsername(traineeUsername))
                .thenThrow(new ResourceNotFoundException("Trainee with username " + traineeUsername + " not found."));

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> trainingService.createTraining(requestDto)
        );

        assertTrue(exception.getMessage().contains("Trainee with username NonExistent.Trainee not found"));
        verify(trainingRepository, never()).create(any());
    }
}
