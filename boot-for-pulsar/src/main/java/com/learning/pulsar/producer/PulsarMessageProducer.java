package com.learning.pulsar.producer;

import com.learning.pulsar.model.Message;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PulsarMessageProducer {

    private static final String TOPIC = "persistent://public/default/messages";
    
    @Autowired
    private PulsarClient pulsarClient;

    public void sendMessage(String content) throws PulsarClientException {
        // 创建生产者
        Producer<Message> producer = pulsarClient.newProducer(Schema.JSON(Message.class))
                .topic(TOPIC)
                .producerName("message-producer")
                .create();

        // 创建消息对象
        Message message = new Message(
                UUID.randomUUID().toString(),
                content,
                LocalDateTime.now()
        );

        // 发送消息（同步）
        MessageId messageId = producer.send(message);
        System.out.println("Message sent successfully. Message ID: " + messageId);

        // 关闭生产者
        producer.close();
    }

    public CompletableFuture<MessageId> sendMessageAsync(String content) throws PulsarClientException {
        // 创建生产者
        Producer<Message> producer = pulsarClient.newProducer(Schema.JSON(Message.class))
                .topic(TOPIC)
                .producerName("async-message-producer")
                .create();

        // 创建消息对象
        Message message = new Message(
                UUID.randomUUID().toString(),
                content,
                LocalDateTime.now()
        );

        // 异步发送消息
        CompletableFuture<MessageId> future = producer.sendAsync(message);
        future.thenAccept(messageId -> {
            System.out.println("Async message sent successfully. Message ID: " + messageId);
            try {
                producer.close();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }).exceptionally(throwable -> {
            System.err.println("Failed to send message: " + throwable.getMessage());
            try {
                producer.close();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
            return null;
        });

        return future;
    }
}
