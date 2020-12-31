/**
 * Author:   claire
 * Date:    2020-12-28 - 21:22
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:22          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:22
 */
@Component("bizOrderCreateBizManager")
@Slf4j
public class BizOrderCreateBizManagerImpl extends AbstractBizManagerImpl<BizOrderCreateRequest, BizOrderCreateResponse> {

    @Resource
    private BaseConvertor<BizOrderCreateModel, BizOrder> createBizOrderConvertor;

    @Resource
    private BaseConvertor<BizOrderExtendsCreateModel, BizOrderExtends> createBizOrderExtendsConvertor;

    @Resource
    private BaseConvertor<BizOrderChannelCreateModel, BizOrderChannel> createBizOrderChannelConvertor;

    @Resource
    private BaseConvertor<BizOrderActivityCreateModel, BizOrderActivity> createBizOrderActivityConvertor;

    @Autowired
    private BizOrderRepository bizOrderRepository;

    @Autowired
    private BizOrderExtendsRepository bizOrderExtendsRepository;

    @Autowired
    private BizOrderChannelRepository bizOrderChannelRepository;

    @Autowired
    private BizOrderActivityRepository bizOrderActivityRepository;

    @Autowired
    private BizOrderLogEventPublisher bizOrderLogEventPublisher;

    @Autowired
    private FinMerchantContractQueryService contractQueryService;

    /**
     * 实际的业务操作
     *
     * @param request 业务请求
     * @return 结果
     */
    @Override
    public BizOrderCreateResponse doProcess(BizOrderCreateRequest request, StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum>... stateMachines) throws Exception {
        BizOrderCreateModel createModel = request.getBizOrderCreateModel();

        ...... // 校验逻辑

        // 构造对应的订单模型. 同时修改
        BizOrder bizOrder = createBizOrderConvertor.modelToEntityConvertor(createModel);
        bizOrder.setCallSystem(request.getCallSystem());

        // MDC日志埋点
        LogUtil.setBizId(bizOrder.getBizOrderId());

        // 对于XXX业务，在创建订单时由于没有详细金额，而是在签约是才把资金等信息传递过来，前期对于传递过来的数据不做处理（无效）
        if (!BizOrderBizTypeEnum.isIn(bizOrder.getBizType(), BizOrderBizTypeEnum.EMPLOAN) &&
                CollectionUtils.isNotEmpty(request.getSubBizOrderCreateModels())) {
            // 判断主订单中金额是否等于所有子订单金额
            BigDecimal totalAmount = bizOrder.getRealAmount();
            BigDecimal totalSumFromSub = request.getSubBizOrderCreateModels().parallelStream().map(model ->
                    BigDecimal.valueOf(model.getRealAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if(!totalAmount.equals(totalSumFromSub)){
                throw new BusinessException(BizOrderErrorCode.ORDER_AMOUNT_NOT_MATCH,"主子订单金额不匹配");
            }

            bizOrder.setOrderLevel(BizOrderLevelEnum.MAIN.getOrderLevel()); // 主订单

            request.getSubBizOrderCreateModels().parallelStream().forEach(subOrderModel -> {
                BizOrder subBizOrder = createBizOrderConvertor.modelToEntityConvertor(subOrderModel);
                // 重新设置parentId及orderLevel
                subBizOrder.setOrderLevel(BizOrderLevelEnum.DETAIL.getOrderLevel());
                subBizOrder.setParentId(bizOrder.getBizOrderId());

                bizOrderRepository.insertSelective(subBizOrder);
            });
        }
        bizOrderRepository.insertSelective(bizOrder);

        ......

        BizOrderCreateResponse createResponse = new BizOrderCreateResponse();
        createResponse.setBizOrderId(bizOrder.getBizOrderId());

        // send log event
        Map attrMap = com.google.common.collect.Maps.newHashMap();
        attrMap.put(AttributesKeyEnum.TARGET_STATUS.getShortKeyName(), createModel.getOrderStatus());
        attrMap.put(AttributesKeyEnum.CALL_SYSTEM.getShortKeyName(), request.getCallSystem());

        // 发送记录日志的event
        bizOrderLogEventPublisher.bizOrderEventPublish(bizOrder,
                request.getOperationType(),
                BizOrderStatusEnum.CREATE.getStatus(),
                attrMap);

        return createResponse;
    }

}