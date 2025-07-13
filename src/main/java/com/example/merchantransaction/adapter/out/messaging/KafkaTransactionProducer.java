package com.example.merchantransaction.adapter.out.messaging;

import com.example.merchantransaction.application.port.out.TransacationEventPublisher;
import com.example.merchantransaction.domain.model.TransactionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionProducer extends BaseKafkaProducer<TransactionEvent> implements TransacationEventPublisher {

    private final String topic;

    public KafkaTransactionProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                    @Value("${kafka.topics.transaction-events}")
                                    String topic) {
        super(kafkaTemplate);
        this.topic = topic;
    }

    @Override
    public void publish(TransactionEvent event) {
        // Assuming your TransactionEvent has a method to get a unique ID
        String key = event.getTransactionId();

        // The 'event' object will be correctly serialized by the JsonSerializer
        sendWithCallback(topic, key, event);
    }
}
