package com.jsalva.gymsystem.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class TrainerWorkloadClient {

    private final RestTemplate restTemplate;

    @Value("${trainer.workload.base-url}")
    private String baseUrl;


    public TrainerWorkloadClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
