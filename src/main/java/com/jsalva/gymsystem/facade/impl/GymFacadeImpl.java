package com.jsalva.gymsystem.facade.impl;

import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.facade.GymFacade;
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

    private User userLogged;

    private UserType loggedUserType;

    private boolean isAuthenticated = false;

    public GymFacadeImpl(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public enum UserType{
        TRAINER,
        TRAINEE
    }

    public User getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
    }

    public UserType getLoggedUserType() {
        return loggedUserType;
    }

    public void setLoggedUserType(UserType loggedUserType) {
        this.loggedUserType = loggedUserType;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    // No authentication needed.
    @Override
    public void createTrainer(String firstName, String lastName, TrainingTypeEnum trainingType) {
        try {
            trainerService.createTrainer(firstName,lastName,trainingType);
        }catch (IllegalArgumentException e){
            logger.error("Error in trainer creation: {}", e.getMessage());
        }
    }

    @Override
    public List<Trainer> getAllTrainers() {
        try {
            requireAuthentication(); // Trainer or Trainee can access.
            return trainerService.getAllTrainers();
        }catch (Exception e){
            logger.error("Error fetching trainers: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Trainer getTrainerById(Long id) {
        try {
            requireAuthentication();
            return trainerService.getTrainerById(id);
        }catch (IllegalArgumentException e){
            logger.error("Error fetching trainer: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive) {
        try {
            requireOwnership(userId);
            trainerService.updateTrainer(userId, firstName, lastName ,trainingType, newPassword, isActive);
        }catch (IllegalArgumentException e){
            logger.error("Error in trainer update: {}", e.getMessage());
        }
    }

    @Override
    public void toggleTrainerActiveState(Long id) {
        try {
            requireTrainerAuthentication();
            trainerService.toggleActiveState(id);
        }catch (IllegalArgumentException e){
            logger.error("Error changing trainer's active status: {}", e.getMessage());
        }
    }

    @Override
    public boolean login(String username, String password) {
        try {
            if(trainerService.validateCredentials(username,password)){
                setAuthenticated(true);
                setLoggedUserType(UserType.TRAINER);
                setUserLogged(new User());
                setUserLogged(findTrainerByUsername(username));
                logger.info("Login successful for trainer {}", username);
                return true;
            } else if (traineeService.validateCredentials(username,password)) {
                setAuthenticated(true);
                setLoggedUserType(UserType.TRAINEE);
                setUserLogged(new User());
                setUserLogged(findTraineeByUsername(username));
                logger.info("Login successful for trainee {}", username);
                return true;
            } else {
                logger.warn("Invalid Credentials");
                return false;
            }
        }catch (SecurityException e){
            logger.error("Error in login: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public void logout() {
        try {
            requireAuthentication();
            setUserLogged(null);
            setLoggedUserType(null);
            setAuthenticated(false);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
        }
    }


    @Override
    public Trainer findTrainerByUsername(String username) {
        try {
            requireAuthentication();
            return trainerService.findByUsername(username);
        }catch (IllegalArgumentException e){
            logger.error("Error looking for trainer with username {}, {}",username, e.getMessage());
            return null;
        }
    }

    @Override
    public void updateTrainerPassword(Long id, String newPassword) {
        try{
            requireOwnership(id);
            trainerService.updatePassword(id, newPassword);
            logger.info("Trainer's password with id {} updated", id);

        }catch (Exception e){
            logger.error("Error updating trainer's password with id {} ", id);
        }
    }

    @Override
    public Set<Trainee> getTraineeListForTrainer(Long id) {
        try {
            requireAuthentication();
            return trainerService.getTraineeSetForTriner(id);
        } catch (Exception e) {
            logger.error("Error finding trainee's set for trainer with id {}", id);
        }
        return Set.of();    }

    // Trainee Methods
    // No authentication
    @Override
    public void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        try {
            traineeService.createTrainee(firstName, lastName, address, dateOfBirth);
        } catch (Exception e) {
            logger.error("Error in trainee creation: {}", e.getMessage());
        }
    }

    @Override
    public List<Trainee> getAllTrainees() {
        try {
            requireTrainerAuthentication();
            return traineeService.getAllTrainees();
        } catch (Exception e) {
            logger.error("Error fetching trainees: {}", e.getMessage());
            return List.of();
        }    }

    @Override
    public Trainee getTraineeById(Long id) {
        try {
            requireTrainerAuthentication();
            return traineeService.getTraineeById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching trainee: {}", e.getMessage());
            return null;
        }    }

    @Override
    public void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth) {
        try {
            requireOwnership(userId);
            traineeService.updateTrainee(userId, firstName, lastName, newPassword, isActive, address, dateOfBirth);
        } catch (IllegalArgumentException e) {
            logger.error("Error in trainee update: {}", e.getMessage());
        }
    }

    @Override
    public void deleteTrainee(Long id) {
        try {
            requireTrainerAuthentication();
            traineeService.deleteTrainee(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting trainee: {}", e.getMessage());
        }
    }

    @Override
    public void toggleTraineeActiveState(Long id) {
        try {
            requireTrainerAuthentication();
            traineeService.toggleActiveState(id);
        }catch (IllegalArgumentException e){
            logger.error("Error changing trainee's active status: {}", e.getMessage());
        }
    }

    @Override
    public Trainee findTraineeByUsername(String username) {
        try {
            requireAuthentication();
            return traineeService.findByUsername(username);
        }catch (IllegalArgumentException e){
            logger.error("Error looking for trainee with username {}, {}",username, e.getMessage());
            return null;
        }
    }

    @Override
    public void updateTraineePassword(Long id, String newPassword) {
        try{
            requireOwnership(id);
            traineeService.updatePassword(id, newPassword);
        }catch (Exception e){
            logger.error("Error updating trainee's password with id {} ", id);
        }
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        try{
            requireTrainerAuthentication();
            traineeService.deleteTraineeByUsername(username);
        }catch (Exception e){
            logger.error("Error deleting trainee with username {}", username);
        }
    }

    @Override
    public List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername) {
        try{
            requireTrainerAuthentication();
            return traineeService.findUnassignedTrainersByTrainee(traineeUsername);
        }catch (Exception e){
            logger.error("Error finding unassigned trainers for trainee with username {}", traineeUsername);
        }
        return List.of();
    }

    @Override
    public Set<Trainer> getTrainerListForTrainee(Long id) {
        try {
            requireAuthentication();
            return traineeService.getTrainersSetForTrainee(id);
        } catch (Exception e) {
            logger.error("Error finding trainers set for trainee with id {}", id);
        }
        return Set.of();
    }

    @Override
    public void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingTypeEnum trainingType, LocalDate trainingDate, Integer duration) {
        try {
            requireTrainerAuthentication();
            trainingService.createTraining(trainerId, traineeId, trainingName, trainingType, trainingDate, duration);
        } catch (IllegalArgumentException e) {
            logger.error("Error in training creation: {}", e.getMessage());
        }
    }

    @Override
    public List<Training> getAllTrainings() {
        try {
            requireTrainerAuthentication();
            return trainingService.getAllTrainings();
        } catch (Exception e) {
            logger.error("Error fetching trainings: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public Training getTrainingById(Long id) {
        try {
            requireTrainerAuthentication();
            return trainingService.getTrainingById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching training: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        try {
            requireTrainerAuthentication();
            return trainingService.getTrainerListByTraineeUsernameOrDateSpan(username, fromDate, toDate);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching trainer's list for trainee: {} {}",username, e.getMessage());
            return null;
        }
    }

    @Override
    public List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate) {
        try {
            requireTrainerAuthentication();
            return trainingService.getTraineeListByTrainerUsernameOrDateSpan(username, fromDate, toDate);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching traiee's list for trainer: {} {}",username, e.getMessage());
            return null;
        }    }

    private void requireAuthentication() {
        if (!isAuthenticated || userLogged == null) {
            logger.error("Attempt to access protected resource without authentication");
            throw new SecurityException("Authentication required");
        }
    }

    private void requireTrainerAuthentication() {
        requireAuthentication();
        if (!UserType.TRAINER.equals(loggedUserType)) {
            logger.error("User {} attempted trainer-only operation", userLogged.getUsername());
            throw new SecurityException("Trainer authentication required");
        }
    }

    private void requireTraineeAuthentication() {
        requireAuthentication();
        if (!UserType.TRAINEE.equals(loggedUserType)) {
            logger.error("User {} attempted trainee-only operation", userLogged.getUsername());
            throw new SecurityException("Trainee authentication required");
        }
    }

    private void requireOwnership(Long userId) {
        requireAuthentication();

        if (!userLogged.getId().equals(userId)) {
            logger.error("User {} attempted to access resource belonging to user {}",
                    userLogged.getUsername(), userId);
            throw new SecurityException("Access denied: insufficient permissions");
        }
    }
}
