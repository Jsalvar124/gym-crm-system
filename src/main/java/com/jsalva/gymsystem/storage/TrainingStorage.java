package com.jsalva.gymsystem.storage;

import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingStorage {

    private static final Logger logger = LoggerFactory.getLogger(TrainingStorage.class);

    @Value("${storage.trainings.file}")
    private String trainingsFilePath;

    private final Map<Long, Object> trainings = new HashMap<>();

    public Map<Long, Object> getTrainings() {
        return trainings;
    }

    @PostConstruct
    public void initializeTrainingData() {
        // Load data from file and populate the map
        loadTrainingsFromCSV();
    }

    private void loadTrainingsFromCSV() {
        logger.info("Loading Trainings from csv file...");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(trainingsFilePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line = reader.readLine(); // Skip header
            int count = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    // Create Trainer object and populate from CSV data
                    Training training = new Training();
                    training.setTrainerId(Long.valueOf(parts[0]));
                    training.setTraineeId(Long.valueOf(parts[1]));
                    training.setTrainingName(parts[2]);
                    training.setTrainingType(TrainingType.valueOf(parts[3]));
                    training.setTrainingDate(LocalDate.parse(parts[4]));
                    training.setDuration(Integer.valueOf(parts[5]));

                    // Add to trainings map with generated ID
                    trainings.put(training.getTrainingId(), training);
                    count++;
                }
            }
            logger.info("Loaded " + count + " trainings");
        } catch (Exception e) {
            logger.error("Error loading trainings: {}", e.getMessage());
        }
    }
}
