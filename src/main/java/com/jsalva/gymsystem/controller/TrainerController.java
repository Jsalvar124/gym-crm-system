package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/trainers")
public class TrainerController {
    GymFacade gymFacade;

    public TrainerController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
        System.out.println("TrainerController created successfully!"); // Add this
    }

    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        List<Trainer> trainers = gymFacade.getAllTrainers();
        return ResponseEntity.ok(trainers);
    }

    @GetMapping("/test")
    public String test() {
        return "API is working!";
    }



}
