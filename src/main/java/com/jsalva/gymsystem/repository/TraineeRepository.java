package com.jsalva.gymsystem.repository;

import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUsername(String username);
    void deleteByUsername(String username);
    // Custom query for unassigned trainers
    @Query("""
        SELECT t FROM Trainer t
        WHERE t NOT IN (
            SELECT DISTINCT tr.trainer FROM Training tr
            WHERE tr.trainee.username = :traineeUsername
        )
        AND t.isActive = true
    """)
    List<Trainer> findUnassignedTrainersByTrainee(@Param("traineeUsername") String traineeUsername);
}
