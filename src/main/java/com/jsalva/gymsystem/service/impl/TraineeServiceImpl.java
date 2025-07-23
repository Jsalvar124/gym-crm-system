package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.dao.TraineeDAO;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;

    @Override
    public Trainee createTrainee(Trainee trainee) {
        return null;
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDAO.findAll();
    }

    @Override
    public Trainee getTraineeById(Long id) {
        return null;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return null;
    }

    @Override
    public void deleteTrainer(Long id) {

    }
}
