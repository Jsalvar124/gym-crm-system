package com.jsalva.gymsystem.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(2)
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Wrap only if HttpServletRequest/Response
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper((HttpServletResponse) response);

        try {
            // Continue filter chain
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Log request + response
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);

            // Copy content back to real response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        String tokenHeader = request.getHeader("Authorization");
        String token = tokenHeader != null? tokenHeader.substring(7): "";


        logger.info("REQUEST: {} {} | body={} | token={}",
                request.getMethod(),
                request.getRequestURI(),
                body,
                token
                );
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
//        String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
//        logger.info("RESPONSE: status={} | body={}", response.getStatus(), body); // Disable body, only for exercise display
        logger.info("RESPONSE: status={}", response.getStatus()); // Disabled body


    }
}
