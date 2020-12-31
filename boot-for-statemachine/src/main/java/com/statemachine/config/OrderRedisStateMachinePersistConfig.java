/**
 * Author:   claire
 * Date:    2020-12-28 - 20:26
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 20:26          V1.13.0
 */
package com.statemachine.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 20:26
 */
@Configuration
public class OrderRedisStateMachinePersistConfig {
    @Autowired
    private StateMachinePersist selfStateMachinePersist;

    @Bean(name = "stateMachinePersister",autowire = Autowire.BY_TYPE)
    public RedisStateMachinePersister stateMachinePersister(){
        return new RedisStateMachinePersister<>(selfStateMachinePersist);
    }
}
