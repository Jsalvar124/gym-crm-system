package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PutMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> updateTrainee(@PathVariable("username") String username, @RequestBody UpdateTraineeRequestDto requestDto){
        if(!username.equals(requestDto.username())){
            return ResponseEntity.badRequest().build();
        }
        TraineeResponseDto responseDto = gymFacade.updateTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable("username") String username){
        TraineeResponseDto responseDto = gymFacade.findTraineeByUsername(username);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("username") String username){
        gymFacade.deleteTraineeByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}/active")
    public ResponseEntity<Map<String, String>> updateTraineeActiveState(@PathVariable("username") String username, @RequestBody Map<String, Boolean> requestBody){
        Boolean isActive = requestBody.get("isActive");
        gymFacade.updateTraineeActiveState(username, isActive);
        return ResponseEntity.ok().build();
    }
}
