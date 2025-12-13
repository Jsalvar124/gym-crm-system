package com.jsalva.gymsystem.client;

import com.jsalva.gymsystem.client.dto.TrainerWorkloadRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrainerWorkloadClient {

    private final RestTemplate restTemplate;

    public TrainerWorkloadClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // method yo call the microservice

    public void updateWorkload(TrainerWorkloadRequestDto dto) {
        restTemplate.postForEntity(
                "http://TRAINER-WORKLOAD-SERVICE/api/workload",
                dto,
                Void.class
        );
    }
}
