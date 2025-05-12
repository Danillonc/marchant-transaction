package com.example.merchantransaction.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ReceivablesSummaryDTO {
    private BigDecimal totalAvailable;
    private BigDecimal totalFuture;
    private BigDecimal totalFees;
}
