package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    void createTraining(Long trainerId, Long traineeId, String trainingName, TrainingTypeEnum trainingType, LocalDate trainingDate, Integer duration);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
    List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);
    List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);
}
