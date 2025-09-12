package com.jsalva.gymsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsalva.gymsystem.controller.advise.GlobalExceptionHandler;
import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.exception.UnprocessableEntityException;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class TrainerControllerTest {
    private MockMvc mockMvc;
    private GymFacade gymFacade;
    private AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        gymFacade = Mockito.mock(GymFacade.class);
        authService = Mockito.mock(AuthService.class);
        TrainerController trainerController = new TrainerController(gymFacade, authService);

        mockMvc = MockMvcBuilders.standaloneSetup(trainerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createTrainer_ShouldReturn201AndLocationHeader() throws Exception {
        // given
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto(
                "John", "Doe", TrainingTypeEnum.PILATES, "jdoe@email.com"
        );

        CreateTrainerResponseDto responseDto =
                new CreateTrainerResponseDto("John.Doe", "abc123FGH4");

        when(gymFacade.createTrainer(any(CreateTrainerRequestDto.class)))
                .thenReturn(responseDto);

        // when + then
        mockMvc.perform(post("/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/trainers/John.Doe"))
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("abc123FGH4"));

        verify(gymFacade).createTrainer(any(CreateTrainerRequestDto.class));
    }

    @Test
    void createTrainer_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        // Missing email → should trigger @NotBlank and @Email
        CreateTrainerRequestDto invalidRequest = new CreateTrainerRequestDto(
                "John", "Doe", TrainingTypeEnum.PILATES, "incorrectEmail"
        );

        mockMvc.perform(post("/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists()); // Your ErrorResponseDto
    }

    @Test
    void createTrainer_ShouldReturn422_WhenEmailAlreadyExists() throws Exception {
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto(
                "John", "Doe", TrainingTypeEnum.PILATES, "jdoe@email.com"
        );

        when(gymFacade.createTrainer(any(CreateTrainerRequestDto.class)))
                .thenThrow(new UnprocessableEntityException("Unprocessable request - email already exists"));

        mockMvc.perform(post("/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Unprocessable request - email already exists"));

        verify(gymFacade).createTrainer(any(CreateTrainerRequestDto.class));
    }
}
