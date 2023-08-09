/**
 * Author:   claire
 * Date:    2023/6/6 - 9:15 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/6/6 - 9:15 上午          V1.0.0
 */
package com.learning.bootforlogistics.demos.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2023/6/6 - 9:15 上午
 * @since 1.0.0
 */
@Data
@EnableConfigurationProperties
@Component
@ConfigurationProperties(prefix = "logistics")
public class LogisticsConfig {
    private static String DEFAULT_SUBSCRIBE_HOST = "https://jumexpress.market.alicloudapi.com";
    private static String DEFAULT_QUERY_HOST = "https://jmexpresv2.market.alicloudapi.com";
    private static String DEFAULT_SUBSCRIBE_PATH = "/express/logistics/subscribe";
    private static String DEFAULT_QUERY_PATH = "/express/query-v2";
    private static String DEFAULT_METHOD = "POST";

    private String subscribeHost = DEFAULT_SUBSCRIBE_HOST;
    private String subscribePath = DEFAULT_SUBSCRIBE_PATH;
    private String subscribeMethod = DEFAULT_METHOD;
    private String subscribeAppcode;
    private String queryHost = DEFAULT_QUERY_HOST;
    private String queryPath = DEFAULT_QUERY_PATH;
    private String queryMethod = DEFAULT_METHOD;
    private String queryAppcode;
}
