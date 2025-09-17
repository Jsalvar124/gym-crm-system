package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Training;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("""
        SELECT t FROM Training t 
        WHERE t.trainer.username = :trainerUsername
        AND (:traineeUsername IS NULL OR t.trainee.username = :traineeUsername)
        AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
        AND (:toDate IS NULL OR t.trainingDate <= :toDate)
    """)
    List<Training> findTrainerTrainings(
            @Param("trainerUsername") String trainerUsername,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("traineeUsername") String traineeUsername
    );

    @Query("""
        SELECT t FROM Training t 
        WHERE t.trainee.username = :traineeUsername
        AND (:trainerUsername IS NULL OR t.trainer.username = :trainerUsername)
        AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
        AND (:toDate IS NULL OR t.trainingDate <= :toDate)
        AND (:trainingType IS NULL OR t.trainingType.trainingTypeName = :trainingType)
    """)
    List<Training> findTraineeTrainings(
            @Param("traineeUsername") String traineeUsername,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerUsername") String trainerUsername,
            @Param("trainingType") TrainingTypeEnum trainingType
    );
}
