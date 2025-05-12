package com.example.merchantransaction.infrastructure.util;

import com.example.merchantransaction.adapter.in.web.dto.ReceivableResponseDTO;
import com.example.merchantransaction.adapter.in.web.dto.ReceivablesSummaryDTO;
import com.example.merchantransaction.domain.model.Receivable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ReceivableConverter {
    private ReceivableConverter() {}

    public static ReceivablesSummaryDTO convertToSummaryDto(List<Receivable> receivables) {
        BigDecimal available = receivables.stream()
                .filter(r -> "paid".equals(r.getStatus()))
                .map(Receivable::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal future = receivables.stream()
                .filter(r -> "waiting_funds".equals(r.getStatus()))
                .map(Receivable::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal fees = receivables.stream()
                .map(Receivable::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReceivablesSummaryDTO(available, future, fees);
    }

    public static List<ReceivableResponseDTO> convertToDto(List<Receivable> receivableList) {

        return receivableList.stream()
                .map(tx -> new ReceivableResponseDTO(
                        tx.getId(),
                        tx.getStatus(),
                        tx.getCreateDate().toString(),
                        tx.getSubtotal().toString(),
                        tx.getDiscount().toString(),
                        tx.getTotal().toString()
                ))
                .collect(Collectors.toList());
    }
}
