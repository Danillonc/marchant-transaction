package com.example.merchantransaction.application.port.in;

import com.example.merchantransaction.adapter.in.web.exception.ReceivableException;
import com.example.merchantransaction.domain.model.Receivable;
import com.example.merchantransaction.domain.model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReceivableUseCase {
    void createReceivables(Transaction transaction) throws ReceivableException;
    List<Receivable> findByDateRange(String start, String end);
    List<Receivable> findAll();
}
