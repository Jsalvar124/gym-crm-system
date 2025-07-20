package com.jsalva.gymsystem.model;

public class Trainer extends User{
    TrainingType specialization;

    public Trainer(String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization=" + specialization +
                ", isActive=" + isActive +
                "} " + super.toString();
    }
}
