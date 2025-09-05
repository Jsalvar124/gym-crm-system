//package com.jsalva.gymsystem.repository;
//
//import com.jsalva.gymsystem.entity.Trainee;
//import com.jsalva.gymsystem.repository.impl.TraineeRepositoryImpl;
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
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TraineeRepositoryTest {
//    @Mock
//    private EntityManager entityManager;
//
//    @Mock
//    private EntityTransaction transaction;
//
//    @InjectMocks
//    private TraineeRepositoryImpl traineeRepository;
//
//    @BeforeEach
//    void setUp() {
//        traineeRepository = new TraineeRepositoryImpl();
//        ReflectionTestUtils.setField(traineeRepository, "em", entityManager);
//    }
//
//    // TODO MOVE TO SERVICE TEST
////    // Test 1: toggleActiveState - your main custom logic
////    @Test
////    void shouldToggleActiveFromTrueToFalse() {
////
////        // Given
////        Trainee trainee = new Trainee();
////        trainee.setActive(true);  // Start as active
////
////        when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);
////
////        // When
////        traineeRepository.toggleActiveState(1L);
////
////        // Then
////        assertFalse(trainee.getActive());  // Should now be inactive
////    }
////
////    // Test 2: validateCredentials - critical for security
////    @Test
////    void shouldReturnTrueWhenCredentialsAreCorrect() {
////        // Given
////        String username = "John.Doe";
////        String password = "newPassword";
////        String hashedPassword = "$2a$10$fA54/oCBsoUF15NuL1WOs.hA5CjP1v/M8USdioI7saEKe5njhjqAS";
////
////        Trainee trainee = new Trainee();
////        trainee.setPassword(hashedPassword);
////
////        // Mock the exact EntityManager calls from findByUsername
////        TypedQuery<Trainee> query = mock(TypedQuery.class);
////        when(entityManager.createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class))
////                .thenReturn(query);
////        when(query.setParameter("username", username)).thenReturn(query);
////        when(query.getSingleResult()).thenReturn(trainee);
////
////        // When
////        boolean result = traineeRepository.validateCredentials(username, password);
////
////        // Then
////        assertTrue(result);
////    }
//    // Test 3: generateUniqueUsername - business logic
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
//        String username = traineeRepository.generateUniqueUsername(firstName, lastName);
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
//        String username = traineeRepository.generateUniqueUsername(firstName, lastName);
//
//        // Then
//        assertEquals("Jane.Smith", username); // Should return base username
//    }
//
//    @Test
//    void shouldFindTraineeByUsername() {
//        // Given
//        String username = "John.Doe";
//        Trainee expectedTrainee = new Trainee();
//        expectedTrainee.setUsername(username);
//
//        TypedQuery<Trainee> query = mock(TypedQuery.class);
//        when(entityManager.createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class))
//                .thenReturn(query);
//        when(query.setParameter("username", username)).thenReturn(query);
//        when(query.getSingleResult()).thenReturn(expectedTrainee);
//
//        // Act
//        Optional<Trainee> result = traineeRepository.findByUsername(username);
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals(expectedTrainee, result.get());
//        assertEquals(username, result.get().getUsername());
//
//        verify(entityManager).createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class);
//        verify(query).setParameter("username", username);
//        verify(query).getSingleResult();
//    }
//}
