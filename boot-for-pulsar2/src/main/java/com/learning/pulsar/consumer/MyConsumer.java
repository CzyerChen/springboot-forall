package com.learning.pulsar.consumer;

import org.apache.pulsar.client.api.Message;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;

@Service
public class MyConsumer {
    @PulsarListener(topics = "my-topic")
    public void receive(Message<String> message) {
        System.out.println("Received in Spring Boot: " + message.getValue());
    }
}
