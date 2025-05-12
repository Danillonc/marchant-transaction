package com.example.merchantransaction.domain.model;

public interface PaymentMethodStrategy {
    Receivable process(Transaction transaction);
}
