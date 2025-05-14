package com.example.merchantransaction.application.service;

import com.example.merchantransaction.adapter.in.web.exception.ReceivableException;
import com.example.merchantransaction.adapter.out.persistence.ReceivableRepositoryImpl;
import com.example.merchantransaction.adapter.out.persistence.client.NumeratorClient;
import com.example.merchantransaction.application.port.in.ReceivableUseCase;
import com.example.merchantransaction.application.port.out.ReceivableRepository;
import com.example.merchantransaction.application.service.factory.PaymentStrategyFactory;
import com.example.merchantransaction.domain.model.PaymentMethodStrategy;
import com.example.merchantransaction.domain.model.Receivable;
import com.example.merchantransaction.domain.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ReceivableServiceImpl implements ReceivableUseCase {

    private final ReceivableRepository receivableRepository;
    private final NumeratorClient numeratorClient;
    private final PaymentStrategyFactory strategyFactory;

    @Override
    public void createReceivables(Transaction transaction) throws ReceivableException {
        try {
            String receivableId = String.valueOf(this.numeratorClient.generateUniqueId());

            //TODO thing about refactoring this snippet code below might be transfering this code to REceivableService layer
            //TODO an dinject by constructor the service layer here - it will unidirectional Transaction -> Receivables.
            PaymentMethodStrategy strategy = strategyFactory.getStrategy(transaction.getMethod());

            Receivable receivable = strategy.process(transaction);
            receivable.setId(receivableId);

            receivableRepository.save(receivable);
        } catch (Exception e) {
            log.error("Failed to create the receivables for transaction: {}", transaction.getId());
            throw new ReceivableException(e.getMessage());
        }

    }

    @Override
    public List<Receivable> findByDateRange(String start, String end) {
        return this.receivableRepository.findByDateRange(start, end);
    }

    @Override
    public List<Receivable> findAll() {
        return this.receivableRepository.findAll();
    }
}
