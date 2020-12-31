/**
 * Author:   claire
 * Date:    2020-12-28 - 16:06
 * Description: statemachine测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 16:06          V1.13.0          statemachine测试
 */
package com.statemachine;

import com.statemachine.constant.OrderEvents;
import com.statemachine.constant.OrderStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

/**
 * 功能简述 <br/> 
 * 〈statemachine测试〉
 *
 * @author claire
 * @date 2020-12-28 - 16:06
 */
@SpringBootApplication
public class StateMachineTestApplication implements CommandLineRunner {
    @Autowired
    private StateMachine<OrderStateEnum, OrderEvents> stateMachine;

    public static void main(String[] args){
        SpringApplication.run(StateMachineTestApplication.class,args);
    }

    public void run(String... args) throws Exception {
        stateMachine.start();
        stateMachine.sendEvent(OrderEvents.PAY);
        stateMachine.sendEvent(OrderEvents.RECEIVE);
    }
}
