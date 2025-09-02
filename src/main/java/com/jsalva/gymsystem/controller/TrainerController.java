package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerResponseDto;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.facade.GymFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/trainers")
public class TrainerController {
    GymFacade gymFacade;

    public TrainerController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
        System.out.println("TrainerController created successfully!"); // Add this
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

    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> getTrainerByUsername(@PathVariable("username") String username){
        TrainerResponseDto responseDto = gymFacade.findTrainerByUsername(username);
        return ResponseEntity.ok(responseDto);
    }





}
