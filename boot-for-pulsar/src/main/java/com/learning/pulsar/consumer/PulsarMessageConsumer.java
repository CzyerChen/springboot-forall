package com.learning.pulsar.consumer;

//import com.example.pulsardemo.model.Message;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PulsarMessageConsumer implements CommandLineRunner {

    private static final String TOPIC = "persistent://public/default/messages";
    private static final String SUBSCRIPTION = "message-subscription";
    
    @Autowired
    private PulsarClient pulsarClient;

    @Override
    public void run(String... args) throws Exception {
        // 启动消费者
        startConsumer();
    }

    public void startConsumer() throws PulsarClientException {
        // 创建消费者
        Consumer<Message> consumer = pulsarClient.newConsumer(Schema.JSON(Message.class))
                .topic(TOPIC)
                .subscriptionName(SUBSCRIPTION)
                .subscriptionType(SubscriptionType.Shared)
                .subscribe();

        // 异步消费消息
        new Thread(() -> {
            while (true) {
                try {
                    // 等待接收消息，超时时间为10秒
                    Message<Message> msg = consumer.receive(10, TimeUnit.SECONDS);
                    
                    if (msg != null) {
                        try {
                            // 处理消息
                            Message message = msg.getValue();
                            System.out.println("Received message: " + message);
                            
                            // 确认消息已消费
                            consumer.acknowledge(msg);
                        } catch (Exception e) {
                            // 处理消息失败，重新放回队列
                            consumer.negativeAcknowledge(msg);
                        }
                    }
                } catch (PulsarClientException e) {
                    if (e.getCause() instanceof java.util.concurrent.TimeoutException) {
                        // 超时异常，继续等待
                        System.out.println("No message received within timeout period, waiting again...");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
