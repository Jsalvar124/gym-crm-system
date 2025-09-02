package com.jsalva.gymsystem.facade;

import com.jsalva.gymsystem.dto.request.ChangePasswordRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerResponseDto;
import com.jsalva.gymsystem.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface
GymFacade {
    // Auth
    String login(String username, String password);
    void updateUserPassword(ChangePasswordRequestDto requestDto);


    //Trainer
    CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto requestDto);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive);
    void toggleTrainerActiveState(Long id);
    TrainerResponseDto findTrainerByUsername(String username);
    void updateTrainerPassword(Long id, String newPassword);
    Set<Trainee> getTraineeListForTrainer(Long id);

    //Trainee
    CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth);
    void deleteTrainee(Long id);
    void toggleTraineeActiveState(Long id);
    TraineeResponseDto findTraineeByUsername(String username);
    void updateTraineePassword(Long id, String newPassword);
    void deleteTraineeByUsername(String username);
    List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername);
    Set<Trainer> getTrainerListForTrainee(Long id);

    //Training
    void createTraining(CreateTrainingRequestDto requestDto);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
    List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);
    List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);

}
