package com.jsalva.gymsystem.facade;

import com.jsalva.gymsystem.dto.request.*;
import com.jsalva.gymsystem.dto.response.*;
import com.jsalva.gymsystem.entity.*;

import java.util.List;
import java.util.Set;

public interface
GymFacade {
    // Auth
    String login(String username, String password);
    void updateUserPassword(ChangePasswordRequestDto requestDto);


    // Trainer
    CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto requestDto);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    TrainerResponseDto updateTrainer(UpdateTrainerRequestDto requestDto);
    void updateTrainerActiveState(String username, Boolean isActive);
    TrainerResponseDto findTrainerByUsername(String username);
    void updateTrainerPassword(Long id, String newPassword);
    Set<Trainee> getTraineeListForTrainer(Long id);


    // Trainee
    CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    TraineeResponseDto updateTrainee(UpdateTraineeRequestDto requestDto);
    void deleteTrainee(Long id);
    void updateTraineeActiveState(String username, Boolean isActive);
    TraineeResponseDto findTraineeByUsername(String username);
    void updateTraineePassword(Long id, String newPassword);
    void deleteTraineeByUsername(String username);
    List<TrainerSummaryDto> findUnassignedTrainersByTrainee(String traineeUsername);
    Set<Trainer> getTrainerListForTrainee(Long id);


    // Training
    void createTraining(CreateTrainingRequestDto requestDto);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
    List<TrainerTrainingListResponseDto> getTrainerTrainings(TrainerTrainingListRequestDto requestDto);
    List<TraineeTrainingListResponseDto> getTraineeTrainings(TraineeTrainingListRequestDto requestDto);
    TrainingResponseDto updateTraining(UpdateTrainingRequestDto trainingRequestDto);

    // Training Types
    List<TrainingTypeResponseDto> getAllTrainingTypes();
}
