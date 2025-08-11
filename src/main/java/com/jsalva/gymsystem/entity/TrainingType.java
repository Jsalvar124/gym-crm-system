package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_type_name", nullable = false)
    private String trainingTypeName;

    public TrainingType() {
    }

    public Long getId() {
        return id;
    }

    public TrainingType setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public TrainingType setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
        return this;
    }
}
