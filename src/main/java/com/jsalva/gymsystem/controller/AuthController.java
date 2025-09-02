package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.ChangePasswordRequestDto;
import com.jsalva.gymsystem.dto.request.LoginRequestDto;
import com.jsalva.gymsystem.facade.GymFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    GymFacade gymFacade;

    public AuthController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        String token = gymFacade.login(loginRequestDto.username(), loginRequestDto.password());
        return ResponseEntity.ok(Map.of("Token", token)); //TODO
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequestDto requestDto){
        gymFacade.updateUserPassword(requestDto);
        return ResponseEntity.ok().build();
    }

}
