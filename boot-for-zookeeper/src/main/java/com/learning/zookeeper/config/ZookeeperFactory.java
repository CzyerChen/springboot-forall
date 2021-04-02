/**
 * Author:   claire
 * Date:    2021-04-02 - 14:50
 * Description: zookeeper连接工厂
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-02 - 14:50          V1.17.0          zookeeper连接配置
 */
package com.learning.zookeeper.config;

import com.learning.zookeeper.properties.ZookeeperConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 功能简述
 * 〈zookeeper连接工厂〉
 *
 * @author claire
 * @date 2021-04-02 - 14:50
 * @since 1.1.0
 */
@Component
public class ZookeeperFactory implements FactoryBean<CuratorFramework> {
    private ZookeeperConfig zookeeperConfig;
    private CuratorFramework curatorClient;

    @PostConstruct
    public void init() {
        ExponentialBackoffRetry backoffRetry = new ExponentialBackoffRetry(zookeeperConfig.getMaxRetries(), zookeeperConfig.getBaseSleepTimeMs());
        curatorClient = CuratorFrameworkFactory.builder().connectString(zookeeperConfig.getConnectString()).retryPolicy(backoffRetry).build();
        curatorClient.start();
    }

    @Override
    public CuratorFramework getObject() throws Exception {
        return curatorClient;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Autowired
    public void setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }
}
