package com.jsalva.gymsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM -------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        Environment env = context.getEnvironment();

        String port = env.getProperty("server.port", "8080");
        String[] profiles = env.getActiveProfiles();
        String activeProfile = profiles.length > 0 ? String.join(",", profiles) : "default";

        System.out.printf("GYM - REST - API STARTED ON: http://localhost:%s (profile: %s)%n", port, activeProfile);
    }
}