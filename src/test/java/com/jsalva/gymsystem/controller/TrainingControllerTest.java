package com.jsalva.gymsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsalva.gymsystem.controller.advise.GlobalExceptionHandler;
import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.exception.InvalidCredentialsException;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TrainingControllerTest {
    private MockMvc mockMvc;
    private GymFacade gymFacade;
    private AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    @BeforeEach
    void setup() {
        gymFacade = Mockito.mock(GymFacade.class);
        authService = Mockito.mock(AuthService.class);
        TrainingController trainingController = new TrainingController(gymFacade, authService);

        mockMvc = MockMvcBuilders.standaloneSetup(trainingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createTraining_ShouldReturnUnauthorized_WhenTokenIsInvalid() throws Exception {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto(
                "John.Doe", "Ana.Gomez", "Training Name", LocalDate.now(), 60
        );

        doThrow(new InvalidCredentialsException("Invalid token - please login"))
                .when(authService).validateTrainerAuth();

        mockMvc.perform(post("/trainings")
                        .header("Authorization", "invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists()); // from ErrorResponseDto
    }

    @Test
    void createTraining_ShouldReturn200_WhenRequestIsValid() throws Exception {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto(
                "John.Doe", "Ana.Gomez", "Training Name", LocalDate.now(), 60
        );

        // auth passes, facade executes without error
        doNothing().when(authService).validateTrainerAuth();
        doNothing().when(gymFacade).createTraining(any(CreateTrainingRequestDto.class));

        mockMvc.perform(post("/trainings")
                        .header("X-Session-Id", "valid-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // since endpoint returns empty body

        verify(authService).validateTrainerAuth();
        verify(gymFacade).createTraining(any(CreateTrainingRequestDto.class));
    }
}
