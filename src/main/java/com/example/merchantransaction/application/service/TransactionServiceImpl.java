package com.example.merchantransaction.application.service;

import com.example.merchantransaction.adapter.out.persistence.client.NumeratorClient;
import com.example.merchantransaction.application.port.in.TransactionUseCase;
import com.example.merchantransaction.application.port.out.ReceivableRepository;
import com.example.merchantransaction.application.port.out.TransactionRepository;
import com.example.merchantransaction.application.service.factory.PaymentStrategyFactory;
import com.example.merchantransaction.domain.model.PaymentMethodStrategy;
import com.example.merchantransaction.domain.model.Receivable;
import com.example.merchantransaction.domain.model.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final ReceivableRepository receivableRepository;
    private final NumeratorClient numeratorClient;
    private final PaymentStrategyFactory strategyFactory;


    @Override
    public void processPayment(Transaction transaction) {
        String transactionId = String.valueOf(this.numeratorClient.generateUniqueId());
        String receivableId = String.valueOf(this.numeratorClient.generateUniqueId());

        transaction.setId(transactionId);
        transaction.setCardNumber(transaction.getCardNumber().substring(12)); // Apenas últimos 4 dígitos

        transactionRepository.save(transaction);

        //recebiveis
        PaymentMethodStrategy strategy = strategyFactory.getStrategy(transaction.getMethod());

        Receivable receivable = strategy.process(transaction);
        receivable.setId(receivableId);

        receivableRepository.save(receivable);

    }

    @Override
    public List<Transaction> getAllTransactions() {
        return this.transactionRepository.findAll();
    }
}
