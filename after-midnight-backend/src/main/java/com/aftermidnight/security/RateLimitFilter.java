package com.aftermidnight.security;

import com.aftermidnight.dto.ApiResponse;
import com.aftermidnight.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RateLimitFilter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static final int LIMIT = 100;
    private static final long TIME_WINDOW_MS = 60000; // 1 minute

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String ip = request.getRemoteAddr();
        RequestCounter counter = requestCounts.computeIfAbsent(ip, k -> new RequestCounter());

        if (counter.isLimitExceeded()) {
            log.warn("Rate limit exceeded for IP: {}", ip);
            sendErrorResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Void> apiResponse = ApiResponse.error("Rate limit exceeded. Try again in a minute.", ErrorCode.TOO_MANY_REQUESTS);
        
        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
    }

    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long startTime = System.currentTimeMillis();

        public synchronized boolean isLimitExceeded() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > TIME_WINDOW_MS) {
                count.set(1);
                startTime = currentTime;
                return false;
            }

            return count.incrementAndGet() > LIMIT;
        }
    }
}
