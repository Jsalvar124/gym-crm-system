package com.jsalva.gymsystem.client;

import com.jsalva.gymsystem.client.dto.TrainerWorkloadRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrainerWorkloadClient {

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(TrainerWorkloadClient.class);

    public TrainerWorkloadClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // method yo call the microservice

    @Retry(name = "trainerWorkloadRetry")
    @CircuitBreaker(
            name = "trainerWorkloadService",
            fallbackMethod = "fallbackUpdateWorkload"
    )
    public void updateWorkload(TrainerWorkloadRequestDto dto) {
        // Add headers with auth token
        String token = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getCredentials();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<TrainerWorkloadRequestDto> request =
                new HttpEntity<>(dto, headers);

        restTemplate.postForEntity(
                "http://TRAINER-WORKLOAD-SERVICE/api/workload",
                dto,
                Void.class
        );
    }


    // Fallback method signature MUST match
    private void fallbackUpdateWorkload(
            TrainerWorkloadRequestDto dto,
            Throwable ex
    ) {
        // Do NOT throw unless you want rollback behavior
        logger.error(
                "Trainer workload service unavailable. Action={} Trainer={}",
                dto.actionType(),
                dto.username(),
                ex
        );
    }
}
