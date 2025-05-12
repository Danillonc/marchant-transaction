package com.example.merchantransaction.application.port.in;

import com.example.merchantransaction.domain.model.Receivable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReceivableUseCase {
    List<Receivable> findByDateRange(String start, String end);
    List<Receivable> findAll();
}
