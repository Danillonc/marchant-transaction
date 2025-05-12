package com.example.merchantransaction.adapter.out.persistence.client;

import com.example.merchantransaction.adapter.out.persistence.client.response.NumeratorResponse;
import com.example.merchantransaction.infrastructure.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class NumeratorClient {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final String baseUrl;

    public NumeratorClient(AppConfig appConfig, RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.baseUrl = appConfig.getNumerator();
    }

    public Integer generateUniqueId() {
        try {
            acquireLock();

            Integer currentValue = getCurrentNumerator();
            return updateNumeratorAtomically(currentValue, currentValue + 1);
        } finally {
            try {
                releaseLock();
            } catch (Exception e) {
                log.warn("Failed to release lock: {}", e.getMessage());
            }
        }
    }

    private void acquireLock() {
        retryTemplate.execute((RetryCallback<Void, RuntimeException>) context -> {
            log.debug("Trying to acquire lock, attempt {}", context.getRetryCount() + 1);

            String url = baseUrl + "/numerator/lock";
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(Map.of("timeout", 10000), getHeader());
            restTemplate.postForEntity(url, request, Void.class);
            return null;
        });
    }

    private Integer getCurrentNumerator() {
        return retryTemplate.execute((RetryCallback<Integer, RuntimeException>) context -> {
            log.debug("Fetching current numerator, attempt {}", context.getRetryCount() + 1);

            String url = baseUrl + "/numerator";
            ResponseEntity<NumeratorResponse> response = restTemplate.getForEntity(url, NumeratorResponse.class);
            NumeratorResponse body = response.getBody();

            // Return the numerator from the response
            return body != null ? body.getNumerator() : null;
        });
    }

    private Integer updateNumeratorAtomically(int oldValue, int newValue) {
        return retryTemplate.execute(context -> {
            log.debug("Updating numerator from {} to {}, attempt {}", oldValue, newValue, context.getRetryCount() + 1);

            String url = baseUrl + "/numerator/test-and-set";
            Map<String, Object> requestBody = Map.of(
                    "oldValue", oldValue,
                    "newValue", newValue
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, getHeader());
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
            Object returnedValue = response.getBody().get("numerator");
            if (returnedValue instanceof Integer) {
                return (Integer) returnedValue;
            } else {
                throw new IllegalStateException("Unexpected response structure from /test-and-set");
            }
        });
    }

    private void releaseLock() {
        try {
            String url = baseUrl + "/numerator/lock";
            restTemplate.delete(url);
        } catch (Exception e) {
            log.warn("Release lock failed (ignored): {}", e.getMessage());
        }
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


}
