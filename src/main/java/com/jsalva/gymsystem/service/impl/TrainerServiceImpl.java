package com.jsalva.gymsystem.service.impl;
import com.jsalva.gymsystem.entity.TrainingType;
import com.jsalva.gymsystem.repository.TrainerRepository;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.utils.UserUtils;
import com.jsalva.gymsystem.utils.EncoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.jsalva.gymsystem.entity.Trainer;
@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public void createTrainer(String firstName, String lastName, TrainingType trainingType) {
        // Create Trainer instance and set basic information.
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(trainingType);

        //Create Username as FirstName.LastnameXX, verify if any homonyms exist, if so add serial number as suffix
        String uniqueUsername = trainerRepository.generateUniqueUsername(firstName,lastName);
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
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    public Trainer getTrainerById(Long id) {
        Optional<Trainer> trainer = trainerRepository.findById(id);
        if(trainer.isEmpty()){
            logger.error("Trainer id not found");
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        return trainer.get();
    }

    @Override
    public void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive) {
        // Verify that the id exists.
        Optional<Trainer> result = trainerRepository.findById(userId);
        if(result.isEmpty()){
            logger.error("Trainer for update not found");
            throw new IllegalArgumentException("Trainer with Id " + userId + " not found.");
        }
        Trainer trainer = result.get();
        // if either lastname or firstname change, reasign username.
        if(firstName!=null && !firstName.equals(trainer.getFirstName()) || lastName != null && !lastName.equals(trainer.getLastName())){
            String first = firstName==null? trainer.getFirstName(): firstName;
            String last = lastName==null? trainer.getLastName(): lastName;
            String username = trainerRepository.generateUniqueUsername(first,last);
            trainer.setUsername(username);
        }

        if(firstName != null){
            trainer.setFirstName(firstName);
        }
        if(lastName != null){
            trainer.setLastName(lastName);
        }

        if(trainingType != null){
            trainer.setSpecialization(trainingType);
        }
        if(newPassword != null){
            updatePassword(userId, newPassword);
        }
        if(isActive != null){
            trainer.setActive(isActive);
        }
        // Save in storage
        trainerRepository.update(trainer);
        logger.debug("Updated Trainer: {}", getTrainerById(userId));
    }

    @Override
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
    public void toggleActiveState(Long id) {
        Optional<Trainer> trainerFound = trainerRepository.findById(id);
        if(trainerFound.isEmpty()){
            logger.error("Trainer not found");
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        logger.info("Trainer with id {} state was modified", id);
        trainerRepository.toggleActiveState(id);

    }

    @Override
    public boolean validateCredentials(String username, String password) {
        return trainerRepository.validateCredentials(username,password);
    }

    @Override
    public Trainer findByUsername(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        if(trainer.isEmpty()){
            logger.error("Trainer with username {} not found", username);
            throw new IllegalArgumentException("Trainer with username " + username + " not found.");
        }
        logger.info("Trainer found: {}", trainer.get());
        return trainer.get();
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        try{
            trainerRepository.updatePassword(id, newPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
