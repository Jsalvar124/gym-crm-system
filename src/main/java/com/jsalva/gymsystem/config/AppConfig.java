package com.jsalva.gymsystem.config;

import com.jsalva.gymsystem.model.Trainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(value="com.jsalva.gymsystem")
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${storage.trainers.file}")
    private String trainersFilePath;
    @Bean
    public Map<Long, Trainer> trainerStorage() {
        Map<Long, Trainer> trainers = new HashMap<>();

        return trainers;
    }

//    @Bean
//    public Map<String, Map<Long, Object>> commonStorage() {
//        Map<String, Map<Long, Object>> storage = new HashMap<>();
//        storage.put("trainers", (Map<Long, Object>) trainerStorage());
//        storage.put("trainees", (Map<Long, Object>) traineeStorage());
//        storage.put("trainings", (Map<Long, Object>) trainingStorage());
//        return storage;
//    }
}

