package com.example.merchantransaction.application.port.in;

import com.example.merchantransaction.domain.model.Transaction;

import java.util.List;

public interface TransactionUseCase {
    void processPayment(Transaction transaction);
    List<Transaction> getAllTransactions();
}
