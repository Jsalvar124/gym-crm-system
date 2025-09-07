package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.TraineeTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeTrainingListResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerSummaryDto;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainees")
public class TraineeController {
    private final GymFacade gymFacade;

    private final AuthService authService;



    public TraineeController(GymFacade gymFacade, AuthService authService) {
        this.gymFacade = gymFacade;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<CreateTraineeResponseDto> createTrainee(@Valid @RequestBody CreateTraineeRequestDto requestDto){
        CreateTraineeResponseDto responseDto = gymFacade.createTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> updateTrainee(@PathVariable("username") String username, @Valid @RequestBody UpdateTraineeRequestDto requestDto, @RequestHeader("X-Session-Id") String sessionId){
        if(!username.equals(requestDto.username())){
            return ResponseEntity.badRequest().build();
        }
        try {
            authService.validateOwnerAuth(sessionId, username);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TraineeResponseDto responseDto = gymFacade.updateTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        try {
            authService.validateTrainerOrOwnerAuth(sessionId, username);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TraineeResponseDto responseDto = gymFacade.findTraineeByUsername(username);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        try {
            authService.validateTrainerAuth(sessionId); // Only a trainer can delete a trainee
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        gymFacade.deleteTraineeByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}/state")
    public ResponseEntity<Map<String, String>> updateTraineeActiveState(@PathVariable("username") String username, @RequestBody Map<String, Boolean> requestBody, @RequestHeader("X-Session-Id") String sessionId){
        try {
            authService.validateTrainerOrOwnerAuth(sessionId, username); // Only Trainers or the trainee owner can soft delete.
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Boolean isActive = requestBody.get("isActive");
        gymFacade.updateTraineeActiveState(username, isActive);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TraineeTrainingListResponseDto>> getTraineeTrainingsWithFilters(@PathVariable("username") String traineeUsername,
                                                                                               @RequestParam(value="fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                               @RequestParam(value="toDate", required = false) LocalDate toDate,
                                                                                               @RequestParam(value="trainerUsername",required = false) String trainerUsername,
                                                                                               @RequestParam(value="trainingType", required = false) TrainingTypeEnum trainingType,
                                                                                               @RequestHeader("X-Session-Id") String sessionId) {

        try {
            authService.validateTrainerOrOwnerAuth(sessionId, traineeUsername);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TraineeTrainingListRequestDto requestDto = new TraineeTrainingListRequestDto(traineeUsername, fromDate, toDate, trainerUsername, trainingType);
        return ResponseEntity.ok(gymFacade.getTraineeTrainings(requestDto));
    }

    @GetMapping("{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerSummaryDto>> getUnassignedTrainers(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        try {
            authService.validateTrainerOrOwnerAuth(sessionId, username);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(gymFacade.findUnassignedTrainersByTrainee(username));
    }


}
