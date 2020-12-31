/**
 * Author:   claire
 * Date:    2020-12-28 - 21:28
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:28          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:28
 */
@Slf4j
@Component("stateMachineSendEventManager")
public class StateMachineSendEventManagerImpl implements StateMachineSendEventManager {

    @Autowired
    private BizOrderRepository bizOrderRepository;

    @Autowired
    private BizOrderStateMachineBuildFactory bizOrderStateMachineBuildFactory;

    @Autowired
    @Qualifier("bizOrderRedisStateMachinePersister")
    private StateMachinePersister<BizOrderStatusEnum,BizOrderStatusChangeEventEnum,String> bizOrderRedisStateMachinePersister;

    /**
     * 发送状态机event，调用bizManagerImpl中具体实现，同时处理状态机持久化
     * <p>
     * 这里会send stateMachine event，从而跳转到对应的action --> bizManagerImpl，出现事务嵌套的情况
     * <p>
     * 不过事务传播默认是TransactionDefinition.PROPAGATION_REQUIRED，所以还是同一个事务中，
     * 只是事务范围扩大至stateMachine的持久化场景了,不要修改默认的传播机制
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(value = "finOrderocTransactionManager", rollbackFor = {BusinessException.class, Exception.class})
    public OrderBaseResponse sendStatusChangeEvent(BizOrderStatusRequest request,
                                                   BizOrderOperationTypeEnum operationTypeEnum,
                                                   BizOrderStatusChangeEventEnum eventEnum) throws Exception {

        // 获取状态机信息
        StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine =
                getStateMachineFromStatusReq(request, operationTypeEnum);


        boolean result = statusChangeCommonOps(stateMachine, request, eventEnum);

        OrderBaseResponse resp = new OrderBaseResponse();
        if (!result) {
            resp.setResultCode(BizOrderErrorCode.ORDER_STATE_MACHINE_EXECUTE_ERR.name());
            resp.setMsg("订单状态操作异常");
        }

        log.info("order status change resp is {}", resp);

        // 更新redis中数据
        // 发送event写log的动作还是放在业务里面，这里无法囊括所有业务数据
        if (result) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    // 将数据持久化到redis中,以bizOrderId作为对应Key
                    try {
                        bizOrderRedisStateMachinePersister.persist(stateMachine, request.getBizCode());
                    } catch (Exception e) {
                        log.error("Persist bizOrderStateMachine error", e);
                    }
                }
            });
        }

        return resp;
    }

    /**
     * 同上，不过是用于订单创建场景，请求为BizOrderCreateRequest
     *
     * @param bizOrderCreateRequest
     * @param operationTypeEnum
     * @param eventEnum
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(value = "finOrderocTransactionManager", rollbackFor = {BusinessException.class, Exception.class})
    public BizOrderCreateResponse sendOrderCreateEvent(BizOrderCreateRequest bizOrderCreateRequest,
                                                       BizOrderOperationTypeEnum operationTypeEnum,
                                                       BizOrderStatusChangeEventEnum eventEnum) throws Exception {


        // 获取对应的stateMachine
        StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine =
                getStateMachineFromCreateReq(bizOrderCreateRequest, operationTypeEnum);

        Message<BizOrderStatusChangeEventEnum> eventMsg = MessageBuilder.withPayload(eventEnum)
                // key 与 status change 时不同，对应的model也不同
                .setHeader(BizOrderConstants.BIZORDER_CONTEXT_CREATE_KEY, bizOrderCreateRequest)
                // 根据传递过来的订单状态决定后续choice跳转
                .setHeader(BizOrderConstants.FINAL_STATUS_KEY, bizOrderCreateRequest.getBizOrderCreateModel().getOrderStatus())
                .build();

        BizOrderCreateResponse createResponse = new BizOrderCreateResponse();

        boolean sendResult = false;

        if (BizOrderStateMachineUtils.acceptEvent(stateMachine, eventMsg)) {
            sendResult = stateMachine.sendEvent(eventMsg);
            log.info("order statemachine send event={},result={}", eventMsg, sendResult);
        } else {
            createResponse.setResultCode(BizOrderErrorCode.NO_ORDER_STATE_MACHINE_TRANSTION_ERR.name());
            createResponse.setMsg("当前订单无法执行请求动作");
        }

        if (sendResult) {
            createResponse.setBizOrderId(bizOrderCreateRequest.getBizOrderCreateModel().getBizOrderId());
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    // 将数据持久化到redis中,以bizOrderId作为对应Key
                    try {
                        bizOrderRedisStateMachinePersister.persist(stateMachine,
                                createResponse.getBizOrderId());
                    } catch (Exception e) {
                        throw new BusinessException(BizOrderErrorCode.ORDER_STATE_MACHINE_EXECUTE_ERR, "状态机持久化失败");
                    }
                }
            });
        }


        return createResponse;
    }

    /**
     * 状态处理的通用操作抽取
     *
     * @param stateMachine  状态机
     * @param statusRequest 状态变更请求
     * @return 执行结果
     * @throws Exception 异常
     */
    private boolean statusChangeCommonOps(
            StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine,
            BizOrderStatusRequest statusRequest,
            BizOrderStatusChangeEventEnum eventEnum) {

        log.info("order statemachine send event={}", eventEnum);


        // 执行引擎，sendEvent,result为执行结果,通过actionListener跳转到对应的Action
        Message<BizOrderStatusChangeEventEnum> eventMsg = MessageBuilder.
                withPayload(eventEnum)
                .setHeader(BizOrderConstants.BIZORDER_CONTEXT_KEY, statusRequest)
                // 只有在需要判断（choice）的场景才用得到，guard实现中使用
                .setHeader(BizOrderConstants.FINAL_STATUS_KEY, statusRequest.getBizOrderStatusModel().getTargetOrderStatus())
                .build();

        // 取到对应的状态机，判断是否可以执行
        boolean result = false;

        // 状态机的当前状态，只有在执行结束后才会变化，也就是节点对应的action执行完才会变更
        // 所以在result=true的情况下，更新状态机的持久化状态才有效
        if (BizOrderStateMachineUtils.acceptEvent(stateMachine, eventMsg)) {
            result = stateMachine.sendEvent(eventMsg);
            log.info("order statemachine send event={},result={}", eventMsg, result);
        } else {
            throw new BusinessException(BizOrderErrorCode.NO_ORDER_STATE_MACHINE_TRANSTION_ERR, "当前订单无法执行请求动作");
        }
        return result;

    }

