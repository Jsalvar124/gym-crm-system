package com.jsalva.gymsystem.model;

import java.time.LocalDate;

public class Trainee extends User{
    String address;
    LocalDate dateOfBirth;

    public Trainee(String firstName, String lastName, String username, String password, boolean isActive, String address, LocalDate dateOfBirth) {
        super(firstName, lastName, username, password, isActive);
        this.address = address;
        this.dateOfBirth = dateOfBirth;
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
}
