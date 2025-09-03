package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.response.TrainingTypeResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("training-types")
public class TrainingTypeController {
    GymFacade gymFacade;

    public TrainingTypeController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDto>> getTrainingTypes(){
        List<TrainingTypeResponseDto> trainingTypes = gymFacade.getAllTrainingTypes();
        return ResponseEntity.ok(trainingTypes);
    }
}
