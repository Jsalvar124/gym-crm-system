package com.jsalva.gymsystem.indicator;

import javax.sql.DataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class DatabasePingHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabasePingHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1000)) {
                return Health.up().withDetail("database", "reachable").build();
            } else {
                return Health.down().withDetail("database", "not reachable").build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
