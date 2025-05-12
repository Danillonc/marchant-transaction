package com.example.merchantransaction.application.service.strategy;

import com.example.merchantransaction.domain.model.PaymentMethodStrategy;
import com.example.merchantransaction.domain.model.Receivable;
import com.example.merchantransaction.domain.model.Transaction;
import com.example.merchantransaction.infrastructure.util.LocalDateFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DebitCardPaymentStrategy implements PaymentMethodStrategy {

    @Override
    public Receivable process(Transaction transaction) {
        BigDecimal discount = transaction.getValue().multiply(BigDecimal.valueOf(0.02));
        BigDecimal total = transaction.getValue().subtract(discount);

        return Receivable.builder().
                transactionId(transaction.getId())
                .status("paid")
                .createDate(LocalDateFormat.convertLocalDateToString(LocalDateTime.now()))
                .subtotal(transaction.getValue())
                .discount(discount)
                .total(total)
                .build();

    }
}
