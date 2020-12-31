/**
 * Author:   claire
 * Date:    2020-12-28 - 21:16
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:16          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:16
 */
@Component
@Slf4j
public class WYLoanBizOrderStateMachineBuilder extends EnumStateMachineConfigurerAdapter implements BizOrderStateMachineBuilder {

......

    @Override
    public String getName() {
        return WYLOAN_BUILDER_NAME;
    }

    @Override
    public StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> build(BeanFactory beanFactory) throws Exception {

        StateMachineBuilder.Builder<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .autoStartup(true)
                .beanFactory(beanFactory)
                .machineId("WYLoanStateMachineId");

        // 初始化状态机，并指定状态集合
        // 详细说明下，由于XXX将用户实名+申请额度的流程都统一承载在订单维度，所以这里有个分支流程处理相关数据
        // 包括实名、业务审核、资料补全、证件上传、贷前额度审核等节点
        // 这块流程与订单流程全都耦合在一起，拆出来工作量及代价都比较大，所以在订单系统中统一维护起来
        // 故XXX的业务状态最多，也最复杂
        builder.configureStates()
                .withStates()
                .initial(CREATE)
                .choice(WYD_INITIAL_JUMP)
                .choice(CHECK_COMPLEMENT)
                .choice(CHECK_UPLOAD)
                .choice(IN_DEAL_RISK_AUDITING)
                .choice(REPAYING) // 还款ing 对应的不同结果

                .end(CANCEL)
                .end(CLOSE)
                .end(SUCCESS)
                .states(EnumSet.allOf(BizOrderStatusEnum.class)) // 所有状态，避免有遗漏
        ;

        // 指定状态机有哪些节点，即迁移动作
        builder.configureTransitions()
                // XXX的创建，并不是CREATE状态，而是为待实名WAIT_REAL_NAME_AUTH或者待借款WAIT_BORROW状态，
                // 所以需要虚拟节点，自己跳转
                .withExternal()
                .source(CREATE)
                .target(WYD_INITIAL_JUMP)
                .event(BizOrderStatusChangeEventEnum.EVT_CREATE)
                .action(orderCreateAction, errorHandlerAction)

                .and()
                .withChoice()
                .source(WYD_INITIAL_JUMP)
                .first(WAIT_REAL_NAME_AUTH, needNameAuthGurad(), needNameAuthAction)
                .then(WAIT_BORROW, toWaitBorrowGuard(), waitBorrowAction)
                .last(CREATE)

                /** 待实名WAIT_REAL_NAME_AUTH 可以到达的节点 **/
                // cancel
                .and().withExternal()
                .source(WAIT_REAL_NAME_AUTH)
                .target(CANCEL)
                .event(EVT_CANCEL)
                .action(cancelAction, errorHandlerAction)

                // close
                .and().withExternal()
                .source(WAIT_REAL_NAME_AUTH)
                .target(CLOSE)
                .event(EVT_SYS_CLOSE)
                .action(closeAction, errorHandlerAction)

                // 实名，下一步是待hr审核
                .and().withExternal()
                .source(WAIT_REAL_NAME_AUTH)
                .target(WAIT_BIZ_AUDIT)
                .event(EVT_NAME_AUTH)
                .action(nameAuthAction, errorHandlerAction)

                /** 待BIZ审核可到达的节点 **/
                // 关闭
                .and().withExternal()
                .source(WAIT_BIZ_AUDIT)
                .target(CLOSE)
                .event(EVT_REFUSE).guard(toCloseGuard())
                .action(closeAction, errorHandlerAction)

                // BIZ审核通过，到待补全资料
                .and().withExternal()
                .source(WAIT_BIZ_AUDIT)
                .target(WAIT_COMPLEMENT)
                .event(EVT_AUDIT)
                .action(hrAuditAction, errorHandlerAction)

                /** 补全资料可以到达的节点 **/
                // 待上传证件
                .and().withExternal()
                .source(WAIT_COMPLEMENT)
                .target(CHECK_COMPLEMENT)
                .event(EVT_COMPLEMENT)

                // choice
                .and().withChoice()
                .source(CHECK_COMPLEMENT)
                .first(WAIT_COMPLEMENT, retryCompleteGuard(), retryCompleteAction)
                .last(WAIT_UPLOAD_IMG, completeAction)

                /** 上传证件可以到达的节点 **/
                .and().withExternal()
                .source(WAIT_UPLOAD_IMG)
                .target(CHECK_UPLOAD)
                .event(EVT_UPLOAD_IMG)

                .and().withChoice()
                .source(CHECK_UPLOAD)
                .first(WAIT_UPLOAD_IMG, retryUploadGuard(), retryUploadAction)
                .last(WAIT_BEF_DEAL_RISK_AUDIT, uploadAction)

                /** 贷前审核可以到达的节点 **/
                // 关单
                .and().withExternal()
                .source(WAIT_BEF_DEAL_RISK_AUDIT)
                .target(CLOSE)
                .event(EVT_AUDIT)
                .guard(toCloseGuard())
                .action(closeAction, errorHandlerAction)

                // 跳转到待借款
                .and().withExternal()
                .source(WAIT_BEF_DEAL_RISK_AUDIT)
                .target(WAIT_BORROW)
                .event(EVT_AUDIT)
                .guard(toWaitBorrowGuard())
                .action(befDealRiskAction, errorHandlerAction)


                /** 待借款可以到达的节点 **/
                // 签约环节补充所有待完善数据，所以是从createService中发起此流程
                .and().withExternal()
                .source(WAIT_BORROW)
                .target(SIGNED)
                .event(EVT_SIGN)
                .action(signAction, errorHandlerAction)

                .and().withExternal()
                .source(WAIT_BORROW)
                .target(IN_DEAL_RISK_AUDITING)
                .event(EVT_AUDIT)

                .and().withChoice()
                .source(IN_DEAL_RISK_AUDITING)
                .first(CLOSE, toCloseGuard(), closeAction)
                .last(WAIT_SIGN, toWaitSignAction)

                .and().withExternal()
                .source(WAIT_SIGN)
                .target(SIGNED)
                .event(EVT_SIGN)
                .action(signAction, errorHandlerAction)// -- to be complete

                /** 签约可以到达的节点 **/
                .and().withExternal()
                .source(SIGNED)
                .target(LOANING)
                .event(EVT_LOAN)
                .action(loanAction, errorHandlerAction)

                /* 需要外部触发，暂时不用choice了，无法自己内部决定
                .and().withChoice()
                .source(LOANING)
                .first(CLOSE, loanFailGuard(), closeAction)
                .last(LOANED, loanSuccGuard(), loanAction())*/
                .and().withExternal()
                .source(LOANING)
                .target(CLOSE)
                .event(EVT_LOAN_FAILED)
                .action(closeAction, errorHandlerAction)

                .and().withExternal()
                .source(LOANING)
                .target(LOANED)
                .event(EVT_LOAN_SUCC)
                .action(loanSuccAction, errorHandlerAction)


                /** 放款成功可以到达的节点 **/
                .and().withExternal()
                .source(LOANED)
                .target(BILL_GEN)
                .event(EVT_GEN_BILL)
                .action(genBillAction, errorHandlerAction)

                /** 生成账单 到逾期/还款 **/
                .and().withExternal()
                .source(BILL_GEN)
                .target(OVERDUE)
                .event(EVT_OVERDUE)
                .action(overdueAction, errorHandlerAction)

                .and().withExternal()
                .source(BILL_GEN)
                .target(REPAYING)
                .event(EVT_REPAY)

                // OVERDUE可以到达的节点
                .and().withExternal()
                .source(OVERDUE)
                .target(REPAYING)
                .event(EVT_REPAY)

                .and().withExternal()
                .source(PART_REPAID)
                .target(OVERDUE)
                .event(EVT_OVERDUE)
                .action(overdueAction, errorHandlerAction)

                .and().withExternal()
                .source(PART_REPAID)
                .target(REPAYING)
                .event(EVT_REPAY)

                // 还款过程，repaying可以到达的节点
                .and().withChoice()
                .source(REPAYING)
                .first(PART_REPAID, partRepayGuard(), partRepayAction) // 部分还款，到本身，状态不变
                .last(REPAID, repaidAction) // 全部还款

                // repayed 可以到达的节点-success 销账
                .and().withExternal()
                .source(REPAID)
                .target(SUCCESS)
                .event(EVT_TOSUCCESS)
                .action(successAction, errorHandlerAction)
        ;


        return builder.build();
    }

