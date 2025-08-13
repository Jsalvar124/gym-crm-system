package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @Column(name = "training_name")
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @Column(name = "training_date")
    private LocalDate trainingDate;

    @Column(name = "duration")
    private Integer duration; // Minutes


    public Training() {
    }

    public Integer getDuration() {
        return duration;
    }

    public Training setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public Training setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
        return this;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public Training setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
        return this;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public Training setTrainingName(String trainingName) {
        this.trainingName = trainingName;
        return this;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public Training setTrainee(Trainee trainee) {
        this.trainee = trainee;
        return this;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Training setTrainer(Trainer trainer) {
        this.trainer = trainer;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Training setId(Long id) {
        this.id = id;
        return this;
    }
}
