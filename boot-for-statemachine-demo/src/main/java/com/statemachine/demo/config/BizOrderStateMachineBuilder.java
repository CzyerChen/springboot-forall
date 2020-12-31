/**
 * Author:   claire
 * Date:    2020-12-28 - 21:15
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:15          V1.13.0
 */
package com.statemachine.demo.config;

import com.statemachine.demo.constant.BizOrderStatusChangeEventEnum;
import com.statemachine.demo.constant.BizOrderStatusEnum;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:15
 */
public interface BizOrderStateMachineBuilder {

    String getName();

    StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> build(BeanFactory beanFactory) throws Exception;

    // 业务一对应的builder name
    String WYLOAN_BUILDER_NAME = "wyLoanStateMachineBuilder";

    // 业务二对应的builder name
    String VPAY_BUILDER_NAME = "vpayStateMachineBuilder";
}