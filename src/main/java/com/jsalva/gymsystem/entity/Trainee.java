package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainees")
@PrimaryKeyJoinColumn(name = "user_id")
public class Trainee extends User{

    @Column(name = "address", nullable = true) //optional
    private String address;

    @Column(name = "birthday", nullable = true) //optional
    private LocalDate dateOfBirth;

    // Many-to-Many non-owner side
    @ManyToMany(mappedBy = "trainees") // Points to the property in Trainer
    private Set<Trainer> trainers = new HashSet<>();

    // One to many non-owner side, cascade deletion
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Training> trainings = new HashSet<>();

    public Trainee() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Trainer> getTrainers() {
        if (trainers != null) {
            Hibernate.initialize(trainers); // Forces load of Trainers.
        }
        return this.trainers;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                "} " + super.toString();
    }
}
