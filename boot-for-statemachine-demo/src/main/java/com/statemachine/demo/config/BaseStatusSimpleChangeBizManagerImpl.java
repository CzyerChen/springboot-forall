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
 * @since 1.13.0
 */
@Slf4j
public abstract class BaseStatusSimpleChangeBizManagerImpl<T, R> extends AbstractBizManagerImpl<T, R> {

    @Autowired
    private BizOrderRepository bizOrderRepository;

    @Autowired
    private BizOrderLogEventPublisher bizOrderLogEventPublisher;

    /**
     * 实际的业务操作
     *
     * @param request       业务请求
     * @return 业务结果
     */
    public OrderBaseResponse doProcess(BizOrderStatusRequest request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws Exception {

        // 重新获取订单信息,肯定不是空，不然就在上层拦截了
        BizOrder bizOrder = bizOrderRepository.selectByBizPrimaryKey(request.getBizCode());

        BizOrderStatusModel statusModel = request.getBizOrderStatusModel();

        BizOrder newBizOrder = new BizOrder();

        newBizOrder.setOrderStatus(wrapTargetStatus(statusModel)); // 前面已经处理对应的状态设置
        newBizOrder.setUpdateTime(Date.from(Instant.now()));
        newBizOrder.setFinishReason(wrapFinishReason(statusModel)); // 需要子类处理

        // 判断是否需要处理attributes 及 effectMoney
        if (null != statusModel.getAttributesMap() && statusModel.getAttributesMap().size() > 0) {
            Map<String, String> curAttributes = AttributeUtil.fromString(bizOrder.getAttributesStr());
            curAttributes.putAll(statusModel.getAttributesMap());
            newBizOrder.setAttributesStr(AttributeUtil.toString(curAttributes));
        }

        newBizOrder.setBizOrderId(bizOrder.getBizOrderId());

        // 订单信息保存
        int updateCount = bizOrderRepository.updateByPrimaryKeySelective(newBizOrder);
        if (1 != updateCount) {
            throw new BusinessException(BizOrderErrorCode.ORDER_UPDATE_ERROR, "订单状态变更失败");
        }

        // send log event
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 如果effectMoney不为空，则记录到log中
                Map<String,String> attributesMap = Maps.newHashMap();
                if (null != statusModel.getEffectAmount()) {
                    attributesMap.put(AttributesKeyEnum.EFFECT_MONEY_AMOUNT.getShortKeyName(), statusModel.getEffectAmount().toString());
                }
                if(null != statusModel.getAttributesMap()) {
                    attributesMap.putAll(statusModel.getAttributesMap());
                }
                attributesMap.put(AttributesKeyEnum.CALL_SYSTEM.getShortKeyName(), request.getCallSystem());

                bizOrderLogEventPublisher.bizOrderEventPublish(newBizOrder, request.getOperationType(),
                        bizOrder.getOrderStatus(), attributesMap);
            }
        });

        return new OrderBaseResponse(); // 返回值不会用到
    }

    /**
     * 构造目标状态
     *
     * @param statusModel 状态模型
     * @return 结果
     */
    public abstract String wrapTargetStatus(BizOrderStatusModel statusModel);

    /**
     * 构造关闭原因，仅需要在close、success、cancel场景下处理
     * @param statusModel 状态模型
     * @return 结果
     */
    public abstract String wrapFinishReason(BizOrderStatusModel statusModel);

}