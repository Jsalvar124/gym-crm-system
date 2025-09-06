package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainingRequestDto;
import com.jsalva.gymsystem.dto.response.TrainingResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> createTraining(@RequestBody CreateTrainingRequestDto requestDto, @RequestHeader("X-Session-Id") String sessionId){
        try{
            authService.validateTrainerAuth(sessionId);
        }catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        gymFacade.createTraining(requestDto);
        return ResponseEntity.ok().build();
    }

    // TODO PUT Training
    @GetMapping("/{id}")
    public ResponseEntity<TrainingResponseDto> updateTraining(@RequestBody UpdateTrainingRequestDto requestDto, @PathVariable Integer id, @RequestHeader("X-Session-Id") String sessionId) {
        try {
            authService.validateTrainerAuth(sessionId);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TrainingResponseDto responseDto = gymFacade.updateTraining(requestDto);


    }
}
