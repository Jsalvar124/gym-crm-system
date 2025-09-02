package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainees")
public class TraineeController {
    GymFacade gymFacade;

    public TraineeController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping
    public ResponseEntity<CreateTraineeResponseDto> createTrainee(@RequestBody CreateTraineeRequestDto requestDto){
        CreateTraineeResponseDto responseDto = gymFacade.createTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<TraineeResponseDto> updateTrainee(@RequestBody UpdateTraineeRequestDto requestDto){
        TraineeResponseDto responseDto = gymFacade.updateTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable("username") String username){
        TraineeResponseDto responseDto = gymFacade.findTraineeByUsername(username);
        return ResponseEntity.ok(responseDto);
    }
}
