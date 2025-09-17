//package com.jsalva.gymsystem.repository;
//
//import com.jsalva.gymsystem.entity.Trainer;
//import com.jsalva.gymsystem.repository.impl.TrainerRepositoryImpl;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.TypedQuery;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerRepositoryTest {
//    @Mock
//    private EntityManager entityManager;
//
//    @Mock
//    private EntityTransaction transaction;
//
//    @InjectMocks
//    private TrainerRepositoryImpl trainerRepository;
//
//    @BeforeEach
//    void setUp() {
//        trainerRepository = new TrainerRepositoryImpl();
//        ReflectionTestUtils.setField(trainerRepository, "em", entityManager);
//
//    }
//
//    @Test
//    void shouldGenerateUniqueUsername() {
//        // Given
//        String firstName = "John";
//        String lastName = "Doe";
//
//        // Mock that "John.Doe" already exists in the database
//        List<String> existingUsernames = List.of("John.Doe", "John.Doe1");
//
//        TypedQuery<String> query = mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(query);
//        when(query.setParameter("baseUsername", "John.Doe%")).thenReturn(query);
//        when(query.getResultList()).thenReturn(existingUsernames);
//
//        // When
//        String username = trainerRepository.generateUniqueUsername(firstName, lastName);
//
//        // Then
//        assertEquals("John.Doe2", username); // Should be John.Doe2 since John.Doe and John.Doe1 exist
//    }
//
//    @Test
//    void shouldReturnBaseUsernameWhenNoneExist() {
//        // Given
//        String firstName = "Jane";
//        String lastName = "Smith";
//
//        // Mock that no usernames exist with this pattern
//        TypedQuery<String> query = mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(query);
//        when(query.setParameter("baseUsername", "Jane.Smith%")).thenReturn(query);
//        when(query.getResultList()).thenReturn(List.of()); // Empty list
//
//        // When
//        String username = trainerRepository.generateUniqueUsername(firstName, lastName);
//
//        // Then
//        assertEquals("Jane.Smith", username); // Should return base username
//    }
//}
