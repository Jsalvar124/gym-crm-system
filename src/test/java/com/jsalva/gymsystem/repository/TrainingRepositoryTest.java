package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.repository.impl.TrainingRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    }

    @Test
    void shouldGetTrainerListByTraineeUsername() {
        // Given
        String username = "john.trainee";
        LocalDate fromDate = null;
        LocalDate toDate = null;

        List<Trainer> expectedTrainers = List.of(new Trainer(), new Trainer());

        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTrainers);

        // When
        List<Trainer> result = trainingRepository.getTrainerListByTraineeUsernameOrDateSpan(username, fromDate, toDate);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldGetTrainerListWithDateRange() {
        // Given
        String username = "john.trainee";
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);

        List<Trainer> expectedTrainers = List.of(new Trainer());

        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(eq("username"), any())).thenReturn(query);
        when(query.setParameter(eq("fromDate"), any())).thenReturn(query);
        when(query.setParameter(eq("toDate"), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTrainers);

        // When
        List<Trainer> result = trainingRepository.getTrainerListByTraineeUsernameOrDateSpan(username, fromDate, toDate);

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetTraineeListByTrainerUsername() {
        // Given
        String username = "jane.trainer";
        LocalDate fromDate = null;
        LocalDate toDate = null;

        List<Trainee> expectedTrainees = List.of(new Trainee(), new Trainee());

        TypedQuery<Trainee> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedTrainees);

        // When
        List<Trainee> result = trainingRepository.getTraineeListByTrainerUsernameOrDateSpan(username, fromDate, toDate);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTrainersFound() {
        // Given
        String username = "nonexistent.trainee";

        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of()); // Empty list

        // When
        List<Trainer> result = trainingRepository.getTrainerListByTraineeUsernameOrDateSpan(username, null, null);

        // Then
        assertTrue(result.isEmpty());
    }
}
