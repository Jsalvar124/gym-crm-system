package com.jsalva.gymsystem.facade.impl;

import com.jsalva.gymsystem.dto.request.ChangePasswordRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class GymFacadeImpl implements GymFacade {
    private final Logger logger = LoggerFactory.getLogger(GymFacadeImpl.class);

    private final TrainerService trainerService;

    private final TraineeService traineeService;

    private final TrainingService trainingService;

    private final AuthService authService;

    public GymFacadeImpl(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService, AuthService authService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.authService = authService;
    }

    // No authentication needed.
    @Override
    public CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto requestDto) {
        return trainerService.createTrainer(requestDto);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        try {
//            requireAuthentication(); // Trainer or Trainee can access.
            return trainerService.getAllTrainers();
        }catch (Exception e){
            logger.error("Error fetching trainers: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Trainer getTrainerById(Long id) {
        try {
            return trainerService.getTrainerById(id);
        }catch (IllegalArgumentException e){
            logger.error("Error fetching trainer: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive) {
        try {
            trainerService.updateTrainer(userId, firstName, lastName ,trainingType, newPassword, isActive);
        }catch (IllegalArgumentException e){
            logger.error("Error in trainer update: {}", e.getMessage());
        }
    }

    @Override
    public void toggleTrainerActiveState(Long id) {
        try {
            trainerService.toggleActiveState(id);
        }catch (IllegalArgumentException e){
            logger.error("Error changing trainer's active status: {}", e.getMessage());
        }
    }

    @Override
    public String login(String username, String password) {
        try {
            String result = authService.login(username,password); // This will be a token
            logger.info("RESULT {}",result);
            return result;
        }catch (SecurityException e){
            logger.error("Error in login: {}", e.getMessage());
        }
        return "";
    }

    @Override
    public void updateUserPassword(ChangePasswordRequestDto requestDto) {
        authService.updatePassword(requestDto.username(), requestDto.oldPassword(), requestDto.newPassword());
    }


    @Override
    public Trainer findTrainerByUsername(String username) {
        try {
            return trainerService.findByUsername(username);
        }catch (IllegalArgumentException e){
            logger.error("Error looking for trainer with username {}, {}",username, e.getMessage());
            return null;
        }
    }

    @Override
    public void updateTrainerPassword(Long id, String newPassword) {
        try{
            trainerService.updatePassword(id, newPassword);
            logger.info("Trainer's password with id {} updated", id);

        }catch (Exception e){
            logger.error("Error updating trainer's password with id {} ", id);
        }
    }

    @Override
    public Set<Trainee> getTraineeListForTrainer(Long id) {
        try {
            return trainerService.getTraineeSetForTriner(id);
        } catch (Exception e) {
            logger.error("Error finding trainee's set for trainer with id {}", id);
        }
        return Set.of();    }

    // Trainee Methods
    // No authentication
    @Override
    public CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto) {
        return traineeService.createTrainee(requestDto);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        try {
            return traineeService.getAllTrainees();
        } catch (Exception e) {
            logger.error("Error fetching trainees: {}", e.getMessage());
            return List.of();
        }    }

    @Override
    public Trainee getTraineeById(Long id) {
        try {
            return traineeService.getTraineeById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching trainee: {}", e.getMessage());
            return null;
        }    }

    @Override
    public void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth) {
        try {
            traineeService.updateTrainee(userId, firstName, lastName, newPassword, isActive, address, dateOfBirth);
        } catch (IllegalArgumentException e) {
            logger.error("Error in trainee update: {}", e.getMessage());
        }
    }

    @Override
    public void deleteTrainee(Long id) {
        try {
            traineeService.deleteTrainee(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting trainee: {}", e.getMessage());
        }
    }

    @Override
    public void toggleTraineeActiveState(Long id) {
        try {
            traineeService.toggleActiveState(id);
        }catch (IllegalArgumentException e){
            logger.error("Error changing trainee's active status: {}", e.getMessage());
        }
    }

    @Override
    public TraineeResponseDto findTraineeByUsername(String username) {
        try {
            return traineeService.findByUsername(username);
        }catch (IllegalArgumentException e){
            logger.error("Error looking for trainee with username {}, {}",username, e.getMessage());
            return null;
        }
    }

    @Override
    public void updateTraineePassword(Long id, String newPassword) {
        try{
            traineeService.updatePassword(id, newPassword);
        }catch (Exception e){
            logger.error("Error updating trainee's password with id {} ", id);
        }
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        try{
            traineeService.deleteTraineeByUsername(username);
        }catch (Exception e){
            logger.error("Error deleting trainee with username {}", username);
        }
    }

    @Override
    public List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername) {
        try{
            return traineeService.findUnassignedTrainersByTrainee(traineeUsername);
        }catch (Exception e){
            logger.error("Error finding unassigned trainers for trainee with username {}", traineeUsername);
        }
        return List.of();
    }

    @Override
    public Set<Trainer> getTrainerListForTrainee(Long id) {
        try {
            return traineeService.getTrainersSetForTrainee(id);
        } catch (Exception e) {
            logger.error("Error finding trainers set for trainee with id {}", id);
        }
        return Set.of();
    }

    @Override
    public void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingTypeEnum trainingType, LocalDate trainingDate, Integer duration) {
        try {
            trainingService.createTraining(trainerId, traineeId, trainingName, trainingType, trainingDate, duration);
        } catch (IllegalArgumentException e) {
            logger.error("Error in training creation: {}", e.getMessage());
        }
    }

    @Override
    public List<Training> getAllTrainings() {
        try {
            return trainingService.getAllTrainings();
        } catch (Exception e) {
            logger.error("Error fetching trainings: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public Training getTrainingById(Long id) {
        try {
            return trainingService.getTrainingById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching training: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        try {
            return trainingService.getTrainerListByTraineeUsernameOrDateSpan(username, fromDate, toDate);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching trainer's list for trainee: {} {}",username, e.getMessage());
            return null;
        }
    }

    @Override
    public List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        try {
            return trainingService.getTraineeListByTrainerUsernameOrDateSpan(username, fromDate, toDate);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching traiee's list for trainer: {} {}",username, e.getMessage());
            return null;
        }
    }

}
