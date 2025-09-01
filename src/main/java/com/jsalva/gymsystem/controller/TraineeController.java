package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
