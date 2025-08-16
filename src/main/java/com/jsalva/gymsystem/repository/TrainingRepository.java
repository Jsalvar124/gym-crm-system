package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import com.jsalva.gymsystem.entity.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends GenericRepository<Training, Long>{
    List<Trainer> getTrainerListByTraineeUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);
    List<Trainee> getTraineeListByTrainerUsernameOrDateSpan(String username, LocalDate fromDate, LocalDate toDate);

}
