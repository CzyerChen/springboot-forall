/**
 * Author:   claire
 * Date:    2020-12-28 - 21:13
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:13          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:13
 */
@Configuration
public class BizOrderRedisStateMachineRepositoryConfig {

    /**
     * 接入asgard后，redis的connectionFactory可以通过serviceName + InnerConnectionFactory来注入
     */
    @Autowired
    private RedisConnectionFactory finOrderRedisInnerConnectionFactory;

    @Bean(name = "redisStateMachineContextRepository", autowire = Autowire.BY_TYPE)
    public RedisStateMachineContextRepository<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> redisStateMachineContextRepository() {

        return new RedisStateMachineContextRepository<>(finOrderRedisInnerConnectionFactory);
    }
}
