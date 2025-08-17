//package com.jsalva.gymsystem.storage;
//
//import com.jsalva.gymsystem.model.Trainee;
//import jakarta.annotation.PostConstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class TraineeStorage {
//    private final Logger logger = LoggerFactory.getLogger(TraineeStorage.class);
//
//    @Value("${storage.trainees.file}")
//    private String traineesFilePath;
//
//    private final Map<Long, Object> trainees = new HashMap<>();
//
//    public Map<Long, Object> getTrainees() {
//        return trainees;
//    }
//
//    @PostConstruct
//    public void initializeTraineeData() {
//        // Load data from file and populate the map
//        loadTraineesFromCSV();
//    }
//
//    private void loadTraineesFromCSV() {
//        try (InputStream is = getClass().getClassLoader().getResourceAsStream(traineesFilePath);
//             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
//            String line = reader.readLine(); // Skip header
//            int count = 0;
//
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 6) {
//                    // Create Trainer object and populate from CSV data
//                    Trainee trainee = new Trainee();
//                    trainee.setFirstName(parts[0]);
//                    trainee.setLastName(parts[1]);
//                    trainee.setUsername(parts[2]);
//                    trainee.setPassword(parts[3]);
//                    trainee.setActive(Boolean.parseBoolean(parts[4]));
//                    trainee.setAddress(parts[5]);
//                    trainee.setDateOfBirth(LocalDate.parse(parts[6]));
//                    // Add to trainees map with generated ID
//                    trainees.put(trainee.getUserId(), trainee);
//                    count++;
//                }
//            }
//            logger.info("Loaded {} trainees", count);
//        } catch (Exception e) {
//            logger.error("Error loading trainees: {}", e.getMessage());
//        }
//    }
//}
