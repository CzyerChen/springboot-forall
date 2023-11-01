/**
 * Author:   claire
 * Date:    2023/8/10 - 5:27 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/10 - 5:27 下午          V1.0.0
 */
package com.learning.nacosclient.listener;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Executor;

/**
 *
 * @author claire
 * @date 2023/8/10 - 5:27 下午
 * @since 1.0.0
 */
@Slf4j
@Component
public class NacosConfigListener implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        try {
            Properties properties = new Properties();
            // gatewayConfig为配置的需要监听的参数
            // nacos地址
            properties.setProperty("serverAddr", "127.0.0.1:8848");
            // 工作空间
            properties.setProperty("namespace", "");
            // 访问密码（如果开启权限校验需要）
//            properties.setProperty("password", "");
            // 访问账户（如果开启权限校验需要）
//            properties.setProperty("username", "");
            ConfigService configService = NacosFactory.createConfigService(properties);
            // 从远程获取配置
            String configInfo = configService.getConfig("localtest", "DEFAULT_GROUP", 5000);
            log.info("获取dataId:{},group:{}配置: {}", "localtest", "DEFAULT_GROUP",configInfo );
            // 初始化操作
//            init(configInfo);
            // 添加监听
            configService.addListener("localtest", "DEFAULT_GROUP", new Listener() {
                // 配置更新操作
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("获取dataId:{},group:{} 更新配置: {}", "localtest", "DEFAULT_GROUP",configInfo );
                    // 更新数据操作
//                    doRefreshProcess(configInfo);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }

            });
        } catch (NacosException e) {
            throw new RuntimeException("Nacos手动监听异常",e);
        }
    }

//    public static void main(String[] args){
//        try {
//            String serverAddr = "{serverAddr}";
//            String dataId = "{dataId}";
//            String group = "{group}";
//            Properties properties = new Properties();
//            properties.put("serverAddr", serverAddr);
//            ConfigService configService = NacosFactory.createConfigService(properties);
//            String content = configService.getConfig(dataId, group, 5000);
//            System.out.println(content);
//        } catch (NacosException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
