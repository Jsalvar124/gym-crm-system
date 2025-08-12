package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trainees")
@PrimaryKeyJoinColumn(name = "user_id")
public class Trainee extends User{

    @Column(name = "address", nullable = true) //optional
    private String address;

    @Column(name = "birthday", nullable = true) //optional
    private LocalDate dateOfBirth;

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
}
