package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "address", nullable = true) //optional
    private String address;

    @Column(name = "birthday", nullable = true) //optional
    private LocalDate dateOfBirth;

    public Trainee() {
    }

    public Long getId() {
        return id;
    }

    public Trainee setId(Long id) {
        this.id = id;
        return this;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public Trainee setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Trainee setUser(User user) {
        this.user = user;
        return this;
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
