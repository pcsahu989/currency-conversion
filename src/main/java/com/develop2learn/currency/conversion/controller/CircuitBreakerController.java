package com.develop2learn.currency.conversion.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
public class CircuitBreakerController {

    Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class.getName());
    private int count = 0;

    @GetMapping("/retry")
    //@Retry(name = "default") //Default max retry attempt is 3
    @Retry(name = "retry-api", fallbackMethod = "fallbackResponse")
    public String retryTest() {

        String response = "Test Completed";

        if(count < 4) {
            count++;
            logger.info("Retrying... {} " + new Date(), count );
            ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/retry", String.class);
            response = forEntity.getBody();
        }

        return response;
    }

    @GetMapping("/circuit-breaker")
    @CircuitBreaker(name = "retry-api", fallbackMethod = "fallbackResponse")
    public String circuitBreakerTest() {

        String response = "Test Completed";

        logger.info("Retrying... {} " + new Date(), count );
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/retry", String.class);
        response = forEntity.getBody();

        return response;
    }

    @GetMapping("/rate-limiter")
    @RateLimiter(name = "default", fallbackMethod = "fallbackResponse")
    //10s ==> 2 calls to this api as configured in property
    public String rateLimiterTest() {
        logger.info("Retrying... {} " + new Date());
        String response = "Test Completed";

        return response;
    }

    @GetMapping("/bulk-head")
    @Bulkhead(name = "default")
    public String bulkHeadTest() {
        logger.info("Retrying... {} " + new Date());
        String response = "Test Completed";

        return response;
    }

    private String fallbackResponse(Exception e) { //can be overloaded for different exceptions
        return "Fallback Response";
    }
}
