//package com.jsalva.gymsystem.storage;
//
//import com.jsalva.gymsystem.model.Trainer;
//import com.jsalva.gymsystem.model.TrainingType;
//import jakarta.annotation.PostConstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class TrainerStorage {
//
//    private final Logger logger = LoggerFactory.getLogger(TrainerStorage.class);
//
//    @Value("${storage.trainers.file}")
//    private String trainersFilePath;
//
//
//    private final Map<Long, Object> trainers = new HashMap<>();
//
//    @PostConstruct
//    public void initializeTrainerData() {
//        // Load data from file and populate the map
//        loadTrainersFromCSV();
//    }
//
//    public Map<Long, Object> getTrainers() {
//        return trainers;
//    }
//
//    private void loadTrainersFromCSV() {
//        try (InputStream is = getClass().getClassLoader().getResourceAsStream(trainersFilePath);
//             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
//            String line = reader.readLine(); // Skip header
//            int count = 0;
//
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 6) {
//                    // Create Trainer object and populate from CSV data
//                    Trainer trainer = new Trainer();
//                    trainer.setFirstName(parts[0]);
//                    trainer.setLastName(parts[1]);
//                    trainer.setUsername(parts[2]);
//                    trainer.setPassword(parts[3]);
//                    trainer.setActive(Boolean.parseBoolean(parts[4]));
//                    trainer.setSpecialization(TrainingType.valueOf(parts[5]));
//                    // Add to trainers map with generated ID
//                    trainers.put(trainer.getUserId(), trainer);
//                    count++;
//                }
//            }
//            logger.info("Loaded {} trainers", count);
//        } catch (Exception e) {
//            logger.error("Error loading trainers: {}", e.getMessage());
//        }
//    }
//}
