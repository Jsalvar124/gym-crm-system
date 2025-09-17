package com.jsalva.gymsystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerSummaryDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.exception.ResourceNotFoundException;
import com.jsalva.gymsystem.exception.UnprocessableEntityException;
import com.jsalva.gymsystem.mapper.TraineeMapper;
import com.jsalva.gymsystem.mapper.TrainerMapper;
import com.jsalva.gymsystem.repository.TraineeRepository;
import com.jsalva.gymsystem.repository.UserRepository;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.UserService;
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

    private final TrainerMapper trainerMapper;

    private final UserService userService;

    public TraineeServiceImpl(TraineeRepository traineeRepository, TraineeMapper traineeMapper, TrainerMapper trainerMapper, UserService userService) {
        this.traineeRepository = traineeRepository;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto requestDto) {
        if(userService.existsByEmail(requestDto.email())){
            logger.error("Error creating trainee - email {} already exists", requestDto.email());
            throw new UnprocessableEntityException("Unprocessable request - email already exists");
        }

        Trainee trainee = new Trainee();
        trainee.setFirstName(requestDto.firstName());
        trainee.setLastName(requestDto.lastName());
        trainee.setAddress(requestDto.address());
        trainee.setDateOfBirth(requestDto.dateOfBirth());
        trainee.setEmail(requestDto.email());

        //Generate and set random Password
        String randomPassword = UserUtils.generateRandomPassword();
        String hashedPassword = EncoderUtils.encryptPassword(randomPassword);
        trainee.setPassword(hashedPassword);

        // Create unique username
        String username = userService.generateUniqueUsername(requestDto.firstName(), requestDto.lastName());
        trainee.setUsername(username);

        //Default isActive boolean set to true.
        trainee.setActive(true);

        // save new trainee
        traineeRepository.save(trainee);
        logger.info("Saved Trainee: {}", trainee.getUsername());

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
            throw new ResourceNotFoundException("Trainee with Id " + id + " not found.");
        }
        return trainee.get();
    }

    @Override
    @Transactional
    public TraineeResponseDto updateTrainee(UpdateTraineeRequestDto requestDto) {
        // Verify the username exists for a given Trainee
        Trainee trainee = findEntityByUsername(requestDto.username());
        // Check if firstname or lastname changed and reassign username accordingly.

        // Verify no repeated emails
        if(!requestDto.email().equals(trainee.getEmail()) && userService.existsByEmail(requestDto.email())){
            logger.error("Error updating trainee - email {} already exists", requestDto.email());
            throw new UnprocessableEntityException("Unprocessable request - email already exists");
        }
        String firstName = requestDto.firstName();
        String lastName = requestDto.lastName();

        if(firstName!=null && !firstName.equals(trainee.getFirstName()) || lastName != null && !lastName.equals(trainee.getLastName())){
            String username = userService.generateUniqueUsername(firstName, lastName);
            trainee.setUsername(username);
        }
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setActive(requestDto.isActive());

        // Modify if not null
        if(requestDto.dateOfBirth() != null){
            trainee.setDateOfBirth(requestDto.dateOfBirth());
        }
        if(requestDto.address() != null){
            trainee.setAddress(requestDto.address());
        }
        // Set if email has changed
        if(!requestDto.email().equals(trainee.getEmail())){
            trainee.setEmail(requestDto.email());
        }

        // Save in storage
        traineeRepository.save(trainee);
        logger.debug("Updated Trainee: {}", requestDto.username());
        return traineeMapper.toResponseDto(trainee);
    }

    @Override
    @Transactional
    public void updateActiveState(String username, Boolean isActive) {
        Trainee trainee = findEntityByUsername(username);
        trainee.setActive(isActive); // Auto merge from dirty check.
        logger.debug("Active status for username {} set to: {}",username, isActive);
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
            throw new ResourceNotFoundException("Trainee with username " + username + " not found.");
        }
        logger.info("Trainee found: {}", result.get().getUsername());
        return result.get();
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {
        Optional<Trainee> result = traineeRepository.findByUsername(username);
        if(!result.isPresent()){
            logger.error("Error deleting Entity, Trainee with username {} not found", username);
            throw new ResourceNotFoundException("Error deleting Entity, Trainee with username "+username +" not found");
        }
        Trainee trainee = result.get();
        //         Clean up relationships before deletion
        trainee.getTrainers().forEach(trainer -> trainer.getTrainees().remove(trainee));
            traineeRepository.delete(trainee);
            logger.info("Trying to delete Trainee with username {}", username);
            traineeRepository.deleteByUsername(username);
            logger.info("Trainee deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerSummaryDto> findUnassignedTrainersByTrainee(String traineeUsername) {
        List<Trainer> trainers = traineeRepository.findUnassignedTrainersByTrainee(traineeUsername);
        return trainerMapper.toSummaryDtoList(trainers);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Trainer> getTrainersSetForTrainee(Long id) {
        try{
            Optional<Trainee> trainee = traineeRepository.findById(id);
            if(trainee.isEmpty()){
                logger.error("Trainee id not found");
                throw new ResourceNotFoundException("Trainee with Id " + id + " not found.");
            }
            return trainee.get().getTrainers();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }


}