    /**
     * 从statusRequest中获取statemachine实例
     *
     * @param statusRequest     状态请求
     * @param operationTypeEnum 操作类型
     * @return 状态机实例
     * @throws Exception 异常
     */
    private StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>
    getStateMachineFromStatusReq(BizOrderStatusRequest statusRequest,
                                 BizOrderOperationTypeEnum operationTypeEnum) throws Exception {
        log.info("Order status change request={},operationType={}", statusRequest, operationTypeEnum);

        if (!StringUtils.equals(statusRequest.getBizCode(), statusRequest.getBizOrderStatusModel().getBizOrderId())) {
            throw new BusinessException(BizOrderErrorCode.ORDER_COMMON_ILLEGAL_ARGUMENT, "请求数据异常");
        }

        // 查询订单，判断请求数据是否合法
        BizOrder bizOrder = bizOrderRepository.selectByBizPrimaryKey(statusRequest.getBizCode());
        if (null == bizOrder
                || !StringUtils.equals(bizOrder.getBizType(), statusRequest.getBizOrderStatusModel().getBizType())
                || !StringUtils.equals(bizOrder.getOrderStatus(), statusRequest.getBizOrderStatusModel().getCurrentOrderStatus())
        ) {
            throw new BusinessException(BizOrderErrorCode.ORDER_COMMON_ILLEGAL_ARGUMENT, "请求数据与订单实际数据不符");
        }

        // 构造状态机模板
        StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> srcStateMachine =
                bizOrderStateMachineBuildFactory.createStateMachine(statusRequest.getBizOrderStatusModel().getBizType(),
                        statusRequest.getBizOrderStatusModel().getSubBizType());

        // 从redis中获取对应的statemachine，并判断当前节点是否可以满足，如果无法从redis中获取对应的的statemachine，则取自DB
        StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine
                = bizOrderRedisStateMachinePersister.restore(srcStateMachine, statusRequest.getBizCode());

        // 由于DB中已持久化，基本上不太可能出现null的情况，目前唯一能想到会出现的情况就是缓存击穿，先抛错
        if (null == stateMachine) {
            throw new BusinessException(BizOrderErrorCode.NO_CORRESPONDING_STATEMACHINE_ERR, "不存在订单对应的状态机实例");
        }
        log.info("order stateMachine info is {}", srcStateMachine);

        return stateMachine;
    }

