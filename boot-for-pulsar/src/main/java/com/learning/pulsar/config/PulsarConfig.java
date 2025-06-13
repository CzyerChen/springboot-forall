package com.learning.pulsar.config;

import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarConfig {

    @Value("${spring.pulsar.client.service-url}")
    private String serviceUrl;

    @Bean
    public PulsarClient pulsarClient() throws PulsarClientException {
        ClientBuilder clientBuilder = PulsarClient.builder()
                .serviceUrl(serviceUrl)
                .operationTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .connectionTimeout(10, java.util.concurrent.TimeUnit.SECONDS);

        // 可以添加认证等其他配置
        // clientBuilder.authentication(AuthenticationFactory.token("your-token"));
        
        return clientBuilder.build();
    }
}
