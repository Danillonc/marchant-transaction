package com.example.merchantransaction.adapter.out.persistence;

import com.example.merchantransaction.application.port.out.ReceivableRepository;
import com.example.merchantransaction.domain.model.Receivable;
import com.example.merchantransaction.domain.model.Transaction;
import com.example.merchantransaction.infrastructure.config.AppConfig;
import com.example.merchantransaction.infrastructure.util.LocalDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ReceivableRepositoryImpl implements ReceivableRepository {


    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final String baseUrl;

    public ReceivableRepositoryImpl(AppConfig appConfig, RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.baseUrl = appConfig.getTransactions();
    }

    @Override
    public void save(Receivable receivable) {
        retryTemplate.execute(context -> {
            log.debug("POST receivable attempt {}", context.getRetryCount() + 1);

            String url = baseUrl + "/receivables";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Receivable> request = new HttpEntity<>(receivable, headers);
            restTemplate.postForEntity(url, request, Transaction.class);
            return null;
        });
    }

    @Override
    public Optional<Receivable> findById(String id) {
        return retryTemplate.execute(context -> {
            log.debug("GET receivable by ID attempt {}", context.getRetryCount() + 1);

            String url = baseUrl + "/receivables/" + id;
            try {
                ResponseEntity<Receivable> response = restTemplate.getForEntity(url, Receivable.class);
                return Optional.ofNullable(response.getBody());
            } catch (Exception ex) {
                log.error("Error retrieving receivable by id: {}", id, ex);
                return Optional.empty(); // Could rethrow or handle differently based on needs
            }
        });
    }

    @Override
    public List<Receivable> findAll() {
        return retryTemplate.execute(context -> {
            log.debug("GET all receivables attempt {}", context.getRetryCount() + 1);

            String url = baseUrl + "/receivables";
            ResponseEntity<Receivable[]> response = restTemplate.getForEntity(url, Receivable[].class);
            Receivable[] array = response.getBody();
            return array != null ? Arrays.asList(array) : Collections.emptyList();
        });
    }

    @Override
    public List<Receivable> findByDateRange(String start, String end) {
        return retryTemplate.execute(context -> {
            log.debug("GET receivables by date range attempt {}", context.getRetryCount() + 1);

            String url = UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path("/receivables")
                    .build()
                    .toUriString();

            ResponseEntity<Receivable[]> response = restTemplate.getForEntity(url, Receivable[].class);
            Receivable[] responseBody = response.getBody();

            LocalDate startDate = LocalDateFormat.convertStringToLocalDate(start);
            LocalDate endDate = LocalDateFormat.convertStringToLocalDate(end);

            return Arrays.stream(responseBody)
                    .filter(receivable -> {
                        LocalDate dbDate = LocalDateFormat.convertStringToLocalDate(receivable.getCreateDate());
                        return !dbDate.isBefore(startDate) && !dbDate.isAfter(endDate);
                    }).collect(Collectors.toList());
        });
    }
}
