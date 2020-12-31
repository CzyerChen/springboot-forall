/**
 * Author:   claire
 * Date:    2020-12-28 - 16:15
 * Description: 配置类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 16:15          V1.13.0          配置类
 */
package com.statemachine.config;

import com.statemachine.constant.OrderEvents;
import com.statemachine.constant.OrderStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * 功能简述 <br/>
 * 〈配置类〉
 *
 * @author claire
 * @date 2020-12-28 - 16:15
 * @since 1.13.0
 */
@Configuration
@EnableStateMachine
public class StatemachineConfigurer extends EnumStateMachineConfigurerAdapter<OrderStateEnum, OrderEvents> {
    private Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public void configure(StateMachineStateConfigurer<OrderStateEnum, OrderEvents> states)
            throws Exception {
        states.withStates()
                .initial(OrderStateEnum.UNPAID)
                .states(EnumSet.allOf(OrderStateEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStateEnum,OrderEvents> transactions) throws Exception {
        transactions
                .withExternal()
                .source(OrderStateEnum.UNPAID)
                .target(OrderStateEnum.WAITING_FOR_RECEIVE)
                .event(OrderEvents.PAY)
                .and()
                .withExternal()
                .source(OrderStateEnum.WAITING_FOR_RECEIVE)
                .target(OrderStateEnum.DONE)
                .event(OrderEvents.RECEIVE);
    }

//    @Override
//    public void configure(StateMachineConfigurationConfigurer<OrderStateEnum,OrderEvents> config) throws Exception {
//          config.withConfiguration().listener(listener());
//    }
//
//    public StateMachineListener<OrderStateEnum,OrderEvents> listener(){
//        return new StateMachineListenerAdapter<OrderStateEnum, OrderEvents>(){
//          @Override
//          public void transition(Transition<OrderStateEnum,OrderEvents> transition){
//              if(transition.getTarget().getId() == OrderStateEnum.UNPAID){
//                 log.info("订单创建，待支付");
//                 return;
//              }
//              if(transition.getSource().getId() == OrderStateEnum.UNPAID && transition.getTarget().getId() == OrderStateEnum.WAITING_FOR_RECEIVE){
//                  log.info("订单完成支付，待收货");
//                  return;
//              }
//              if(transition.getSource().getId() ==  OrderStateEnum.WAITING_FOR_RECEIVE && transition.getTarget().getId() == OrderStateEnum.DONE){
//                  log.info("用户已收货，订单完成");
//                  return;
//              }
//          }
//        };
//    }
}
