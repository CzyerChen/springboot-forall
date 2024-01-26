/**
 * Author:   claire Date:    2024/1/15 - 2:39 下午 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2024/1/15 - 2:39 下午          V1.0.0
 */

package com.learning.bootforrocketmq5.config;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.ProducerBuilder;
import org.apache.rocketmq.client.apis.producer.TransactionChecker;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2024/1/15 - 2:39 下午
 * @since 1.0.0
 */
public class ProducerSingleton {
    private static volatile Producer PRODUCER;
    private static volatile Producer TRANSACTIONAL_PRODUCER;
    private static final String ACCESS_KEY = "yourAccessKey";
    private static final String SECRET_KEY = "yourSecretKey";
//    private static final String ENDPOINTS = "127.0.0.1:8081";

    private ProducerSingleton() {
    }

    private static Producer buildProducer(TransactionChecker checker,String endpoints, String... topics) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        // Credential provider is optional for client configuration.
        // This parameter is necessary only when the server ACL is enabled. Otherwise,
        // it does not need to be set by default.
//        SessionCredentialsProvider sessionCredentialsProvider =
//            new StaticSessionCredentialsProvider(ACCESS_KEY, SECRET_KEY);
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                     .setEndpoints(endpoints)
                                                                     // On some Windows platforms, you may encounter SSL compatibility issues. Try turning off the SSL option in
                                                                     // client configuration to solve the problem please if SSL is not essential.
                                                                     // .enableSsl(false)
//                                                                     .setCredentialProvider(sessionCredentialsProvider)
                                                                     .build();
        final ProducerBuilder builder = provider.newProducerBuilder()
                                                .setClientConfiguration(clientConfiguration)
                                                // Set the topic name(s), which is optional but recommended. It makes producer could prefetch
                                                // the topic route before message publishing.
                                                .setTopics(topics);
        if (checker != null) {
            // Set the transaction checker.
            builder.setTransactionChecker(checker);
        }
        return builder.build();
    }

    public static Producer getInstance(String endpoints,String... topics) throws ClientException {
        if (null == PRODUCER) {
            synchronized (ProducerSingleton.class) {
                if (null == PRODUCER) {
                    PRODUCER = buildProducer(null,endpoints, topics);
                }
            }
        }
        return PRODUCER;
    }

    public static Producer getTransactionalInstance(TransactionChecker checker, String endpoint,
                                                    String... topics) throws ClientException {
        if (null == TRANSACTIONAL_PRODUCER) {
            synchronized (ProducerSingleton.class) {
                if (null == TRANSACTIONAL_PRODUCER) {
                    TRANSACTIONAL_PRODUCER = buildProducer(checker,endpoint, topics);
                }
            }
        }
        return TRANSACTIONAL_PRODUCER;
    }
}
