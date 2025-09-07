package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.dto.request.TraineeTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.TrainerTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainingRequestDto;
import com.jsalva.gymsystem.dto.response.TraineeTrainingListResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerTrainingListResponseDto;
import com.jsalva.gymsystem.dto.response.TrainingResponseDto;
import com.jsalva.gymsystem.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    void createTraining(CreateTrainingRequestDto requestDto);
    List<Training> getAllTrainings();
    Training getTrainingById(Long id);
    List<TrainerTrainingListResponseDto> getTrainersTrainingListByTraineeUsernameOrDateSpan(TrainerTrainingListRequestDto requestDto);
    List<TraineeTrainingListResponseDto> getTraineesTrainingListByTrainerUsernameOrDateSpan(TraineeTrainingListRequestDto requestDto);
    TrainingResponseDto updateTraining(Long id, UpdateTrainingRequestDto requestDto);
}
