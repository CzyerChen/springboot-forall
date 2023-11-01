/**
 * Author:   claire
 * Date:    2023/8/10 - 6:21 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/10 - 6:21 下午          V1.0.0
 */
package com.learning.nacosclient;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

/**
 *
 * @author claire
 * @date 2023/8/10 - 6:21 下午
 * @since 1.0.0
 */
public class NacosTest {
//    public static void main(String[] args) throws NacosException {
//        Properties properties = new Properties();
//        // gatewayConfig为配置的需要监听的参数
//        // nacos地址
//        properties.setProperty("serverAddr", "127.0.0.1:8848");
//        // 工作空间
//        properties.setProperty("namespace", "demotest");
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        // 从远程获取配置
//        String configInfo = configService.getConfig("localtest", "DEFAULT_GROUP", 5000);
//        System.out.println(configInfo );
//    }
}
