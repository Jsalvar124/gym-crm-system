package com.jsalva.gymsystem.indicator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "jvmInfo") // -> will be available at /actuator/jvmInfo
public class JvmInfoIndicator {
    @ReadOperation
    public Map<String, Object> jvmInfo() {
        Map<String, Object> details = new HashMap<>();
        details.put("freeMemoryMb", bytesToMb(Runtime.getRuntime().freeMemory()));
        details.put("totalMemoryMb", bytesToMb(Runtime.getRuntime().totalMemory()));
        details.put("maxMemoryMb", bytesToMb(Runtime.getRuntime().maxMemory()));
        details.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        return details;
    }

    private double bytesToMb(long bytes) {
        return bytes / (1024.0 * 1024);
    }
}