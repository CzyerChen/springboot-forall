/**
 * Author:   claire
 * Date:    2020-12-28 - 21:17
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:17          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:17
 */
@Component
public class BizOrderStateMachineBuildFactory implements InitializingBean {

    @Autowired
    private List<BizOrderStateMachineBuilder> builders;

    @Autowired
    private BeanFactory beanFactory;

    /**
     * 用来存储builder-name及builder的map
     */
    private Map<String, BizOrderStateMachineBuilder> builderMap = Maps.newConcurrentMap();

    /**
     * 用于存储bizType+subBizType 与 builder-name的集合
     */
    private Map<String, String> bizTypeBuilderMap = Maps.newConcurrentMap();


    /**
     * State machine instantiation is a relatively expensive operation so it is better to try to pool instances
     * instead of instantiating a new instance with every request
     * <p>
     * 不过目前先继续每次请求过来进行创建，后续再考虑池化操作
     * <p>
     * 创建statemachine
     *
     * @param bizType
     * @param subBizType
     * @return
     */
    public StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> createStateMachine(String bizType, String subBizType) {
        if (StringUtils.isBlank(subBizType)) {
            subBizType = "";
        }
        String key = StringUtils.trim(bizType) + StringUtils.trim(subBizType);

        String builderName = bizTypeBuilderMap.get(key);
        if (StringUtils.isBlank(builderName)) {
            throw new BusinessException(BizOrderErrorCode.NO_CORRESPONDING_STATEMACHINE_BUILDER, "当前业务没有对应的状态机配置，请检查");
        }

        return createStateMachine(builderName);
    }

    /**
     * 创建stateMachine
     * @param builderName
     * @return
     */
    public StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> createStateMachine(String builderName) {

        BizOrderStateMachineBuilder builder = builderMap.get(builderName);

        StateMachine<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> stateMachine = null;
        try {
            stateMachine = builder.build(beanFactory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BizOrderErrorCode.ORDER_GENERIC_EXCEPTION, e.getLocalizedMessage());
        }

        return stateMachine;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        builderMap = builders.stream().collect(Collectors.toMap(
                BizOrderStateMachineBuilder::getName,
                Function.identity()
        ));

        // 暂时将bizType和subBizType XXX-单笔授信作为key，绑定对应的XXX状态机，后续还需要绑定别的业务
        bizTypeBuilderMap.put(BizOrderBizTypeEnum.EMPLOAN.getOrderBizType() + BizOrderSubTypeEnum.SINGLE_AUTH.getBizSubType(),
                WYLOAN_BUILDER_NAME);
        // XXX 不区分子业务类型
        bizTypeBuilderMap.put(BizOrderBizTypeEnum.EMPLOAN.getOrderBizType(), WYLOAN_BUILDER_NAME);


    }
}