package com.example.merchantransaction.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
public class Receivable {
    private String id;
    private String transactionId;
    private String status;
    @JsonProperty("create_date")
    private String createDate;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

}
