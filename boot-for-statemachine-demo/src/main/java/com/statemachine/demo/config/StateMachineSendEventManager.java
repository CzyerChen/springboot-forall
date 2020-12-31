/**
 * Author:   claire
 * Date:    2020-12-28 - 21:26
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:26          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:26
 */
public interface StateMachineSendEventManager {

    /**
     * 发送状态机event，调用bizManagerImpl中具体实现，同时处理状态机持久化
     * <p>
     * 用于订单的状态变更
     *
     * @param request
     * @param operationTypeEnum
     * @param eventEnum
     * @return
     * @throws BusinessException
     */
    OrderBaseResponse sendStatusChangeEvent(BizOrderStatusRequest request,
                                            BizOrderOperationTypeEnum operationTypeEnum,
                                            BizOrderStatusChangeEventEnum eventEnum) throws Exception;


    /**
     * 同上，不过是用于订单创建场景
     *
     * @param request
     * @param operationTypeEnum
     * @param eventEnum
     * @return
     * @throws Exception
     */
    BizOrderCreateResponse sendOrderCreateEvent(BizOrderCreateRequest request,
                                                BizOrderOperationTypeEnum operationTypeEnum,
                                                BizOrderStatusChangeEventEnum eventEnum) throws Exception;

}
