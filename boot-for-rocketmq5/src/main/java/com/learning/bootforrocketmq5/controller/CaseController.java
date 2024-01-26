/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.learning.bootforrocketmq5.controller;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.tools.command.MQAdminStartup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import test.apache.skywalking.apm.testcase.rocketmq.client.java.service.MessageService;

@RestController
@RequestMapping("/case")
@Slf4j
public class CaseController {

    private static final String SUCCESS = "Success";

    @Value("${endpoints:192.168.2.93:8081}")
    private String endpoints;

    @Value("${nameServer:demo}")
    private String nameServer;

    static final String TOPIC = "dataTopic";
    static final String TAG = "TagA";
    static final String GROUP = "group1";

    Producer producer;

    PushConsumer consumer;

    SimpleConsumer simpleConsumer;
    @Autowired
    private MessageService messageService;

    @RequestMapping("/rocketmq-5-grpc-scenario")
    @ResponseBody
    public String testcase() {
        try {
            ClientServiceProvider provider = ClientServiceProvider.loadService();
            ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                         .setEndpoints(endpoints)
                                                                         //                    .enableSsl(false)
                                                                         .build();
            // start producer
            if (producer == null) {
                producer = provider.newProducerBuilder()
                                   .setClientConfiguration(clientConfiguration)
                                   .build();
            }

            // send msg
            Message message = provider.newMessageBuilder()
                                      // Set topic for the current message.
                                      .setTopic(TOPIC)
                                      // Message secondary classifier of message besides topic.
                                      .setTag(TAG)
                                      // Key(s) of the message, another way to mark message besides message id.
                                      .setKeys("KeyA")
                                      .setBody("This is a normal message for Apache RocketMQ".getBytes(
                                          StandardCharsets.UTF_8))
                                      .build();
            SendReceipt sendReceipt = producer.send(message);

            // start consumer
            //            Thread thread = new Thread(new Runnable() {
            //                @Override
            //                public void run() {
            //                    try {
            //                        FilterExpression filterExpression = new FilterExpression(TAG, FilterExpressionType.TAG);
            //                        if (consumer == null) {
            //                            consumer = provider.newPushConsumerBuilder()
            //                                    .setClientConfiguration(clientConfiguration)
            //                                    .setConsumerGroup(GROUP)
            //                                    .setSubscriptionExpressions(Collections.singletonMap(TOPIC, filterExpression))
            //                                    .setMessageListener(new MyConsumer())
            //                                    .build();
            //                        }
            //                    } catch (Exception e) {
            //                        log.error("consumer start error", e);
            //                    }
            //                }
            //            });

            //            thread.start();
            FilterExpression filterExpression = new FilterExpression(TAG, FilterExpressionType.TAG);
            SimpleConsumer consumer = provider.newSimpleConsumerBuilder()
                                              .setClientConfiguration(clientConfiguration)
                                              // Specify the consumer group.
                                              .setConsumerGroup(GROUP)
                                              // Specify the timeout period for long polling requests.
                                              .setAwaitDuration(Duration.ofSeconds(10))
                                              // Specify the subscription.
                                              .setSubscriptionExpressions(
                                                  Collections.singletonMap(TOPIC, filterExpression))
                                              .build();
            int maxMessageNum = 16;
            // Specify the invisible time of the message.
            Duration invisibleDuration = Duration.ofSeconds(10);
            for (int i = 0; i < 1; i++) {
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
            log.error("testcase error", e);
        }
        return SUCCESS;
    }

    @RequestMapping("/testcase")
    @ResponseBody
    public String testcase2() {
        try {
            messageService.sendNormalMessageAsync(TOPIC + "5", TAG + ":async", GROUP);
            messageService.sendNormalMessageAsync(TOPIC + "6", TAG + ":async", GROUP);
            messageService.sendNormalMessageAsync(TOPIC + "7", TAG + ":async", GROUP);
//                        messageService.sendNormalMessage(TOPIC+"5", TAG + ":normal", GROUP);
//            messageService.sendNormalMessage(TOPIC+"6", TAG + ":normal", GROUP);
//            messageService.sendNormalMessage(TOPIC+"7", TAG + ":normal", GROUP);
            //            messageService.sendFifoMessage(TOPIC+"1", TAG + ":fifo", GROUP);
            //            messageService.sendDelayMessage(TOPIC+"4", TAG + ":delay", GROUP);
            //            messageService.sendTransactionMessage(TOPIC+"3", TAG + ":transaction", GROUP);

            //            messageService.pushConsume(TOPIC+"5", TAG + ":normal", GROUP);
            //            messageService.pushConsumes(Arrays.asList(TOPIC+"5",TOPIC+"1",TOPIC+"4",TOPIC+"3"),Arrays.asList(TAG + ":normal",TAG + ":fifo",TAG + ":delay",TAG + ":transaction"),GROUP);
            //            messageService.pushConsume(TOPIC+"5", TAG + ":normal", GROUP);
            //            messageService.pushConsume(TOPIC+"1", TAG + ":fifo", GROUP);
            //            messageService.pushConsume(TOPIC+"4", TAG + ":delay", GROUP);
            //            messageService.pushConsume(TOPIC+"3", TAG + ":transaction", GROUP);
//            messageService.simpleConsume(TOPIC + "5", TAG + ":normal", GROUP, 1, 10);
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//            thread.start();
            //            messageService.simpleConsume(TOPIC+"1", TAG + ":fifo", GROUP,1,10);
            //            messageService.simpleConsume(TOPIC+"4", TAG + ":delay", GROUP,1,10);
            //            messageService.simpleConsume(TOPIC+"3", TAG + ":transaction", GROUP,1,10);
            Thread.sleep(3000);
        } catch (Exception e) {
            log.error("testcase error", e);
        }
        return SUCCESS;
    }

    @RequestMapping("/healthCheck")
    @ResponseBody
    public String healthCheck() throws Exception {
        System.setProperty(MixAll.ROCKETMQ_HOME_ENV, this.getClass().getResource("/").getPath());
        String[] subArgs = new String[] {
            "updateTopic",
            "-n",
            nameServer,
            "-c",
            "DefaultCluster",
            "-t",
            "TopicTest"
        };
        MQAdminStartup.main(subArgs);

        subArgs = new String[] {
            "updateSubGroup",
            "-n",
            nameServer,
            "-c",
            "DefaultCluster",
            "-g",
            "group1"
        };
        MQAdminStartup.main(subArgs);

        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                     .setEndpoints(endpoints)
                                                                     .enableSsl(false)
                                                                     .build();
        // start producer
        Producer producer = provider.newProducerBuilder()
                                    .setClientConfiguration(clientConfiguration)
                                    .build();
        return SUCCESS;
    }

}
