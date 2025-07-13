package com.example.merchantransaction.adapter.out.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class BaseKafkaProducer<T> {

    protected KafkaTemplate<String, Object> kafkaTemplate;

    BaseKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, T message) {
        log.debug("Sending message to topic {}: {}", topic, message);
        kafkaTemplate.send(topic, message);
    }

    protected void sendWithCallback(String topic, String key, T message) {
        kafkaTemplate.send(topic, key, message).whenComplete(
                (result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic={} partition={}", topic, result.getRecordMetadata().partition());
                    } else {
                        log.error("Failed to send message to topic=" + topic, ex);
                    }
                }
        );
    }
}