    /**
     * 判断是否要到待借款状态
     *
     * @return 如果需要，返回true，否则返回false
     */
    private Guard<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> toWaitBorrowGuard() {
        return context -> {
            String finalOrderStatus = (String) context.getMessageHeader(BizOrderConstants.FINAL_STATUS_KEY);

            if (BizOrderStatusEnum.equals(finalOrderStatus, BizOrderStatusEnum.WAIT_BORROW)) {
                log.debug("toWaitBorrowGurad return true");
                return true;

            }
            log.debug("toWaitBorrowGurad return false");
            return false;
        };
    }

    /**
     * 判断是否需要用户实名
     *
     * @return 如果需要，返回true，否则返回false
     */
    private Guard<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> needNameAuthGurad() {
        return context -> {
            String finalOrderStatus = (String) context.getMessageHeader(BizOrderConstants.FINAL_STATUS_KEY);

            if (BizOrderStatusEnum.equals(finalOrderStatus, BizOrderStatusEnum.WAIT_REAL_NAME_AUTH)) {
                log.debug("needNameAuthGurad return true");
                return true;
            }

            log.debug("needNameAuthGurad return false");
            return false;
        };
    }

    private Guard<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> retryUploadGuard() {

        return context -> {
            // 判断请求参数中的targetStatus是否为需要重试upload
            String finalOrderStatus = (String) context.getMessageHeader(BizOrderConstants.FINAL_STATUS_KEY);

            if (BizOrderStatusEnum.equals(finalOrderStatus, BizOrderStatusEnum.WAIT_UPLOAD_IMG)) {
                log.debug("retryUploadGuard return true");
                return true;
            }

            log.debug("retryUploadGuard return false");
            return false;
        };
    }

