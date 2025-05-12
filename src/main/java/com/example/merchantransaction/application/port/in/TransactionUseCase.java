package com.example.merchantransaction.application.port.in;

import com.example.merchantransaction.adapter.in.web.exception.TransactionException;
import com.example.merchantransaction.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionUseCase {
    void processPayment(Transaction transaction) throws TransactionException;
    List<Transaction> getAllTransactions();
    Optional<Transaction> findById(String id);
}
