package com.example.merchantransaction.application.service;

import com.example.merchantransaction.application.port.in.ReceivableUseCase;
import com.example.merchantransaction.application.port.out.ReceivableRepository;
import com.example.merchantransaction.domain.model.Receivable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReceivableServiceImpl implements ReceivableUseCase {

    private final ReceivableRepository receivableRepository;

    @Override
    public List<Receivable> findByDateRange(String start, String end) {
        return this.receivableRepository.findByDateRange(start, end);
    }

    @Override
    public List<Receivable> findAll() {
        return this.receivableRepository.findAll();
    }
}
