package com.example.merchantransaction.adapter.in.web.exception;

import com.example.merchantransaction.domain.model.Transaction;

public class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }
}
