/**
 * Author:   claire
 * Date:    2020-12-28 - 21:24
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:24          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:24
 * @since 1.13.0
 */
@Component("bizOrderToCloseBizManager")
public class BizOrderToCloseBizManagerImpl extends BaseStatusToUnstableTargetBizManagerImpl<BizOrderStatusRequest, OrderBaseResponse> {

    /**
     * 实际的业务操作
     *
     * @param request       业务请求
     * @param stateMachines 将上游处理后的stateMachine传递进来，后续持久化
     * @return 业务结果
     */
    @Override
    public OrderBaseResponse doProcess(BizOrderStatusRequest request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws Exception {
        return super.doProcess(request, stateMachines);
    }

    /**
     * 构造待发送的statemachine event
     *
     * @return 对应的event
     */
    @Override
    public BizOrderStatusChangeEventEnum wrapToEvent() {
        return BizOrderStatusChangeEventEnum.EVT_SYS_CLOSE;
    }

    /**
     * 构造对应的状态
     *
     * @return 状态
     */
    @Override
    public String wrapTargetOrderStatus() {
        return BizOrderStatusEnum.CLOSE.getStatus() + "";
    }

    /**
     * 构造对应的结束原因，只有finish态才需要返回，否则直接return null即可
     *
     * @param curOrderStatus 当前订单状态
     * @return 订单结束原因
     */
    @Override
    public String wrapFinishReason(String curOrderStatus) {
        return "CLOSE_FROM_" + curOrderStatus + "||";
    }
}
