package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
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

    // Lifecycle Events - Automatically maintain M2M relationship
    @PostPersist
    @PostUpdate
    private void updateTrainerTraineeRelationship() {
        if (trainer != null && trainee != null) {
            trainer.addTrainee(trainee);
        }
    }

    public Training() {
    }

    private Training(Builder builder){
        this.id = builder.id;
        this.trainer = builder.trainer;
        this.trainee = builder.trainee;
        this.trainingName = builder.trainingName;
        this.trainingType = builder.trainingType;
        this.trainingDate = builder.trainingDate;
        this.duration = builder.duration;
    }

    public static class Builder {
        private Long id;
        private Trainer trainer;
        private Trainee trainee;
        private String trainingName;
        private TrainingType trainingType;
        private LocalDate trainingDate;
        private Integer duration; // Minutes

        public Builder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder setTrainingDate(LocalDate trainingDate) {
            this.trainingDate = trainingDate;
            return this;
        }

        public Builder setTrainingType(TrainingType trainingType) {
            this.trainingType = trainingType;
            return this;
        }

        public Builder setTrainingName(String trainingName) {
            this.trainingName = trainingName;
            return this;
        }

        public Builder setTrainee(Trainee trainee) {
            this.trainee = trainee;
            return this;
        }

        public Builder setTrainer(Trainer trainer) {
            this.trainer = trainer;
            return this;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Training build(){
            return new Training(this);
        }
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

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainer=" + trainer +
                ", trainee=" + trainee +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", duration=" + duration +
                '}';
    }
}
