/**
 * Author:   claire
 * Date:    2021/12/20 - 3:52 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021/12/20 - 3:52 下午          V1.0.0
 */
package com.learning.nacos.config;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import org.springframework.context.annotation.Configuration;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021/12/20 - 3:52 下午
 * @since 1.0.0
 */
@Configuration
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "http://127.0.0.1:8848", username="nacos",password = "nacos"))
@NacosPropertySources({
        @NacosPropertySource(dataId = "example", autoRefreshed = true),
        @NacosPropertySource(dataId = "arthas",groupId = "sms-admin",autoRefreshed = true),
        @NacosPropertySource(dataId = "arthas",groupId = "sms-channel",autoRefreshed = true)
})
public class NacosConfig {
}
