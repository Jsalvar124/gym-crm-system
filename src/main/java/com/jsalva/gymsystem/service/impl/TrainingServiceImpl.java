package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.messaging.enums.ActionType;
import com.jsalva.gymsystem.messaging.producer.TrainerWorkloadProducer;
import com.jsalva.gymsystem.messaging.dto.TrainerWorkloadMessageDto;
import com.jsalva.gymsystem.messaging.mapper.TrainerWorkloadRequestDtoMapper;
import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.dto.request.TraineeTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.TrainerTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainingRequestDto;
import com.jsalva.gymsystem.dto.response.TraineeTrainingListResponseDto;
import com.jsalva.gymsystem.dto.response.TrainerTrainingListResponseDto;
import com.jsalva.gymsystem.dto.response.TrainingResponseDto;
import com.jsalva.gymsystem.entity.*;
import com.jsalva.gymsystem.exception.ResourceNotFoundException;
import com.jsalva.gymsystem.exception.UnprocessableEntityException;
import com.jsalva.gymsystem.mapper.TrainingMapper;
import com.jsalva.gymsystem.repository.TrainingRepository;
import com.jsalva.gymsystem.service.TraineeService;
import com.jsalva.gymsystem.service.TrainerService;
import com.jsalva.gymsystem.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    private final TrainingMapper trainingMapper;

    private final TrainerWorkloadProducer trainerWorkloadProducer;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService, TrainingMapper trainingMapper, TrainerWorkloadProducer trainerWorkloadProducer) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingMapper = trainingMapper;
        this.trainerWorkloadProducer = trainerWorkloadProducer;
    }

    @Override
    @Transactional
    public void createTraining(CreateTrainingRequestDto requestDto) {
        // Check that both trainer and trainee exist.
        Trainer trainer = trainerService.findEntityByUsername(requestDto.trainerUsername());
        if(!trainer.isActive()){
            logger.error("Error creating training, Trainer {} is inactive", trainer.getUsername());
            throw new UnprocessableEntityException("Error creating training, Trainer " + trainer.getUsername() + " is inactive");
        }

        Trainee trainee = traineeService.findEntityByUsername(requestDto.traineeUsername());
        if(!trainee.isActive()){
            logger.error("Error creating training, Trainee {} is inactive", trainee.getUsername());
            throw new UnprocessableEntityException("Error creating training, Trainee " + trainee.getUsername() + " is inactive");
        }

        // get Training type from trainer specialization
        TrainingType type = trainer.getSpecialization();
        if(type == null){
            logger.error("Error creating training, TrainingType not found");
            throw new ResourceNotFoundException("TrainingType not found.");
        }
        // If both exist, proceed
        Training training = new Training.Builder()
                .setTrainer(trainer)
                .setTrainee(trainee)
                .setTrainingName(requestDto.trainingName())
                .setTrainingType(type)
                .setTrainingDate(requestDto.trainingDate())
                .setDuration(requestDto.trainingDuration())
                .build();

        trainingRepository.save(training);
        logger.info("Saved Training: {}", training);

        // Microservice call
        logger.info("Sending Add Training Workload Request");
        TrainerWorkloadMessageDto workloadRequestDto = TrainerWorkloadRequestDtoMapper.fromTraining(training, ActionType.ADD);
        trainerWorkloadProducer.updateWorkload(workloadRequestDto);

    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponseDto> getAllTrainings() {
        List<Training> trainings = trainingRepository.findAll();
        return trainingMapper.toTrainingResponseDtoList(trainings);
    }

    @Override
    @Transactional(readOnly = true)
    public Training getTrainingById(Long id) {
        Optional<Training> training = trainingRepository.findById(id);
        if(training.isEmpty()){
            logger.error("Training id not found");
            throw new ResourceNotFoundException("Training with Id " + id + " not found.");
        }
        return training.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerTrainingListResponseDto> getTrainersTrainingListByTraineeUsernameOrDateSpan(TrainerTrainingListRequestDto requestDto) {
        String trainerUsername = requestDto.trainerUsername();
        LocalDate fromDate = requestDto.fromDate();
        LocalDate toDate = requestDto.toDate();
        String traineeUsername = requestDto.traineeUsername();
        List<Training> trainings = trainingRepository.findTrainerTrainings(trainerUsername, fromDate, toDate, traineeUsername);

        return trainingMapper.toTrainerResponseDtoList(trainings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainingListResponseDto> getTraineesTrainingListByTrainerUsernameOrDateSpan(TraineeTrainingListRequestDto requestDto) {
        String traineeUsername = requestDto.traineeUsername();
        LocalDate fromDate = requestDto.fromDate();
        LocalDate toDate = requestDto.toDate();
        String trainerUsername = requestDto.trainerUsername();
        TrainingTypeEnum trainingType = requestDto.trainingType();
        List<Training> trainings = trainingRepository.findTraineeTrainings(traineeUsername, fromDate, toDate, trainerUsername, trainingType);
        return trainingMapper.toTraineeResponseDtoList(trainings);
    }

    @Override
    @Transactional
    public TrainingResponseDto updateTraining(Long id, UpdateTrainingRequestDto requestDto) {
        // Check if training exists, get managed entity
        Training training = getTrainingById(id);

        // Check if trainer and trainee exist and are active
        Trainer trainer = trainerService.findEntityByUsername(requestDto.trainerUsername());
        if(!trainer.isActive()){
            logger.error("Error updating training with id {}, Trainer {} is inactive", id, trainer.getUsername());
            throw new UnprocessableEntityException(String.format(
                    "Error updating training with id %d, Trainer %s is inactive",
                    id, trainer.getUsername()
            ));
        }
        training.setTrainer(trainer);

        Trainee trainee = traineeService.findEntityByUsername(requestDto.traineeUsername());
        if(!trainee.isActive()){
            logger.error("Error updating training with id {}, Trainee {} is inactive", id, trainee.getUsername());
            throw new UnprocessableEntityException(String.format(
                    "Error updating training with id %d, Trainee %s is inactive",
                    id, trainee.getUsername()
            ));
        }
        training.setTrainee(trainee);
        training.setTrainingName(requestDto.trainingName());
        training.setTrainingDate(requestDto.trainingDate());
        training.setDuration(requestDto.trainingDuration());
        logger.info("Training updated successfully");
        return trainingMapper.toTrainingResponseDto(training);
    }

    @Override
    @Transactional
    public void deleteTraining(Long id) {
        logger.info("Deleting training with id {}", id);

        // Check if training exists, get managed entity
        Training training = getTrainingById(id);

        // Delete training from database
        trainingRepository.delete(training);

        // Call Microservice Client with Delete Action
        logger.debug("Deleted training {}, notifying workload service", training.getId());
        trainerWorkloadProducer.updateWorkload(
                TrainerWorkloadRequestDtoMapper.fromTraining(training, ActionType.DELETE)
        );
    }
}
