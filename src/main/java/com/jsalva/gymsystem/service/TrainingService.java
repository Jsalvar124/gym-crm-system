package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Integer duration);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
    List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);
    List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);
}
