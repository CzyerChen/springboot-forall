/**
 * Author:   claire
 * Date:    2020-12-28 - 21:20
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:20          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:20
 * @since 1.13.0
 */
@FunctionalInterface
public interface BaseBizManager<T, R> {

    /**
     * process模板，用于处理通用写服务相关方法，包括处理幂等、记录日志、事务保证等
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    R process(T request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws BusinessException;

}