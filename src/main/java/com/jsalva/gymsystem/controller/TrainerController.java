package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.request.TrainerTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerTrainingListResponseDto;
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
@RequestMapping("/trainers")
public class TrainerController {
    private final GymFacade gymFacade;

    private final AuthService authService;

    public TrainerController(GymFacade gymFacade, AuthService authService) {
        this.gymFacade = gymFacade;
        this.authService = authService;
    }

    @GetMapping("/test")
    public String test() {
        return "API is working!";
    }

    @PostMapping
    public ResponseEntity<CreateTrainerResponseDto> createTrainer(@RequestBody @Valid CreateTrainerRequestDto requestDto){
        CreateTrainerResponseDto responseDto = gymFacade.createTrainer(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> updateTrainer(@PathVariable("username") String username, @RequestBody UpdateTrainerRequestDto requestDto, @RequestHeader("X-Session-Id") String sessionId){
        if(!username.equals(requestDto.username())){
            return ResponseEntity.badRequest().build();
        }
        try {
            authService.validateOwnerAuth(sessionId, username);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TrainerResponseDto responseDto = gymFacade.updateTrainer(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> getTrainerByUsername(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        try {
            authService.validateTrainerAuth(sessionId);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TrainerResponseDto responseDto = gymFacade.findTrainerByUsername(username);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{username}/state")
    public ResponseEntity<Map<String, String>> updateTrainerActiveState(@PathVariable("username") String username, @RequestBody Map<String, Boolean> requestBody, @RequestHeader("X-Session-Id") String sessionId){
        try {
            authService.validateTrainerAuth(sessionId);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Boolean isActive = requestBody.get("isActive");
        gymFacade.updateTrainerActiveState(username, isActive);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TrainerTrainingListResponseDto>> getTrainerTrainingsWithFilters(@PathVariable("username") String trainerUsername,
                                                                                               @RequestParam(value="fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                               @RequestParam(value="toDate", required = false) LocalDate toDate,
                                                                                               @RequestParam(value="traineeUsername",required = false) String traineeUsername,
                                                                                               @RequestHeader("X-Session-Id") String sessionId){
        try {
            authService.validateTrainerAuth(sessionId);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TrainerTrainingListRequestDto requestDto = new TrainerTrainingListRequestDto(trainerUsername, fromDate, toDate, traineeUsername);
        return ResponseEntity.ok(gymFacade.getTrainerTrainings(requestDto));
    }

}