    /**
     * 获取statemachine实例
     *
     * @param createRequest     状态请求
     * @param operationTypeEnum 操作类型
     * @return 状态机实例
     * @throws Exception 异常
     */
    private StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> getStateMachineFromCreateReq(BizOrderCreateRequest createRequest,
                                                                                                         BizOrderOperationTypeEnum operationTypeEnum) throws Exception {
        log.info("Order create request={},operationType={}", createRequest, operationTypeEnum);

        // 构造状态机模板
        StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> srcStateMachine =
                bizOrderStateMachineBuildFactory.createStateMachine(createRequest.getBizOrderCreateModel().getBizType(),
                        createRequest.getBizOrderCreateModel().getSubBizType());

        if (null == srcStateMachine) {
            throw new BusinessException(BizOrderErrorCode.NO_CORRESPONDING_STATEMACHINE_ERR, "不存在订单对应的状态机实例");
        }

        // 如果是sign，表示订单已存在，需要额外判断并restore状态机；如果是直接create，则不需要处理这些判断
        if (StringUtils.equalsIgnoreCase(BizOrderOperationTypeEnum.SIGN.getOperationType(),
                createRequest.getOperationType())) {
            if (!StringUtils.equals(createRequest.getBizCode(), createRequest.getBizOrderCreateModel().getBizOrderId())) {
                throw new BusinessException(BizOrderErrorCode.ORDER_COMMON_ILLEGAL_ARGUMENT, "请求数据异常");
            }

            // 查询订单，判断请求数据是否合法
            BizOrder bizOrder = bizOrderRepository.selectByBizPrimaryKey(createRequest.getBizCode());
            if (null == bizOrder
                    || !StringUtils.equals(bizOrder.getBizType(), createRequest.getBizOrderCreateModel().getBizType())
            ) {
                throw new BusinessException(BizOrderErrorCode.ORDER_COMMON_ILLEGAL_ARGUMENT, "请求数据与订单实际数据不符");
            }

            // 从redis中获取对应的statemachine，并判断当前节点是否可以满足，如果无法从redis中获取对应的的statemachine，则取自DB
            StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine
                    = bizOrderRedisStateMachinePersister.restore(srcStateMachine, createRequest.getBizOrderCreateModel().getBizOrderId());

            // 由于DB中已持久化，基本上不太可能出现null的情况，目前唯一能想到会出现的情况就是缓存击穿，先抛错
            if (null == stateMachine) {
                throw new BusinessException(BizOrderErrorCode.NO_CORRESPONDING_STATEMACHINE_ERR, "不存在订单对应的状态机实例");
            }

            return stateMachine;
        }

        log.info("order stateMachine info is {}", srcStateMachine);

        return srcStateMachine;
    }
}
