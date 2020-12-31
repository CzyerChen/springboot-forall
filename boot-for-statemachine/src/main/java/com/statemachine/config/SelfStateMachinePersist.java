/**
 * Author:   claire
 * Date:    2020-12-28 - 20:41
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 20:41          V1.13.0
 */
package com.statemachine.config;

import com.statemachine.constant.OrderEvents;
import com.statemachine.constant.OrderStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.stereotype.Component;
/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 20:41
 */
@Component("SelfStateMachinePersist")
public class SelfStateMachinePersist implements StateMachinePersist<OrderStateEnum, OrderEvents,String> {

    @Autowired
    @Qualifier("redisStateMachineContextRepository")
    private RedisStateMachineContextRepository<OrderStateEnum, OrderEvents> redisStateMachineContextRepository;

    @Override
    public void write(StateMachineContext<OrderStateEnum, OrderEvents> stateMachineContext, String s) throws Exception {

    }

    @Override
    public StateMachineContext<OrderStateEnum, OrderEvents> read(String s) throws Exception {
        return null;
    }
}
