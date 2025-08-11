package com.jsalva.gymsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    0..n means zero to many (optional, many side). 1 means exactly one (mandatory, single side).
//    A Trainer must have exactly one Specialization (the 1 side). A Specialization can be linked to zero or many Trainers (the 0..n side).

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TrainingType specialization;

//    1. User ↔ Trainee / User ↔ Trainer
//    The black diamond means composition in UML.
//    Composition says:
//            "This object owns the other object, and the lifetime of the child depends entirely on the parent."
//    A Trainee cannot exist without a User.
//    A Trainer cannot exist without a User.
//    If you delete the User, the related Trainee or Trainer must also be deleted.

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Trainer() {
    }

    public Long getId() {
        return id;
    }

    public Trainer setId(Long id) {
        this.id = id;
        return this;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public Trainer setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Trainer setUser(User user) {
        this.user = user;
        return this;
    }
}
