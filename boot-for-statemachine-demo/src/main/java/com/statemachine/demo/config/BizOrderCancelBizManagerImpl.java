/**
 * Author:   claire
 * Date:    2020-12-28 - 21:23
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:23          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:23
 */
@Component("bizOrderCancelBizManager")
public class BizOrderCancelBizManagerImpl extends BaseStatusSimpleChangeBizManagerImpl<BizOrderStatusRequest, OrderBaseResponse> {

    /**
     * 实际的业务操作
     *
     * @param request       业务请求
     * @return 业务结果
     */
    @Override
    public OrderBaseResponse doProcess(BizOrderStatusRequest request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws Exception {
        return super.doProcess(request,stateMachines);
    }

    /**
     * 构造目标状态
     *
     * @param statusModel 状态模型
     * @return 结果
     */
    @Override
    public String wrapTargetStatus(BizOrderStatusModel statusModel) {
        return BizOrderStatusEnum.CANCEL.getStatus();
    }

    /**
     * 关闭原因，仅需要在close、success、cancel场景下处理
     *
     * @param statusModel
     * @return
     */
    @Override
    public String wrapFinishReason(BizOrderStatusModel statusModel) {
        if (StringUtils.isBlank(statusModel.getFinishReason())) {
            return "CANCEL_FROM_" + StringUtils.upperCase(statusModel.getCurrentOrderStatus());
        }
        return statusModel.getFinishReason();
    }
}
