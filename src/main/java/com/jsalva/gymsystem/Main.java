package com.jsalva.gymsystem;

import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;

public class Main {
    public static void main(String[] args) {
        Trainer trainer = new Trainer("Juan", "Pérez", "Juan.Pérez", "pass231", true, TrainingType.BOULDERING);
        Trainer trainer1 = new Trainer("Jorge", "Cifuentes", "Jorge.Cifuentes", "pass123", true, TrainingType.BOXING);
        System.out.println(trainer);
        System.out.println(trainer1);

    }
}