    /**
     * 判断是否需要重试补全资料
     *
     * @return 结果
     */
    private Guard<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> retryCompleteGuard() {

        return context -> {
            // 判断请求参数中的targetStatus是否为需要重试complete补全
            String finalOrderStatus = (String) context.getMessageHeader(BizOrderConstants.FINAL_STATUS_KEY);

            if (BizOrderStatusEnum.equals(finalOrderStatus, BizOrderStatusEnum.WAIT_COMPLEMENT)) {
                log.debug("retryCompleteGuard return true");
                return true;
            }

            log.debug("retryCompleteGuard return false");
            return false;

        };
    }

    private Guard<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> toCloseGuard() {

        return context -> {
            // 判断请求参数中的targetStatus是否为关单
            String finalOrderStatus = (String) context.getMessageHeader(BizOrderConstants.FINAL_STATUS_KEY);
            if (BizOrderStatusEnum.equals(finalOrderStatus, BizOrderStatusEnum.CLOSE)) {
                log.debug("toCloseGuard return true");
                return true;
            }

            log.debug("toCloseGuard return false");
            return false;
        };
    }


    private Guard<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> partRepayGuard() {
        return context -> {
            // 判断请求参数中的targetStatus是否为关单
            String finalOrderStatus = (String) context.getMessageHeader(BizOrderConstants.FINAL_STATUS_KEY);
            if (BizOrderStatusEnum.equals(finalOrderStatus, BizOrderStatusEnum.PART_REPAID)) {
                log.debug("partRepayGuard return true");
                return true;
            }

            log.debug("partRepayGuard return false");
            return false;
        };
    }

    @Autowired
    @Qualifier("errorHandlerAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> errorHandlerAction;

    @Resource(name = "orderCreateAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> orderCreateAction;

    @Resource(name = "successAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> successAction;

    @Resource(name = "cancelAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> cancelAction;

    @Resource(name = "closeAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> closeAction;

    @Resource(name = "nameAuthAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> nameAuthAction;

    @Resource(name = "needNameAuthAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> needNameAuthAction;

    @Resource(name = "waitBorrowAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> waitBorrowAction;

    @Resource(name = "hrAuditAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> hrAuditAction;

    @Resource(name = "retryCompleteAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> retryCompleteAction;

    @Resource(name = "completeAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> completeAction;

    @Resource(name = "retryUploadAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> retryUploadAction;

    @Resource(name = "uploadAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> uploadAction;

    @Resource(name = "befDealRiskAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> befDealRiskAction;

    // sign时需要补充所有必需业务数据
    @Resource(name = "signAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> signAction;

    @Resource(name = "toWaitSignAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> toWaitSignAction;

    @Resource(name = "loanAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> loanAction;

    @Resource(name = "loanSuccAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> loanSuccAction;

    @Resource(name = "genBillAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> genBillAction;

    @Resource(name = "overdueAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> overdueAction;

    @Resource(name = "partRepayAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> partRepayAction;

    @Resource(name = "repaidAction")
    private Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> repaidAction;

    /**
     * 异常处理Action
     *
     * @return action对象
     */
    @Bean(name = "errorHandlerAction", autowire = Autowire.BY_TYPE)
    public Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> errorHandlerAction() {

        return context -> {
            RuntimeException exception = (RuntimeException) context.getException();
            log.error("stateMachine execute error = ", exception);
            context.getStateMachine()
                    .getExtendedState().getVariables()
                    .put(RuntimeException.class, exception);
        };
    }

    /**
     * 创建订单
     * @return
     */
    @Bean(name = "orderCreateAction", autowire = Autowire.BY_TYPE)
    public Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> orderCreateAction(){

        return context -> {
            // 订单创建相关请求
            BizOrderCreateRequest createRequest = (BizOrderCreateRequest) context.getMessageHeader(BizOrderConstants.BIZORDER_CONTEXT_CREATE_KEY);

            // 从context中获取状态机
            StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine = context.getStateMachine();

            log.info("order info={},stateMachine id={},uuid={},jump from {} to sign status",
                    createRequest,
                    stateMachine.getId(),
                    stateMachine.getUuid(),
                    stateMachine.getState().getId());

            bizOrderCreateBizManager.process(createRequest);
        };
    }


    /**
     * 自动跳转到close的Action
     * <p>
     * 比如超时未处理，希望关单，可以使用此action
     *
     * @return action对象
     */
    @Bean(name = "toCloseAction",autowire = Autowire.BY_TYPE)
    public Action<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> toCloseAction() {
        return context -> {
            StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine = context.getStateMachine();

            BizOrderStatusRequest statusRequest = (BizOrderStatusRequest) context.getMessageHeader(BizOrderConstants.BIZORDER_CONTEXT_KEY);

            log.info("order info={},stateMachine id={},uuid={}, jump from {} to toClose status",
                    statusRequest,
                    stateMachine.getId(),
                    stateMachine.getUuid(),
                    stateMachine.getState().getId());

            bizOrderToCloseBizManager.process(statusRequest);

        };
    }

}