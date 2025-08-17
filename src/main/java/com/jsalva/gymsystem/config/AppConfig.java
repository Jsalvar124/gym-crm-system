package com.jsalva.gymsystem.config;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@ComponentScan(value="com.jsalva.gymsystem")
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("gym-persistence-unit");
    }

    @Bean
    EntityManager entityManager(EntityManagerFactory entityManagerFactory){
        return entityManagerFactory.createEntityManager();

    }


}

