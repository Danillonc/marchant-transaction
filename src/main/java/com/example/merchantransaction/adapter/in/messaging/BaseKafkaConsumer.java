package com.example.merchantransaction.adapter.in.messaging;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseKafkaConsumer<T> {

    protected void handleMessage(String topic, T message) {
        log.info("Consumed from topic={} payload={}", topic, message);
        process(message);
    }

    protected abstract void process(T message);
}
