package com.jsalva.gymsystem.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
@Component
@Order(1)
public class TransactionIdFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TransactionIdFilter.class);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Create a random id for the transaction
        String transactionId = UUID.randomUUID().toString().split("-")[0]; //shorter id
        try {
            MDC.put("transactionId", transactionId);
            logger.debug("Incoming request [{}] {}", transactionId, ((HttpServletRequest) request).getRequestURI());
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // prevent leaks across threads
        }
    }
}
