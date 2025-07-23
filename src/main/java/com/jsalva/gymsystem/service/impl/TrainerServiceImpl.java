package com.jsalva.gymsystem.service.impl;
import com.jsalva.gymsystem.dao.TrainerDAO;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
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
    public Trainer createTrainer(Trainer trainer) {

        //Create Username as FirstName.LastnameXX, verify if any homonyms exist, if so add serial number as suffix
        List<Trainer> trainers = getAllTrainers();
        List<Trainee> trainees = traineeService.getAllTrainees();
        String username = UserUtils.generateUniqueUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainers,
                trainees);

        trainer.setUsername(username);
        //Generate and set random Password
        trainer.setPassword(UserUtils.generateRandomPassword());

        //Default active boolean is true.
        trainer.setActive(true);

        Trainer newTrainer = trainerDAO.save(trainer);

        return null;
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerDAO.findAll();
    }

    @Override
    public Trainer getTrainerById(Long id) {
        Trainer trainer = trainerDAO.findById(id);
        if(trainer == null){
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        return trainer;

    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        // Verify that the id exists.
        Trainer trainerFound = trainerDAO.findById(trainer.getUserId());
        if(trainerFound == null){
            throw new IllegalArgumentException("Trainer with Id " + trainer.getUserId() + " not found.");
        }
        trainerDAO.update(trainer);
        return trainer;
    }

    @Override
    public void deleteTrainer(Long id) {
        // Verify that the id exists.
        Trainer trainerFound = trainerDAO.findById(id);
        if(trainerFound == null){
            throw new IllegalArgumentException("Trainer with Id " + id + " not found.");
        }
        trainerDAO.delete(id);
    }

}
