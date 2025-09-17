package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.repository.TraineeRepository;
import com.jsalva.gymsystem.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Trainee Service Tests")
public class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Mock
    private UserService userService;

    @Test
    void shouldCreateTraineeSuccessfully() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String address = "20 St. City";
        LocalDate date = LocalDate.of(2025,5,5);
        String email = "email@email.com";

        // Mock the dependencies
        when(userService.existsByEmail(email)).thenReturn(false);
        when(userService.generateUniqueUsername(firstName, lastName)).thenReturn("John.Doe");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(new Trainee());

        // When
        traineeService.createTrainee(new CreateTraineeRequestDto(firstName, lastName, date, email, address));

        // Then
        verify(userService).generateUniqueUsername(firstName, lastName);
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void shouldSetTraineePropertiesCorrectly() {
        // Given
        String firstName = "Jane";
        String lastName = "Smith";
        String address = "20 St. City";
        LocalDate date = LocalDate.of(2025,5,5);
        String email = "email@email.com";

        when(userService.existsByEmail(email)).thenReturn(false);
        when(userService.generateUniqueUsername(firstName, lastName)).thenReturn("Jane.Smith");

        // When
        traineeService.createTrainee(new CreateTraineeRequestDto(firstName, lastName, date, email, address));

        // Then - verify the trainee passed to create() has correct properties
        verify(traineeRepository).save(argThat(trainee ->
                trainee.getFirstName().equals("Jane") &&
                        trainee.getLastName().equals("Smith") &&
                        trainee.getUsername().equals("Jane.Smith") &&
                        trainee.isActive() == true &&
                        trainee.getAddress().equals("20 St. City") &&
                        trainee.getEmail().equals("email@email.com") &&
                        trainee.getDateOfBirth().equals(date) &&
                        trainee.getPassword() != null
        ));
    }
}
