package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.model.Trainee;

import java.util.List;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);
    List<Trainee> getAllTrainees();
    Trainee getTraineeById(Long id);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainer(Long id);
}
