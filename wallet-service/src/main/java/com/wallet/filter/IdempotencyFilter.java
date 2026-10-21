package com.wallet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class IdempotencyFilter implements Filter {

    private final StringRedisTemplate redisTemplate;

    public IdempotencyFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String idempotencyKey = httpRequest.getHeader("Idempotency-Key");

            if (idempotencyKey != null) {
                Boolean isNewRequest = redisTemplate.opsForValue()
                        .setIfAbsent(idempotencyKey, "processed", 24, TimeUnit.HOURS);

                if (Boolean.FALSE.equals(isNewRequest)) {
                    httpResponse.setStatus(409); 
                    httpResponse.getWriter().write("Duplicate transaction detected! Request blocked.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}