/**
 * Author:   claire
 * Date:    2023/7/31 - 9:49 上午
 * Description: 快手配置类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/7/31 - 9:49 上午          V1.0.0          快手配置类
 */
package com.learning.kuaishou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 功能简述
 * 〈快手配置类〉
 *
 * @author claire
 * @date 2023/7/31 - 9:49 上午
 * @since 1.0.0
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "kuaishou")
public class KuaishouConfig {

    private String appKey;
    private String appSecret;
    private String signSecret;
    private String msgSecret;
    private String grantCode;
    private String serverUrl;
}
