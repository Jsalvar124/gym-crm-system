package com.jsalva.gymsystem.facade.impl;

import com.jsalva.gymsystem.dto.request.*;
import com.jsalva.gymsystem.dto.response.*;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public class GymFacadeImpl implements GymFacade {
    private final Logger logger = LoggerFactory.getLogger(GymFacadeImpl.class);

    private final TrainerService trainerService;

    private final TraineeService traineeService;

    private final TrainingService trainingService;

    private final TrainingTypeService trainingTypeService;

    private final AuthService authService;

    public GymFacadeImpl(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService, TrainingTypeService trainingTypeService, AuthService authService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.authService = authService;
    }

    // No authentication needed.
    @Override
    public CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto requestDto) {
        return trainerService.createTrainer(requestDto);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @Override
    public Trainer getTrainerById(Long id) {
        return trainerService.getTrainerById(id);
    }

    @Override
    public TrainerResponseDto updateTrainer(UpdateTrainerRequestDto requestDto) {
        return trainerService.updateTrainer(requestDto);
    }

    @Override
    public void updateTrainerActiveState(String username, Boolean isActive) {
        trainerService.updateActiveState(username, isActive);
    }

    @Override
    public String login(String username, String password) {
        String result = authService.login(username,password); // This will be a token
        logger.info("Session Id {}",result);
        return result;
    }

    @Override
    public void updateUserPassword(ChangePasswordRequestDto requestDto) {
        authService.updatePassword(requestDto.username(), requestDto.oldPassword(), requestDto.newPassword());
    }


    @Override
    public TrainerResponseDto findTrainerByUsername(String username) {
        return trainerService.findByUsername(username);
    }

    @Override
    public Set<Trainee> getTraineeListForTrainer(Long id) {
        return trainerService.getTraineeSetForTrainer(id);
}

    // Trainee Methods
    // No authentication
    @Override
    public CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto) {
        return traineeService.createTrainee(requestDto);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    @Override
    public Trainee getTraineeById(Long id) {
        return traineeService.getTraineeById(id);
    }

    @Override
    public TraineeResponseDto updateTrainee(UpdateTraineeRequestDto requestDto) {
        return traineeService.updateTrainee(requestDto);
    }

    @Override
    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    @Override
    public void updateTraineeActiveState(String username, Boolean isActive) {
        traineeService.updateActiveState(username, isActive);
    }

    @Override
    public TraineeResponseDto findTraineeByUsername(String username) {
        return traineeService.findByUsername(username);
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        traineeService.deleteTraineeByUsername(username);
    }

    @Override
    public List<TrainerSummaryDto> findUnassignedTrainersByTrainee(String traineeUsername) {
        return traineeService.findUnassignedTrainersByTrainee(traineeUsername);
    }

    @Override
    public Set<Trainer> getTrainerListForTrainee(Long id) {
        return traineeService.getTrainersSetForTrainee(id);
    }

    @Override
    public void createTraining(CreateTrainingRequestDto requestDto) {
        trainingService.createTraining(requestDto);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    @Override
    public Training getTrainingById(Long id) {
        return trainingService.getTrainingById(id);
    }

    @Override
    public List<TrainerTrainingListResponseDto> getTrainerTrainings(TrainerTrainingListRequestDto requestDto) {
        return trainingService.getTrainersTrainingListByTraineeUsernameOrDateSpan(requestDto);
    }

    @Override
    public List<TraineeTrainingListResponseDto> getTraineeTrainings(TraineeTrainingListRequestDto requestDto) {
        return trainingService.getTraineesTrainingListByTrainerUsernameOrDateSpan(requestDto);
    }

    @Override
    public TrainingResponseDto updateTraining(Long id, UpdateTrainingRequestDto requestDto) {
        return trainingService.updateTraining(id, requestDto);
    }

    @Override
    public List<TrainingTypeResponseDto> getAllTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }

}
