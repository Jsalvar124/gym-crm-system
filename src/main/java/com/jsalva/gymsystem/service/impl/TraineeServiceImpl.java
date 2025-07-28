package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.dao.TraineeDAO;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private TrainerService trainerService;

    @Override
    public void createTrainee(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);

        //Generate and set random Password
        trainee.setPassword(UserUtils.generateRandomPassword());

        // Create unique username
        String username = UserUtils.generateUniqueUsername(firstName, lastName, trainerService.getAllTrainers(), getAllTrainees());
        trainee.setUsername(username);
        traineeDAO.save(trainee);
        logger.debug("Saved Trainee: {}", trainee);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDAO.findAll();
    }

    @Override
    public Trainee getTraineeById(Long id) {
        Trainee trainee = traineeDAO.findById(id);
        if(trainee == null){
            throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
        }
        return trainee;
    }

    @Override
    public void updateTrainee(Long userId, String firstName, String lastName, String newPassword, Boolean isActive, String address, LocalDate dateOfBirth) {
        // Verify the ID exists
        Trainee trainee = traineeDAO.findById(userId);
        if(trainee == null){
            logger.error("Trainee for update not found");
            throw new IllegalArgumentException("Trainee with Id " + userId + " not found.");
        }
        // Check if firstname or lastname changed and reassign username accordingly.
        if(firstName!=null && !firstName.equals(trainee.getFirstName()) || lastName != null && !lastName.equals(trainee.getLastName())) {
            String first = firstName == null? trainee.getFirstName() : firstName;
            String last = lastName == null? trainee.getLastName() : lastName;
            String username = UserUtils.generateUniqueUsername(first, last, trainerService.getAllTrainers(), getAllTrainees());
            trainee.setUsername(username);
        }
        if(firstName != null){
            trainee.setFirstName(firstName);
        }
        if(lastName != null){
            trainee.setLastName(lastName);
        }
        if(newPassword != null){
            trainee.setPassword(newPassword);
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
        traineeDAO.update(trainee);
        logger.debug("Updated Trainee: {}", getTraineeById(userId)); // Retrieve from storage.
    }

    @Override
    public void deleteTrainee(Long id) {
        Trainee trainee = traineeDAO.findById(id);
        if(trainee == null){
            logger.error("Trainee for deletion not found");
            throw new IllegalArgumentException("Trainee with Id " + id + " not found.");
        }
        logger.info("Deleting trainee with id {}", id);
        traineeDAO.delete(id);
    }
}
