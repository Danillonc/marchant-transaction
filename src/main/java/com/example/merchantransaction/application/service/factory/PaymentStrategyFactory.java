package com.example.merchantransaction.application.service.factory;

import com.example.merchantransaction.application.service.strategy.CreditCardPaymentStrategy;
import com.example.merchantransaction.application.service.strategy.DebitCardPaymentStrategy;
import com.example.merchantransaction.domain.model.PaymentMethodStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    private final Map<String, PaymentMethodStrategy> strategies = new HashMap<>();

    private final CreditCardPaymentStrategy creditCardPaymentStrategy;
    private final DebitCardPaymentStrategy debitCardPaymentStrategy;

    @PostConstruct
    public void init() {
        strategies.put("credit_card", creditCardPaymentStrategy);
        strategies.put("debit_card", debitCardPaymentStrategy);
    }

    public PaymentMethodStrategy getStrategy(String paymentMethod) {
        PaymentMethodStrategy strategy = strategies.get(paymentMethod.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment method: "+ paymentMethod);
        }
        return strategy;
    }

}
