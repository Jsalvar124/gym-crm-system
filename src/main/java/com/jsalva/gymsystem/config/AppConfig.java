package com.jsalva.gymsystem.config;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.*;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement // Add enable Transaction Management
@ComponentScan(basePackages = {
        "com.jsalva.gymsystem.service",
        "com.jsalva.gymsystem.repository",
        "com.jsalva.gymsystem.facade",
        "com.jsalva.gymsystem.dao",
        "com.jsalva.gymsystem.mapper"
        // Add other packages but NOT controller package
})
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

    // Add Transaction Manager to use @Tansactional
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}

