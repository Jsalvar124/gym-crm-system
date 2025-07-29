package com.jsalva.gymsystem.model;

import java.time.LocalDate;

public class Training {
    private static Long idCount = 1L;

    private Long trainingId;
    private Long trainerId;
    private Long traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Integer duration; // Minutes

    public Training(){
        this.trainingId = idCount++;
    } // Keep the default constructor for spring.

    // Make the constructor private
    private Training(Builder builder) {
        this.trainingId = idCount++;
        this.trainerId = builder.trainerId;
        this.traineeId = builder.traineeId;
        this.trainingName = builder.trainingName;
        this.trainingType = builder.trainingType;
        this.trainingDate = builder.trainingDate;
        this.duration = builder.duration;
    }

    // Builder static inner class
    public static class Builder{
        // Contains the same attributes as the outer class
        private Long trainingId;
        private Long trainerId;
        private Long traineeId;
        private String trainingName;
        private TrainingType trainingType;
        private LocalDate trainingDate;
        private Integer duration; // Minutes

        // builder setters created by intelliJ
        public Builder setTrainingId(Long trainingId) {
            this.trainingId = trainingId;
            return this;
        }

        public Builder setTrainerId(Long trainerId) {
            this.trainerId = trainerId;
            return this;
        }

        public Builder setTraineeId(Long traineeId) {
            this.traineeId = traineeId;
            return this;
        }

        public Builder setTrainingName(String trainingName) {
            this.trainingName = trainingName;
            return this;
        }

        public Builder setTrainingType(TrainingType trainingType) {
            this.trainingType = trainingType;
            return this;
        }

        public Builder setTrainingDate(LocalDate trainingDate) {
            this.trainingDate = trainingDate;
            return this;
        }

        public Builder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        // Build method
        public Training build() {
            return new Training(this);
        }
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                ", trainerId=" + trainerId +
                ", traineeId=" + traineeId +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", duration=" + duration +
                '}';
    }
}
