/**
 * Author:   claire Date:    2024/1/15 - 2:33 下午 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2024/1/15 - 2:33 下午          V1.0.0
 */

package com.learning.bootforrocketmq5.service;

import com.learning.bootforrocketmq5.config.ProducerSingleton;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.apis.producer.Transaction;
import org.apache.rocketmq.client.apis.producer.TransactionChecker;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author claire
 * @date 2024/1/15 - 2:33 下午
 * @since 1.0.0
 */
@Slf4j
@Service
public class MessageService {
    @Value("${endpoints:192.168.2.93:8081}")
    private String endpoints;

    public void sendNormalMessage(String topic, String tag, String group) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        final Producer producer = ProducerSingleton.getInstance(endpoints, topic);
        // Define your message body.
        byte[] body = "This is a normal message for Apache RocketMQ".getBytes(StandardCharsets.UTF_8);
        final Message message = provider.newMessageBuilder()
                                        // Set topic for the current message.
                                        .setTopic(topic)
                                        // Message secondary classifier of message besides topic.
                                        .setTag(tag)
                                        // Key(s) of the message, another way to mark message besides message id.
                                        .setKeys("yourMessageKey-1c151062f96e")
                                        .setBody(body)
                                        .build();
        try {
            final SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
            //            producer.close();
        } catch (Throwable t) {
            log.error("Failed to send message", t);
        }
        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        return;
    }

    public void sendNormalMessageAsync(String topic, String tag, String group) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        final Producer producer = ProducerSingleton.getInstance(endpoints, topic);
        // Define your message body.
        byte[] body = "This is a normal message for Apache RocketMQ".getBytes(StandardCharsets.UTF_8);
        final Message message = provider.newMessageBuilder()
                                        // Set topic for the current message.
                                        .setTopic(topic)
                                        // Message secondary classifier of message besides topic.
                                        .setTag(tag)
                                        // Key(s) of the message, another way to mark message besides message id.
                                        .setKeys("yourMessageKey-1c151062f96e")
                                        .setBody(body)
                                        .build();
        try {
            producer.sendAsync(message);
            log.info("Send message successfully, messageId");
            //            producer.close();
        } catch (Throwable t) {
            log.error("Failed to send message", t);
        }
        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        return;
    }

    public void sendFifoMessage(String topic, String tag, String group) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        final Producer producer = ProducerSingleton.getInstance(endpoints, topic);
        // Define your message body.
        byte[] body = "This is a FIFO message for Apache RocketMQ".getBytes(StandardCharsets.UTF_8);
        final Message message = provider.newMessageBuilder()
                                        // Set topic for the current message.
                                        .setTopic(topic)
                                        // Message secondary classifier of message besides topic.
                                        .setTag(tag)
                                        // Key(s) of the message, another way to mark message besides message id.
                                        .setKeys("yourMessageKey-1ff69ada8e0e")
                                        // Message group decides the message delivery order.
                                        .setMessageGroup(group)
                                        .setBody(body)
                                        .build();
        try {
            final SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
            //            producer.close();
        } catch (Throwable t) {
            log.error("Failed to send message", t);
        }
        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        return;
    }

    public void sendDelayMessage(String topic, String tag, String group) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        final Producer producer = ProducerSingleton.getInstance(endpoints, topic);
        // Define your message body.
        byte[] body = "This is a delay message for Apache RocketMQ".getBytes(StandardCharsets.UTF_8);
        Duration messageDelayTime = Duration.ofSeconds(10);
        final Message message = provider.newMessageBuilder()
                                        // Set topic for the current message.
                                        .setTopic(topic)
                                        // Message secondary classifier of message besides topic.
                                        .setTag(tag)
                                        // Key(s) of the message, another way to mark message besides message id.
                                        .setKeys("yourMessageKey-3ee439f945d7")
                                        // Set expected delivery timestamp of message.
                                        .setDeliveryTimestamp(System.currentTimeMillis() + messageDelayTime.toMillis())
                                        .setBody(body)
                                        .build();
        try {
            final SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
            //            producer.close();
        } catch (Throwable t) {
            log.error("Failed to send message", t);
        }
        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        return;
    }

    public void sendTransactionMessage(String topic, String tag, String group) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        TransactionChecker checker = messageView -> {
            log.info("Receive transactional message check, message={}", messageView);
            // Return the transaction resolution according to your business logic.
            return TransactionResolution.COMMIT;
        };
        // Get producer using singleton pattern.
        final Producer producer = ProducerSingleton.getTransactionalInstance(checker, endpoints, topic);
        final Transaction transaction = producer.beginTransaction();
        // Define your message body.
        byte[] body = "This is a transaction message for Apache RocketMQ".getBytes(StandardCharsets.UTF_8);
        final Message message = provider.newMessageBuilder()
                                        // Set topic for the current message.
                                        .setTopic(topic)
                                        // Message secondary classifier of message besides topic.
                                        .setTag(tag)
                                        // Key(s) of the message, another way to mark message besides message id.
                                        .setKeys("yourMessageKey-565ef26f5727")
                                        .setBody(body)
                                        .build();
        try {
            final SendReceipt sendReceipt = producer.send(message, transaction);
            log.info("Send transaction message successfully, messageId={}", sendReceipt.getMessageId());
            // Commit the transaction.
            transaction.commit();
            // Or rollback the transaction.
            // transaction.rollback();

            // Close the producer when you don't need it anymore.
            // You could close it manually or add this into the JVM shutdown hook.
            //            producer.close();
        } catch (Throwable t) {
            log.error("Failed to send message", t);
        }
        return;
    }

    public void pushConsume(String topic, String tag, String group) {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                     .setEndpoints(endpoints)
                                                                     .build();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
                    PushConsumer consumer = provider.newPushConsumerBuilder()
                                                    .setClientConfiguration(clientConfiguration)
                                                    .setSubscriptionExpressions(
                                                        Collections.singletonMap(topic, filterExpression))
                                                    .setConsumerGroup(group)
                                                    .setMessageListener(new MyConsumer())
                                                    .build();
                } catch (Exception e) {
                    log.error("consumer start error", e);
                }
            }
        });
        thread.start();
    }

    public void pushConsumes(List<String> topics, List<String> tags, String group) {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                     .setEndpoints(endpoints)
                                                                     .build();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, FilterExpression> filterExpressionMap = new HashMap<>();
                    for (int i = 0; i < topics.size(); i++) {
                        filterExpressionMap.put(
                            topics.get(i), new FilterExpression(tags.get(i), FilterExpressionType.TAG));
                    }

                    PushConsumer consumer = provider.newPushConsumerBuilder()
                                                    .setClientConfiguration(clientConfiguration)
                                                    .setSubscriptionExpressions(filterExpressionMap)
                                                    .setConsumerGroup(group)
                                                    .setMessageListener(new MyConsumer())
                                                    .build();
                } catch (Exception e) {
                    log.error("consumer start error", e);
                }
            }
        });
        thread.start();
    }

    public void simpleConsume(String topic, String tag, String group, Integer maxMessageNum, Integer duration) {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                     .setEndpoints(endpoints)
                                                                     .build();

        try {
            FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
            SimpleConsumer consumer = provider.newSimpleConsumerBuilder()
                                              .setClientConfiguration(clientConfiguration)
                                              // Specify the consumer group.
                                              .setConsumerGroup(group)
                                              // Specify the timeout period for long polling requests.
                                              .setAwaitDuration(Duration.ofSeconds(30))
                                              // Specify the subscription.
                                              .setSubscriptionExpressions(
                                                  Collections.singletonMap(topic, filterExpression))
                                              .build();

            Duration invisibleDuration = Duration.ofSeconds(duration);
            for (int i = 0; i < maxMessageNum; i++) {
                final List<MessageView> messages = consumer.receive(maxMessageNum, invisibleDuration);
                messages.forEach(messageView -> {
                    // LOGGER.info("Received message: {}", messageView);
                    System.out.println("Received message: " + messageView);
                });
                for (MessageView msg : messages) {
                    final MessageId messageId = msg.getMessageId();
                    try {
                        // After consumption is complete, the consumer must call the ACK method to commit the consumption result to the broker.
                        consumer.ack(msg);
                        System.out.println("Message is acknowledged successfully, messageId= " + messageId);
                        //LOGGER.info("Message is acknowledged successfully, messageId={}", messageId);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        //LOGGER.error("Message is failed to be acknowledged, messageId={}", messageId, t);
                    }
                }
            }
        } catch (Exception e) {
            log.error("consumer start error", e);
        }
    }

    public void simpleConsumes(String topics, String tags, String group, Integer maxMessageNum, Integer duration) {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                     .setEndpoints(endpoints)
                                                                     .build();

        try {
            Map<String, FilterExpression> filterExpressionMap = new HashMap<>();
            for (int i = 0; i < topics.split(",").length; i++) {
                FilterExpression filterExpression = new FilterExpression(tags.split(",")[i], FilterExpressionType.TAG);
                filterExpressionMap.put(topics.split(",")[i], filterExpression);
            }

            SimpleConsumer consumer = provider.newSimpleConsumerBuilder()
                                              .setClientConfiguration(clientConfiguration)
                                              // Specify the consumer group.
                                              .setConsumerGroup(group)
                                              // Specify the timeout period for long polling requests.
                                              .setAwaitDuration(Duration.ofSeconds(10))
                                              // Specify the subscription.
                                              .setSubscriptionExpressions(filterExpressionMap)
                                              .build();

            Duration invisibleDuration = Duration.ofSeconds(duration);
            for (int i = 0; i < maxMessageNum; i++) {
                final List<MessageView> messages = consumer.receive(maxMessageNum, invisibleDuration);
                messages.forEach(messageView -> {
                    // LOGGER.info("Received message: {}", messageView);
                    System.out.println("Received message: " + messageView);
                });
                for (MessageView msg : messages) {
                    final MessageId messageId = msg.getMessageId();
                    try {
                        // After consumption is complete, the consumer must call the ACK method to commit the consumption result to the broker.
                        consumer.ack(msg);
                        System.out.println("Message is acknowledged successfully, messageId= " + messageId);
                        //LOGGER.info("Message is acknowledged successfully, messageId={}", messageId);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        //LOGGER.error("Message is failed to be acknowledged, messageId={}", messageId, t);
                    }
                }
            }
        } catch (Exception e) {
            log.error("consumer start error", e);
        }
    }

    public static class MyConsumer implements MessageListener {

        @Override
        public ConsumeResult consume(MessageView messageView) {
            log.info("Consume message successfully, messageId={},messageBody={}", messageView.getMessageId(),
                     messageView.getBody().toString()
            );
            return ConsumeResult.SUCCESS;
        }
    }

}
