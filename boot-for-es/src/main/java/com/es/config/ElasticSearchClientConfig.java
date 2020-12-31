/**
 * Author:   claire
 * Date:    2020-12-30 - 15:59
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-30 - 15:59          V1.13.0
 */
package com.es.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-30 - 15:59
 */
@Configuration
public class ElasticSearchClientConfig extends AbstractElasticsearchConfiguration {

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .withBasicAuth("test","test")
                .withSocketTimeout(60000)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}

