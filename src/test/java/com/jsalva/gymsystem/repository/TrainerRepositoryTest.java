package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.repository.impl.TrainerRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerRepositoryTest {
    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @InjectMocks
    private TrainerRepositoryImpl trainerRepository;

    @BeforeEach
    void setUp() {
        trainerRepository = new TrainerRepositoryImpl(entityManager);

    }

    // Test 1: toggleActiveState - your main custom logic
    @Test
    void shouldToggleActiveFromTrueToFalse() {

        when(entityManager.getTransaction()).thenReturn(transaction);
        // Given
        Trainer trainer = new Trainer();
        trainer.setActive(true);  // Start as active

        when(entityManager.find(Trainer.class, 1L)).thenReturn(trainer);

        // When
        trainerRepository.toggleActiveState(1L);

        // Then
        assertFalse(trainer.getActive());  // Should now be inactive
    }

    // Test 2: validateCredentials - critical for security
    @Test
    void shouldReturnTrueWhenCredentialsAreCorrect() {
        // Given
        String username = "John.Doe";
        String password = "newPassword";
        String hashedPassword = "$2a$10$fA54/oCBsoUF15NuL1WOs.hA5CjP1v/M8USdioI7saEKe5njhjqAS";

        Trainer trainer = new Trainer();
        trainer.setPassword(hashedPassword);

        // Mock the exact EntityManager calls from findByUsername
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainer);

        // When
        boolean result = trainerRepository.validateCredentials(username, password);

        // Then
        assertTrue(result);
    }
    // Test 3: generateUniqueUsername - business logic
    @Test
    void shouldGenerateUniqueUsername() {
        // Given
        String firstName = "John";
        String lastName = "Doe";

        // Mock that "John.Doe" already exists in the database
        List<String> existingUsernames = List.of("John.Doe", "John.Doe1");

        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(query);
        when(query.setParameter("baseUsername", "John.Doe%")).thenReturn(query);
        when(query.getResultList()).thenReturn(existingUsernames);

        // When
        String username = trainerRepository.generateUniqueUsername(firstName, lastName);

        // Then
        assertEquals("John.Doe2", username); // Should be John.Doe2 since John.Doe and John.Doe1 exist
    }

    @Test
    void shouldReturnBaseUsernameWhenNoneExist() {
        // Given
        String firstName = "Jane";
        String lastName = "Smith";

        // Mock that no usernames exist with this pattern
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(query);
        when(query.setParameter("baseUsername", "Jane.Smith%")).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of()); // Empty list

        // When
        String username = trainerRepository.generateUniqueUsername(firstName, lastName);

        // Then
        assertEquals("Jane.Smith", username); // Should return base username
    }
}
