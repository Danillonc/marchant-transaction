package com.example.merchantransaction.application.port.out;

import com.example.merchantransaction.domain.model.Receivable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReceivableRepository {
    void save(Receivable receivable);
    Optional<Receivable> findById(String id);
    List<Receivable> findAll();
    List<Receivable> findByDateRange(String start, String end);
}
;