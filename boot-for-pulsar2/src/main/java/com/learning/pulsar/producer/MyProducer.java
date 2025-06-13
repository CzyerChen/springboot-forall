package com.learning.pulsar.producer;

import java.util.concurrent.CompletableFuture;
import org.apache.pulsar.client.api.MessageId;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

@Service
public class MyProducer {
    private final PulsarTemplate<String> pulsarTemplate;

    public MyProducer(PulsarTemplate<String> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    public void sendMessage(String message) {
// 由于 convertAndSend(String, String) 方法未定义，可能需要使用正确的方法
// 假设使用 send 方法来替代，具体根据 PulsarTemplate 的实际方法决定
        pulsarTemplate.send("my-topic", message);
        System.out.println("Sent: " + message);
    }

    public CompletableFuture<MessageId> sendMessageAsync(String message) {
        return pulsarTemplate.sendAsync("my-topic", message);
    }
}
