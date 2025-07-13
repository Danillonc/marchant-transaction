package com.example.merchantransaction.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TransactionEvent {

    private String transactionId;
    private double amount;
    private long timestamp;

    public TransactionEvent(String transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }


}
