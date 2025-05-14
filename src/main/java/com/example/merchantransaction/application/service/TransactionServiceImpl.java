package com.example.merchantransaction.application.service;

import com.example.merchantransaction.adapter.in.web.exception.TransactionException;
import com.example.merchantransaction.adapter.out.persistence.client.NumeratorClient;
import com.example.merchantransaction.application.port.in.ReceivableUseCase;
import com.example.merchantransaction.application.port.in.TransactionUseCase;
import com.example.merchantransaction.application.port.out.TransactionRepository;
import com.example.merchantransaction.domain.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final NumeratorClient numeratorClient;
    private final ReceivableUseCase receivableUseCase;



    @Override
    public void createTransaction(Transaction transaction) throws TransactionException {
        String transactionId = null;

        try {
             transactionId = String.valueOf(this.numeratorClient.generateUniqueId());

            transaction.setId(transactionId);
            transaction.setCardNumber(transaction.getCardNumber().substring(12)); // Apenas últimos 4 dígitos

            this.transactionRepository.save(transaction);

            //creating receivable for transaction
            this.receivableUseCase.createReceivables(transaction);

        } catch (Exception e) {
            log.error("Transaction was not created");

            manualRollbackCompensation(transactionId);

            throw new TransactionException("Failed to process payment: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return this.transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return this.transactionRepository.findById(id);
    }


    private void manualRollbackCompensation(String transactionId) {
        // Compensation: Delete transaction if receivable fails, doing manual rollback because the operation is not in a transaction database.
        if (transactionId != null) {
            try {
                transactionRepository.delete(transactionId);
                log.info("Rolled back transaction with ID: {}", transactionId);
            } catch (Exception rollbackEx) {
                log.error("Failed to rollback transaction with ID: {}", transactionId, rollbackEx);
            }
        }
    }
}
