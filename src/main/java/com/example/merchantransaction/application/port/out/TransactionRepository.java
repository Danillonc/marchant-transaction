package com.example.merchantransaction.application.port.out;

import com.example.merchantransaction.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);
    Optional<Transaction> findById(String id);
    List<Transaction> findAll();
    boolean delete(String transactionId);
}
