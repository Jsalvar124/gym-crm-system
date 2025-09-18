package com.jsalva.gymsystem.indicator;

import com.jsalva.gymsystem.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UserMetrics {

    public UserMetrics(MeterRegistry registry, UserService userService) {
        // Gauge will call userService.countUsers() whenever Prometheus scrapes
        Gauge.builder("app_users_total", userService, UserService::countUsers)
                .description("Current number of registered users")
                .register(registry);
    }
}
