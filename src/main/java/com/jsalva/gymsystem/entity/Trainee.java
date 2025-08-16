package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    public Trainee() {
    }

    public String getAddress() {
        return address;
    }

    public Trainee setAddress(String address) {
        this.address = address;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Trainee setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public Set<Trainer> getTrainers() {
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
