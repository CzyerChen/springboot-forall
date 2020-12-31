/**
 * Author:   claire
 * Date:    2020-12-28 - 21:21
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:21          V1.13.0
 */
package com.statemachine.demo.config;

import com.statemachine.demo.constant.BizOrderStatusChangeEventEnum;
import com.statemachine.demo.constant.BizOrderStatusEnum;
import com.statemachine.demo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:21
 */
@Slf4j
public abstract class AbstractBizManagerImpl<T,R> implements BaseBizManager<T,R>{

    @Autowired
    private BizOrderIdemRepository orderIdemRepository;

    @Autowired
    private BizOrderLogEventPublisher bizOrderLogEventPublisher;

    @Override
    @Transactional(value = "finOrderocTransactionManager", rollbackFor = {BusinessException.class, Exception.class})
    public R process(T request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws BusinessException {
        try {
            // 幂等控制
            if (checkIdem(request)) {
                log.info("check idempotence bingo,request={}", request);
                throw new BusinessException(200, "幂等操作，本次请求忽略");
            }

            // 实际业务处理
            R resp = doProcess(request, stateMachines);
            log.info("response = {}", resp);

            return resp;
        } catch (BusinessException e) {
            log.error("process Business Exception = {}", e);
            throw new BusinessException(e.getErrorCode(), ExceptionUtil.getErrorMsg(e));
        } catch (Exception e) {
            log.error("process Exception = {}", e);
            throw new BusinessException(BizOrderErrorCode.ORDER_GENERIC_EXCEPTION, ExceptionUtil.getErrorMsg(e));
        }
    }

    /**
     * 实际的业务操作
     *
     * @param request 业务请求
     * @param stateMachines 将上游处理后的stateMachine传递进来，后续持久化，可选参数
     * @return 业务结果
     */
    public abstract R doProcess(T request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws Exception;

    /**
     * 判断是否幂等
     * 幂等 ==> 返回true
     *
     * @param request
     * @return
     */
    private boolean checkIdem(T request) {
        boolean result = false;
        // 反射获取请求中基础数据
        String bizOrderId = (String) ReflectionUtil.getValue(request, "bizCode", "");
        String operationType = (String) ReflectionUtil.getValue(request, "operationType", "");
        String sourceId = (String) ReflectionUtil.getValue(request, "sourceId", "");

        String idemNo = bizOrderId + operationType + sourceId;
        BizOrderIdem idem = new BizOrderIdem(idemNo, bizOrderId);

        // 违反唯一性约束
        try {
            orderIdemRepository.insert(idem);
        } catch (DuplicateKeyException e) {
            result = true;
            log.error("接口重复消费, idemNo = {}, orderCode = {}, exception = {}", idemNo, bizOrderId, e);
        } catch (Exception e) {
            log.error("未知异常，exception={}", e);
        }

        return result;
    }
}
