package com.example.merchantransaction.application.port.out;

import com.example.merchantransaction.domain.model.TransactionEvent;

public interface TransacationEventPublisher {
    void publish(TransactionEvent event);
}
