package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Integer duration);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
}
