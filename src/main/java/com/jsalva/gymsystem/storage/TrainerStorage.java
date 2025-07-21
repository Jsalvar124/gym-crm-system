package com.jsalva.gymsystem.storage;

import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainerStorage {

    @Value("${storage.trainers.file}")
    private String trainersFilePath;


    private Map<Long, Object> trainers = new HashMap<>();

    @PostConstruct
    public void initializeTrainerData() {
        // Load data from file and populate the map
        loadTrainersFromCSV();
    }

    public Map<Long, Object> getTrainers() {
        return trainers;
    }

    public void setTrainers(Map<Long, Object> trainers) {
        this.trainers = trainers;
    }

    private void loadTrainersFromCSV() {
        System.out.println("loadTrainersFromCSV called!");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(trainersFilePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line = reader.readLine(); // Skip header
            int count = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    // Create Trainer object and populate from CSV data
                    Trainer trainer = new Trainer();
                    trainer.setFirstName(parts[0]);
                    trainer.setLastName(parts[1]);
                    trainer.setUsername(parts[2]);
                    trainer.setPassword(parts[3]);
                    trainer.setActive(Boolean.parseBoolean(parts[4]));
                    trainer.setSpecialization(TrainingType.valueOf(parts[5]));
                    // Add to trainers map with generated ID
                    trainers.put(trainer.getUserId(), trainer);
                    count++;
                }
            }
            System.out.println("Loaded " + count + " trainers");

        } catch (Exception e) {
            System.err.println("Error loading trainers: " + e.getMessage());
        }
    }
}
