package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends GenericRepository<Training, Long> {
    List<Training> getTrainersTrainingListByTraineeUsernameOrDateSpan(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername);

    List<Training> getTraineesTrainingListByTrainerUsernameOrDateSpan(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, TrainingTypeEnum trainingType);
}
