package com.example.merchantransaction.adapter.out.persistence;

import com.example.merchantransaction.application.port.out.TransactionRepository;
import com.example.merchantransaction.domain.model.Transaction;
import com.example.merchantransaction.infrastructure.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final String baseUrl;

    public TransactionRepositoryImpl(AppConfig appConfig, RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.baseUrl = appConfig.getTransactions();
    }

    @Override
    public void save(Transaction transaction) {
        retryTemplate.execute(context -> {
            log.debug("POST transaction attempt {}", context.getRetryCount() + 1);

            URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/transactions")
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);

            restTemplate.postForEntity(uri, request, Transaction.class);
            return null;
        });
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return retryTemplate.execute(context -> {
            log.debug("GET transaction by ID attempt {}", context.getRetryCount() + 1);
            URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/transactions/{id}")
                    .buildAndExpand(id)
                    .toUri();

            try {
                ResponseEntity<Transaction> response = restTemplate.getForEntity(uri, Transaction.class);
                return Optional.ofNullable(response.getBody());
            } catch (Exception e) {
                log.warn("Failed to fetch transaction with ID {}: {}", id, e.getMessage());
                return Optional.empty();
            }
        });
    }

    @Override
    public List<Transaction> findAll() {
        return retryTemplate.execute(context -> {
            log.debug("GET all transactions attempt {}", context.getRetryCount() + 1);
            URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/transactions")
                    .build()
                    .toUri();

            ResponseEntity<Transaction[]> response = restTemplate.getForEntity(uri, Transaction[].class);
            Transaction[] body = response.getBody();
            return body != null ? Arrays.asList(body) : Collections.emptyList();
        });
    }

    @Override
    public boolean delete(String transactionId) {
        return retryTemplate.execute(context -> {
            log.debug("Deleting transaction: {}", transactionId);
            URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/transactions/{id}")
                    .buildAndExpand(transactionId)
                    .toUri();

            try {
                restTemplate.delete(uri);
                return true;
            } catch (RestClientException e) {
                log.debug("Failed to delete transaction: {}, error: {}", transactionId, e.getMessage());
                return false;
            }
        });

    }
}
