package com.jsalva.gymsystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.mapper.TraineeMapper;
import com.jsalva.gymsystem.repository.TraineeRepository;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.utils.EncoderUtils;
import com.jsalva.gymsystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeRepository traineeRepository;

    private final TraineeMapper traineeMapper;

    public TraineeServiceImpl(TraineeRepository traineeRepository, TraineeMapper traineeMapper) {
        this.traineeRepository = traineeRepository;
        this.traineeMapper = traineeMapper;
    }


    @Override
    @Transactional
    public CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(requestDto.firstName());
        trainee.setLastName(requestDto.lastName());
        trainee.setAddress(requestDto.address());
        trainee.setDateOfBirth(requestDto.dateOfBirth());

        //Generate and set random Password
        String randomPassword = UserUtils.generateRandomPassword();
        logger.debug("generated Password: {}", randomPassword);
        String hashedPassword = EncoderUtils.encryptPassword(randomPassword);
        trainee.setPassword(hashedPassword);

        // Create unique username
        String username = traineeRepository.generateUniqueUsername(requestDto.firstName(), requestDto.lastName());
        logger.debug("generated username: {}", username);
        trainee.setUsername(username);

        //Default isActive boolean set to true.
        trainee.setActive(true);

        // save new trainee
        traineeRepository.create(trainee);
        logger.debug("Saved Trainee: {}", trainee);

        // return dto
        return new CreateTraineeResponseDto(username, randomPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee getTraineeById(Long id) {
        Optional<Trainee> trainee = traineeRepository.findById(id);
        if(trainee.isEmpty()){
            throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
        }
        return trainee.get();
    }

    @Override
    @Transactional
    public TraineeResponseDto updateTrainee(UpdateTraineeRequestDto requestDto) {
        // Verify the username exists for a given Trainee
        Trainee trainee = findEntityByUsername(requestDto.username());
        // Check if firstname or lastname changed and reassign username accordingly.
        String firstName = requestDto.firstName();
        String lastName = requestDto.lastName();

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
        if(requestDto.isActive() != null){
            trainee.setActive(requestDto.isActive());
        }
        if(requestDto.dateOfBirth() != null){
            trainee.setDateOfBirth(requestDto.dateOfBirth());
        }
        if(requestDto.address() != null){
            trainee.setAddress(requestDto.address());
        }

        // Save in storage
        traineeRepository.update(trainee);
        logger.debug("Updated Trainee: {}", requestDto.username());
        return traineeMapper.toResponseDto(trainee);
    }

    @Override
    @Transactional
    public void deleteTrainee(Long id) {
        Optional<Trainee> result = traineeRepository.findById(id);
        if(result.isEmpty()){
            logger.error("Trainee for deletion not found");
            throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
        }
        logger.info("Deleting trainee with id {}", id);
        // Clean up many-to-many relationships BEFORE deletion, since Trainee is the owner of the relation.
        Trainee trainee = result.get();
        Set<Trainer> trainers = trainee.getTrainers();
        for (Trainer trainer : trainers) {
            trainer.getTrainees().remove(trainee); // Remove trainee from trainer's collection
        }
        trainee.getTrainers().clear(); // Clear trainee's trainer collection

        traineeRepository.delete(id);
    }

    @Override
    @Transactional
    public void toggleActiveState(Long id) {
        try{
            Optional<Trainee> result = traineeRepository.findById(id);
            if(result.isPresent()){
                Trainee trainee = result.get();
                Boolean current = trainee.isActive();
                trainee.setActive(!current); // Auto merge from dirty check.
                logger.debug("Active status for id {} set to: {}",id, !current);
            }else{
                logger.error("Trainee not found");
                throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
            }
        } catch (Exception e) {
            logger.error("Error changing trainee's active status {}",e.getMessage());
            throw e;
        }
    }


    @Override
    @Transactional(readOnly = true)
    public TraineeResponseDto findByUsername(String username) {
        Trainee trainee = findEntityByUsername(username);
        return traineeMapper.toResponseDto(trainee);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee findEntityByUsername(String username) {
        Optional<Trainee> result = traineeRepository.findByUsername(username);
        if(result.isEmpty()){
            logger.error("Trainee with username {} not found", username);
            throw new IllegalArgumentException("Trainee with username " + username + " not found.");
        }
        logger.info("Trainee found: {}", result.get());
        return result.get();
    }

    @Override
    @Transactional
    public void updatePassword(Long id, String newPassword) {
        try{
            logger.info("Trying to update Trainee's password with id {}", id);
            traineeRepository.updatePassword(id, newPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {
        try{
            logger.info("Trying to delete Trainee with username {}", username);
            traineeRepository.deleteByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findUnassignedTrainersByTrainee(String traineeUsername) {
        return traineeRepository.findUnassignedTrainersByTrainee(traineeUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Trainer> getTrainersSetForTrainee(Long id) {
        try{
            Optional<Trainee> trainee = traineeRepository.findById(id);
            if(trainee.isEmpty()){
                logger.error("Trainee id not found");
                throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
            }
            return trainee.get().getTrainers();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
