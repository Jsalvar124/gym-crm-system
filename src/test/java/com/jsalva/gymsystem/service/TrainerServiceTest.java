//package com.jsalva.gymsystem.service;
//
//import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
//import com.jsalva.gymsystem.entity.TrainingTypeEnum;
//import com.jsalva.gymsystem.entity.Trainer;
//import com.jsalva.gymsystem.entity.TrainingType;
//import com.jsalva.gymsystem.repository.TrainerRepository;
//import com.jsalva.gymsystem.service.impl.TrainerServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerServiceTest {
//
//    @Mock
//    private TrainerRepository trainerRepository;
//
//    @Mock
//    private TrainingTypeService trainingTypeService;
//
//    @InjectMocks
//    private TrainerServiceImpl trainerService;
//
//    @Test
//    void shouldCreateTrainerSuccessfully() {
//        // Given
//        String firstName = "John";
//        String lastName = "Doe";
//        TrainingTypeEnum trainingType = TrainingTypeEnum.BOXING;
//
//        TrainingType mockTrainingType = new TrainingType();
//        mockTrainingType.setTrainingTypeName(trainingType);
//
//        // Mock the dependencies
//        when(trainingTypeService.findTrainingTypeByName(trainingType)).thenReturn(mockTrainingType);
//        when(trainerRepository.generateUniqueUsername(firstName, lastName)).thenReturn("John.Doe");
//        when(trainerRepository.create(any(Trainer.class))).thenReturn(new Trainer());
//
//        // When
//        trainerService.createTrainer(new CreateTrainerRequestDto(firstName, lastName, trainingType));
//
//        // Then
//        verify(trainingTypeService).findTrainingTypeByName(trainingType);
//        verify(trainerRepository).generateUniqueUsername(firstName, lastName);
//        verify(trainerRepository).create(any(Trainer.class));
//    }
//
//    @Test
//    void shouldSetTrainerPropertiesCorrectly() {
//        // Given
//        String firstName = "Jane";
//        String lastName = "Smith";
//        TrainingTypeEnum trainingType = TrainingTypeEnum.ZUMBA;
//
//        TrainingType mockTrainingType = new TrainingType();
//        mockTrainingType.setTrainingTypeName(trainingType);
//
//        when(trainingTypeService.findTrainingTypeByName(trainingType)).thenReturn(mockTrainingType);
//        when(trainerRepository.generateUniqueUsername(firstName, lastName)).thenReturn("Jane.Smith");
//
//        // When
//        trainerService.createTrainer(new CreateTrainerRequestDto(firstName, lastName, trainingType));
//
//        // Then - verify the trainer passed to create() has correct properties
//        verify(trainerRepository).create(argThat(trainer ->
//                trainer.getFirstName().equals("Jane") &&
//                        trainer.getLastName().equals("Smith") &&
//                        trainer.getUsername().equals("Jane.Smith") &&
//                        trainer.getActive() == true &&
//                        trainer.getSpecialization().equals(mockTrainingType) &&
//                        trainer.getPassword() != null
//        ));
//    }
//
//    @Test
//    void shouldReturnTrainerWhenIdExists() {
//        // Given an id
//        Long trainerId = 1L;
//        Trainer trainer = new Trainer();
//        trainer.setUsername("Juan.Perez");
//
//        //Mock the trainerRepository response
//        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
//
//        // Make the call to the tested method
//        Trainer trainerResult = trainerService.getTrainerById(trainerId);
//
//        //Verify (Expected, Actual)
//        assertEquals(trainer.getUsername(), trainerResult.getUsername());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenTrainerNotFound() {
//        // Given an invalid Id
//        Long trainerId = 999L;
//        Trainer trainer = new Trainer();
//        trainer.setUsername("Juan.Perez");
//
//        //Mock the trainerRepository response
//        when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());
//
//        //Verify Exception is thrown
//        Exception ex = assertThrows(IllegalArgumentException.class, ()->{
//            //the call of the method with an invalid id
//            trainerService.getTrainerById(trainerId);
//        });
//
//        assertEquals("Trainer with Id 999 not found.", ex.getMessage());
//    }
//}
