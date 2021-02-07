/**
 * Author:   claire
 * Date:    2020-02-10 - 09:35
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-10 - 09:35          V1.3.1
 */
package com.actuator.indicator;

import com.actuator.client.OlapSearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * 功能简述
 * 〈实时搜索引擎健康检查类〉
 *
 * @author claire
 * @date 2020-01-09 - 16:33
 */
@Component
public class OlapEngineHealthIndicator extends AbstractHealthIndicator {
    @Autowired
    private OlapSearchClient searchClient;

    /**
     * 功能描述:
     * 〈健康检测详细操作，通过认证操作测活〉
     *
     * @param  builder
     * @since 1.3.0
     * @author claire
     * @date 2020-01-09 - 18:24
     */
    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            searchClient.auth();
            builder.up()
                    .withDetail("olap","【Connection Active】Successful !");
        }catch (Exception e){
            builder.down()
                    .withDetail("olap","【Connection Error】 Please check the network !");
        }
    }
}
