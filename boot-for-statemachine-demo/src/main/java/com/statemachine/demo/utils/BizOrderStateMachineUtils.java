/**
 * Author:   claire
 * Date:    2020-12-28 - 21:29
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:29          V1.13.0
 */
package com.statemachine.demo.utils;

import com.statemachine.demo.constant.BizOrderStatusChangeEventEnum;
import com.statemachine.demo.constant.BizOrderStatusEnum;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineUtils;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.trigger.DefaultTriggerContext;
import org.springframework.statemachine.trigger.Trigger;

import javax.swing.plaf.nimbus.State;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:29
 */
public class BizOrderStateMachineUtils {

    /**
     * 判断是否可以执行对应的event
     *
     * @param stateMachine
     * @param eventMsg
     * @return
     */
    public static boolean acceptEvent(StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine,
                                      Message<BizOrderStatusChangeEventEnum> eventMsg) {
        State<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> cs = stateMachine.getState();

        for (Transition<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> transition : stateMachine.getTransitions()) {
            State<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> source = transition.getSource();
            Trigger<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> trigger = transition.getTrigger();

            if (cs != null && StateMachineUtils.containsAtleastOne(source.getIds(), cs.getIds())) {
                if (trigger != null && trigger.evaluate(new DefaultTriggerContext<>(eventMsg.getPayload()))) {
                    return true;
                }
            }
        }
        return false;
    }

}

