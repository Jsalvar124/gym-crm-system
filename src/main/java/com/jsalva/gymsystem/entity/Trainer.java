package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Trainer extends User{
    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TrainingType specialization;

    // Many-to-Many with Trainer as owner
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY) // added fetch lazy
    @JoinTable(
            name = "trainer_trainee", // Junction table name
            joinColumns = @JoinColumn(name = "trainer_id"), // Trainer side
            inverseJoinColumns = @JoinColumn(name = "trainee_id") // Trainee side
    )
    private Set<Trainee> trainees = new HashSet<>();

    public Trainer() {
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public Trainer setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
        return this;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization=" + specialization +
                "} " + super.toString();
    }

    public void addTrainee(Trainee trainee) {
        this.trainees.add(trainee);        // HashSet automatically prevents duplicates
        trainee.getTrainers().add(this);
    }

    public Set<Trainee> getTrainees() {
        return this.trainees;
    }
}
