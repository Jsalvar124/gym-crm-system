package com.jsalva.gymsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsalva.gymsystem.controller.advise.GlobalExceptionHandler;
import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.entity.Trainee;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TraineeControllerTest {
    private MockMvc mockMvc;
    private GymFacade gymFacade;
    private AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        gymFacade = Mockito.mock(GymFacade.class);
        authService = Mockito.mock(AuthService.class);
        TraineeController traineeController = new TraineeController(gymFacade, authService);

        mockMvc = MockMvcBuilders.standaloneSetup(traineeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createTrainee_ShouldReturn201AndLocationHeader() throws Exception {
        // given
        CreateTraineeRequestDto requestDto = new CreateTraineeRequestDto(
                "Ana", "Gomez", null,"agomez@email.com", "CR 27 # 12 -15"
        );

        CreateTraineeResponseDto responseDto =
                new CreateTraineeResponseDto("Ana.Gomez", "abc123FGH4");

        when(gymFacade.createTrainee(any(CreateTraineeRequestDto.class)))
                .thenReturn(responseDto);

        // when + then
        mockMvc.perform(post("/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/trainees/Ana.Gomez"))
                .andExpect(jsonPath("$.username").value("Ana.Gomez"))
                .andExpect(jsonPath("$.password").value("abc123FGH4"));

        verify(gymFacade).createTrainee(any(CreateTraineeRequestDto.class));
    }

    @Test
    void createTrainee_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        // Missing email → should trigger @NotBlank and @Email
        CreateTraineeRequestDto invalidRequest = new CreateTraineeRequestDto(
                "Ana", "Gomez", null,null, "CR 27 # 12 -15"
        );


        mockMvc.perform(post("/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists()); // Your ErrorResponseDto
    }

    @Test
    void createTrainee_ShouldReturn422_WhenEmailAlreadyExists() throws Exception {
        CreateTraineeRequestDto requestDto = new CreateTraineeRequestDto(
                "Ana", "Gomez", null,"agomez@email.com", "CR 27 # 12 -15"
        );

        when(gymFacade.createTrainee(any(CreateTraineeRequestDto.class)))
                .thenThrow(new UnprocessableEntityException("Unprocessable request - email already exists"));

        mockMvc.perform(post("/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Unprocessable request - email already exists"));

        verify(gymFacade).createTrainee(any(CreateTraineeRequestDto.class));
    }
}
