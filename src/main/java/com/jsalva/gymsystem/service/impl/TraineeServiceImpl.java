package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.repository.TraineeRepository;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.utils.EncoderUtils;
import com.jsalva.gymsystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeRepository traineeRepository;

    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }


    @Override
    public void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);

        //Generate and set random Password
        String randomPassword = UserUtils.generateRandomPassword();
        logger.debug("generated Password: {}", randomPassword);
        String hashedPassword = EncoderUtils.encryptPassword(randomPassword);
        trainee.setPassword(hashedPassword);

        // Create unique username
        String username = traineeRepository.generateUniqueUsername(firstName,lastName);
        trainee.setUsername(username);

        //Default isActive boolean set to true.
        trainee.setActive(true);

        // save new trainee
        traineeRepository.create(trainee);
        logger.debug("Saved Trainee: {}", trainee);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }

    @Override
    public Trainee getTraineeById(Long id) {
        Optional<Trainee> trainee = traineeRepository.findById(id);
        if(trainee.isEmpty()){
            throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
        }
        return trainee.get();
    }

    @Override
    public void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth) {
        // Verify the ID exists
        Optional<Trainee> result = traineeRepository.findById(userId);
        if(result.isEmpty()){
            logger.error("Trainee for update not found");
            throw new IllegalArgumentException("Trainee with Id " + userId + " not found.");
        }
        Trainee trainee = result.get();
        // Check if firstname or lastname changed and reassign username accordingly.
        if(firstName!=null && !firstName.equals(trainee.getFirstName()) || lastName != null && !lastName.equals(trainee.getLastName())){
            String username = traineeRepository.generateUniqueUsername(firstName,lastName);
            trainee.setUsername(username);
        }
        if(firstName != null){
            trainee.setFirstName(firstName);
        }
        if(lastName != null){
            trainee.setLastName(lastName);
        }
        if(newPassword != null){
            updatePassword(userId, newPassword);
        }
        if(isActive != null){
            trainee.setActive(isActive);
        }
        if(dateOfBirth != null){
            trainee.setDateOfBirth(dateOfBirth);
        }
        if(address != null){
            trainee.setAddress(address);
        }

        // Save in storage
        traineeRepository.update(trainee);
        logger.debug("Updated Trainee: {}", getTraineeById(userId)); // Retrieve from storage.
    }

    @Override
    public void deleteTrainee(Long id) {
        Optional<Trainee> trainee = traineeRepository.findById(id);
        if(trainee.isEmpty()){
            logger.error("Trainee for deletion not found");
            throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
        }
        logger.info("Deleting trainee with id {}", id);
        traineeRepository.delete(id);
    }

    @Override
    public void toggleActiveState(Long id) {
        Optional<Trainee> traineeFound = traineeRepository.findById(id);
        if(traineeFound.isEmpty()){
            logger.error("Trainee not found");
            throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
        }
        logger.info("Trainee with id {} state was modified", id);
        traineeRepository.toggleActiveState(id);
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        return traineeRepository.validateCredentials(username,password);
    }

    @Override
    public Trainee findByUsername(String username) {
        Optional<Trainee> trainee = traineeRepository.findByUsername(username);
        if(trainee.isEmpty()){
            logger.error("Trainee with username {} not found", username);
            throw new IllegalArgumentException("Trainee with username " + username + " not found.");
        }
        logger.info("Trainee found: {}", trainee.get());
        return trainee.get();
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        try{
            traineeRepository.updatePassword(id, newPassword);
            logger.info("Trainee's password updated");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTraineeByUsername(String username) {
        try{
            traineeRepository.deleteByUsername(username);
            logger.info("Trainee with username {} deleted", username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername) {
        return traineeRepository.findUnassignedTrainersByTrainee(traineeUsername);
    }
}
