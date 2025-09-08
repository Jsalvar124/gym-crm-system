package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainingRequestDto;
import com.jsalva.gymsystem.dto.response.TrainingResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    private final GymFacade gymFacade;

    private final AuthService authService;


    public TrainingController(GymFacade gymFacade, AuthService authService) {
        this.gymFacade = gymFacade;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> createTraining(@Valid @RequestBody CreateTrainingRequestDto requestDto, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerAuth(sessionId);
        gymFacade.createTraining(requestDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingResponseDto> updateTraining(@Valid @RequestBody UpdateTrainingRequestDto requestDto, @PathVariable("id") Long id, @RequestHeader("X-Session-Id") String sessionId) {
        authService.validateTrainerAuth(sessionId);
        TrainingResponseDto responseDto = gymFacade.updateTraining(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
