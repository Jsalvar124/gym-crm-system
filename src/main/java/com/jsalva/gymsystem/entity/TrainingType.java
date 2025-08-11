package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_type_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingTypeEnum trainingTypeName;

    public TrainingType() {
    }

    public Long getId() {
        return id;
    }

    public TrainingType setId(Long id) {
        this.id = id;
        return this;
    }

    public TrainingTypeEnum getTrainingTypeName() {
        return trainingTypeName;
    }

    public TrainingType setTrainingTypeName(TrainingTypeEnum trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
        return this;
    }
}
