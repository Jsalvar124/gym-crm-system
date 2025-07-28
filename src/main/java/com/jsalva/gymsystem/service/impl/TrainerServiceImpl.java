package com.jsalva.gymsystem.service.impl;
import com.jsalva.gymsystem.dao.TrainerDAO;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private TraineeService traineeService;

    @Override
    public void createTrainer(String firstName, String lastName, TrainingType trainingType) {
        // Create Trainer instance and set basic information.
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(trainingType);

        //Create Username as FirstName.LastnameXX, verify if any homonyms exist, if so add serial number as suffix
        String first = firstName == null? trainer.getFirstName() : firstName;
        String last = lastName == null? trainer.getLastName() : lastName;
        String username = UserUtils.generateUniqueUsername(trainer.getFirstName(), trainer.getLastName(), getAllTrainers(), traineeService.getAllTrainees());
        trainer.setUsername(username);

        //Generate and set random Password
        trainer.setPassword(UserUtils.generateRandomPassword());

        //Default isActive boolean set to true.
        trainer.setActive(true);

        // Save trainer
        trainerDAO.save(trainer);
        logger.debug("Saved Trainer: {}", trainer);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerDAO.findAll();
    }

    @Override
    public Trainer getTrainerById(Long id) {
        Trainer trainer = trainerDAO.findById(id);
        if(trainer == null){
            logger.error("Trainer id not found");
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        return trainer;
    }

    @Override
    public void updateTrainer(Long userId, String firstName, String lastName, TrainingType trainingType, String newPassword, Boolean isActive) {
        // Verify that the id exists.
        Trainer trainer = trainerDAO.findById(userId);
        if(trainer == null){
            logger.error("Trainer for update not found");
            throw new IllegalArgumentException("Trainer with Id " + userId + " not found.");
        }
        // if either lastname or firstname change, reasign username.
        if(firstName!=null && !firstName.equals(trainer.getFirstName()) || lastName != null && !lastName.equals(trainer.getLastName())){
            List<Trainer> trainers = getAllTrainers();
            List<Trainee> trainees = traineeService.getAllTrainees();
            String username = UserUtils.generateUniqueUsername(firstName, lastName, trainers, trainees);
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
            trainer.setPassword(newPassword);
        }
        if(isActive != null){
            trainer.setActive(isActive);
        }

        // Save in storage
        trainerDAO.update(trainer);
        logger.debug("Updated Trainer: {}", getTrainerById(userId));
    }

    @Override
    public void deleteTrainer(Long id) {
        // Verify that the id exists.
        Trainer trainerFound = trainerDAO.findById(id);
        if(trainerFound == null){
            logger.error("Trainer for deletion not found");
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        logger.info("Deleting trainer with id {}", id);
        trainerDAO.delete(id);
    }

}
