package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    GymFacade gymFacade;

    public TrainingController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping
    public ResponseEntity<Void> createTraining(@RequestBody CreateTrainingRequestDto requestDto){
        gymFacade.createTraining(requestDto);
        return ResponseEntity.ok().build();
    }

    // TODO PUT Training
}
