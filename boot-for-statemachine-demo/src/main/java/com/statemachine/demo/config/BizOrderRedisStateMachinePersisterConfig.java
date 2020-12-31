/**
 * Author:   claire
 * Date:    2020-12-28 - 21:11
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:11          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:11
 */
@Configuration
public class BizOrderRedisStateMachinePersisterConfig {

    @Autowired
    private StateMachinePersist bizOrderRedisStateMachineContextPersist;

    @Bean(name = "bizOrderRedisStateMachinePersister",autowire = Autowire.BY_TYPE)
    public StateMachinePersister<BizOrderStatusEnum, BizOrderStatusChangeEventEnum,String> bizOrderRedisStateMachinePersister() {
        return new RedisStateMachinePersister<>(bizOrderRedisStateMachineContextPersist);
    }

}