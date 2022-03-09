/**
 * Author:   claire
 * Date:    2021/12/26 - 6:09 下午
 * Description: demo
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021/12/26 - 6:09 下午          V1.0.0          demo
 */
package com.learning.nacos.config;

/**
 * 功能简述 
 * 〈demo〉
 *
 * @author claire
 * @date 2021/12/26 - 6:09 下午
 * @since 1.0.0
 */

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@NacosConfigurationProperties(prefix = "arthas",dataId = "arthas",groupId = "sms-admin",autoRefreshed = true,properties =@NacosProperties(namespace = "015ae736-f567-4633-b6ce-62d2adf0970a"),type = ConfigType.PROPERTIES)
public class DemoProperties {
    private String home;
    private String agentid;
    private String configMap;

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;                                                  
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getConfigMap() {
        return configMap;
    }

    public void setConfigMap(String configMap) {
        this.configMap = configMap;
    }

    @Override
    public String toString() {
        return "DemoProperties{" +
                "home='" + home + '\'' +
                ", agentid='" + agentid + '\'' +
                ", configMap='" + configMap + '\'' +
                '}';
    }
}
