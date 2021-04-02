/**
 * Author:   claire
 * Date:    2021-04-02 - 14:48
 * Description: zookeeper配置文件
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-02 - 14:48          V1.17.0          zookeeper配置文件
 */
package com.learning.zookeeper.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 功能简述 
 * 〈zookeeper配置文件〉
 *
 * @author claire
 * @date 2021-04-02 - 14:48
 * @since 1.1.0
 */
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperConfig {
    private String connectString;
    private Integer maxRetries;
    private Integer baseSleepTimeMs;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(Integer baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }
}
