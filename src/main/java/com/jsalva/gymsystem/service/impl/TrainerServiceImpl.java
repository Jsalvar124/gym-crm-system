package com.jsalva.gymsystem.service.impl;
import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.CreateTraineeResponseDto;
import com.jsalva.gymsystem.dto.response.CreateTrainerResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerResponseDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import com.jsalva.gymsystem.mapper.TrainerMapper;
import com.jsalva.gymsystem.repository.TrainerRepository;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.TrainingTypeService;
import com.jsalva.gymsystem.utils.UserUtils;
import com.jsalva.gymsystem.utils.EncoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.jsalva.gymsystem.entity.Trainer;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerRepository trainerRepository;

    private final TrainingTypeService trainingTypeService;

    private final TrainerMapper trainerMapper;

    public TrainerServiceImpl(TrainerRepository trainerRepository, TrainingTypeService trainingTypeService, TrainerMapper trainerMapper) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeService = trainingTypeService;
        this.trainerMapper = trainerMapper;
    }

    @Override
    @Transactional
    public CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto requestDto) {
        TrainingType type = trainingTypeService.findTrainingTypeByName(requestDto.specialization());

        if(type==null){
            logger.error("Invalid training type");
            throw new RuntimeException();
        }

        // Create Trainer instance and set basic information.
        Trainer trainer = new Trainer();
        trainer.setFirstName(requestDto.firstName());
        trainer.setLastName(requestDto.lastName());
        trainer.setSpecialization(type);

        //Create Username as FirstName.LastnameXX, verify if any homonyms exist, if so add serial number as suffix
        String uniqueUsername = trainerRepository.generateUniqueUsername(requestDto.firstName(),requestDto.lastName());
        logger.debug("generated username: {}", uniqueUsername);
        trainer.setUsername(uniqueUsername);

        //Generate and set random Password
        String randomPassword = UserUtils.generateRandomPassword();
        logger.debug("generated Password: {}", randomPassword);
        String hashedPassword = EncoderUtils.encryptPassword(randomPassword);
        trainer.setPassword(hashedPassword);

        //Default isActive boolean set to true.
        trainer.setActive(true);

        // Save trainer
        trainerRepository.create(trainer);
        logger.debug("Saved Trainer: {}", trainer);

        // Return Dto
        return new CreateTrainerResponseDto(uniqueUsername, randomPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getTrainerById(Long id) {
        Optional<Trainer> trainer = trainerRepository.findById(id);
        if(trainer.isEmpty()){
            logger.error("Trainer id not found");
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        return trainer.get();
    }

    @Override
    @Transactional
    public TrainerResponseDto updateTrainer(UpdateTrainerRequestDto requestDto) {
        // Verify that the username exists.
        Trainer trainer = findEntityByUsername(requestDto.username());
        // if either lastname or firstname change, reasign username.
        String firstName = requestDto.firstName();
        String lastName = requestDto.lastName();
        if(firstName!=null && !firstName.equals(trainer.getFirstName()) || lastName != null && !lastName.equals(trainer.getLastName())){
            String first = firstName==null? trainer.getFirstName(): firstName;
            String last = lastName==null? trainer.getLastName(): lastName;
            String username = trainerRepository.generateUniqueUsername(first,last);
            trainer.setUsername(username);
        }
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        TrainingType specialization = trainingTypeService.findTrainingTypeByName(requestDto.specialization());
        trainer.setSpecialization(specialization);
        trainer.setActive(requestDto.isActive());

        // Save in storage
        trainerRepository.update(trainer);
        logger.debug("Updated Trainer: {}", requestDto.username());
        return trainerMapper.toResponseDto(trainer);
    }

    @Override
    @Transactional
    public void deleteTrainer(Long id) {
        // Verify that the id exists.
        Optional<Trainer> trainerFound = trainerRepository.findById(id);
        if(trainerFound.isEmpty()){
            logger.error("Trainer for deletion not found");
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        logger.info("Deleting trainer with id {}", id);
        trainerRepository.delete(id);
    }

    @Override
    @Transactional
    public void updateActiveState(String username, Boolean isActive) {
        try{
            Trainer trainer = findEntityByUsername(username); // Find Trainer, now Managed entity
            trainer.setActive(isActive); // Auto merge from dirty check.
            logger.debug("Active status for username {} set to: {}",username, isActive);
        } catch (Exception e) {
            logger.error("Error changing trainer's active status {}",e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCredentials(String username, String password) {
        try {
            Trainer trainer = findEntityByUsername(username);
            String hashedPassword = trainer.getPassword();
            return EncoderUtils.verifyPassword(password, hashedPassword);
        } catch (SecurityException e){
            logger.error("Error validating credentials");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerResponseDto findByUsername(String username) {
        Trainer trainer = findEntityByUsername(username);
        return trainerMapper.toResponseDto(trainer);
    }

    @Override
    public Trainer findEntityByUsername(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        if(trainer.isEmpty()){
            logger.error("Trainer with username {} not found", username);
            throw new IllegalArgumentException("Trainer with username " + username + " not found.");
        }
        logger.info("Trainer with username {} found", username);
        return trainer.get();
    }

    @Override
    @Transactional
    public void updatePassword(Long id, String newPassword) {
        try{
            trainerRepository.updatePassword(id, newPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Trainee> getTraineeSetForTrainer(Long id) {
        try{
            Optional<Trainer> trainer = trainerRepository.findById(id);
            if(trainer.isEmpty()){
                logger.error("Trainee id not found");
                throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
            }
            return trainer.get().getTrainees();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
