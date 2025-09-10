package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.repository.impl.TrainingRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingRepositoryTest {

    @Mock
    EntityManager entityManager;

    TrainingRepositoryImpl trainingRepository;

    @BeforeEach
    void setUp(){
        trainingRepository = new TrainingRepositoryImpl();
        ReflectionTestUtils.setField(trainingRepository, "em", entityManager);

    }

    @Test
    void shouldGetTrainerListByTraineeUsername() {
        // Given
        String trainerUsername = "John.Trainer";
        LocalDate fromDate = null;
        LocalDate toDate = null;
        String traineeUsername = "Jane.Trainee";


        List<Training> expectedTrainings = List.of(new Training(), new Training());

        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTrainings);

        // When
        List<Training> result = trainingRepository.getTrainersTrainingListByTraineeUsernameOrDateSpan(trainerUsername, fromDate, toDate, traineeUsername);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTrainersFound() {
        // Given
        String trainerUsername = "nonexistent.trainer";

        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter("trainerUsername", trainerUsername)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of()); // Empty list

        // When
        List<Training> result = trainingRepository.getTrainersTrainingListByTraineeUsernameOrDateSpan(trainerUsername, null, null, null);

        // Then
        assertTrue(result.isEmpty());
    }
}
