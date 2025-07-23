package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;

import java.util.List;
import java.util.Map;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer);
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(Long id);
    Trainer updateTrainer(Trainer trainer);
    void deleteTrainer(Long id);
}
