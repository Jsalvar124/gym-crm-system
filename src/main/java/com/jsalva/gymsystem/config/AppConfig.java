package com.jsalva.gymsystem.config;
import com.jsalva.gymsystem.storage.TraineeStorage;
import com.jsalva.gymsystem.storage.TrainerStorage;
import com.jsalva.gymsystem.storage.TrainingStorage;
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

    @Bean
    public TrainerStorage trainerStorage() {
        System.out.println("trainerStorage() bean method called!");
        return new TrainerStorage();
    }

    @Bean
    public TraineeStorage traineeStorage() {
        System.out.println("traineeStorage() bean method called!");
        return new TraineeStorage();
    }

    @Bean
    public TrainingStorage trainingStorage() {
        System.out.println("trainingStorage() bean method called!");
        return new TrainingStorage();
    }
    @Bean("storage")
    public Map<String, Map<Long, Object>> commonStorage() {

        System.out.println("commonStorage() bean method called!");
        Map<String, Map<Long, Object>> storage = new HashMap<>();
        storage.put("trainers", trainerStorage().getTrainers());
        storage.put("trainees", traineeStorage().getTrainees());
        storage.put("trainings", trainingStorage().getTrainings());

        return storage;
    }

}

