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
@Slf4j
public abstract class BaseStatusToUnstableTargetBizManagerImpl<T, R> extends AbstractBizManagerImpl<T, R> {

    @Autowired
    private BizOrderRepository bizOrderRepository;

    @Autowired
    private BizOrderLogEventPublisher bizOrderLogEventPublisher;

    @Autowired
    private BeanMapper beanMapper;

    /**
     * 实际的业务操作
     *
     * @param request       业务请求
     * @param stateMachines 将上游处理后的stateMachine传递进来，后续持久化
     * @return 业务结果
     */
    OrderBaseResponse doProcess(BizOrderStatusRequest request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws Exception {
        // 修改状态
        BizOrderStatusModel statusModel = request.getBizOrderStatusModel();

        BizOrder bizOrder = bizOrderRepository.selectByBizPrimaryKey(statusModel.getBizOrderId());
        String currentStatus = bizOrder.getOrderStatus();

        BizOrder newBizOrder = new BizOrder();
        newBizOrder.setOrderStatus(statusModel.getTargetOrderStatus());
        newBizOrder.setBizOrderId(bizOrder.getBizOrderId());
        newBizOrder.setUpdateTime(Date.from(Instant.now()));

        int updateCount = bizOrderRepository.updateByPrimaryKeySelective(newBizOrder);
        if (1 != updateCount) {
            throw new BusinessException(BizOrderErrorCode.ORDER_UPDATE_ERROR, "更新订单状态失败");
        }

        // send spring statemachine event
        if (null != stateMachines && stateMachines.length > 0) {
            StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine = stateMachines[0];

            // 重新构造request
            statusModel.setFinishReason(wrapFinishReason(statusModel.getTargetOrderStatus()) + statusModel.getFinishReason());
            statusModel.setCurrentOrderStatus(statusModel.getTargetOrderStatus());
            statusModel.setTargetOrderStatus(wrapTargetOrderStatus());

            request.setBizOrderStatusModel(statusModel); // need or not

            Message<BizOrderStatusChangeEventEnum> eventMsg = MessageBuilder.
                    withPayload(wrapToEvent())
                    .setHeader(BizOrderConstants.BIZORDER_CONTEXT_KEY, request)
                    .build();

            stateMachine.sendEvent(eventMsg);
        }

        // send log event
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
//            @Retryable(maxAttempts = 5)
            public void afterCommit() {
                // 发送记录日志的event
                bizOrderLogEventPublisher.bizOrderEventPublish(bizOrder,
                        request.getOperationType(), currentStatus,
                        Maps.newHashMap(AttributesKeyEnum.CALL_SYSTEM.getShortKeyName(), request.getCallSystem()));

            }
        });

        return new OrderBaseResponse();
    }

    /**
     * 构造待发送的statemachine event
     *
     * @return 对应的event
     */
    public abstract BizOrderStatusChangeEventEnum wrapToEvent();

    /**
     * 构造对应的状态
     *
     * @return 状态
     */
    public abstract String wrapTargetOrderStatus();

    /**
     * 构造对应的结束原因，只有finish态才需要返回，否则直接return null即可
     *
     * @param curOrderStatus 当前订单状态
     * @return 订单结束原因
     */
    public abstract String wrapFinishReason(String curOrderStatus);
}
