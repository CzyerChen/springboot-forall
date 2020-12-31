/**
 * Author:   claire
 * Date:    2020-12-28 - 18:01
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 18:01          V1.13.0
 */
package com.statemachine.config;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * 功能简述 <br/>
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 18:01
 */
@WithStateMachine
public class OrderEventConfig {
    private Logger log = LoggerFactory.getLogger(getClass());

    @OnTransition(target = "UNPAID")
    public void create() {
        log.info("订单创建，待支付");
    }

    @OnTransition(source = "UNPAID", target = "WAITING_FOR_RECEIVE")
    public void pay() {
        log.info("订单已支付，待收货");
    }

    @OnTransition(source = "WAITING_FOR_RECEIVE", target = "DONE")
    public void receive() {
       log.info("用户已收货，订单完成");
    }
}
