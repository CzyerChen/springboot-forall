/**
 * Author:   claire
 * Date:    2020-12-28 - 21:12
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:12          V1.13.0
 */
package com.statemachine.demo.config;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:12
 */
@Component("bizOrderRedisStateMachineContextPersist")
public class BizOrderRedisStateMachineContextPersist implements StateMachinePersist<BizOrderStatusEnum, BizOrderStatusChangeEventEnum, String> {

    @Autowired
    @Qualifier("redisStateMachineContextRepository")
    private RedisStateMachineContextRepository<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> redisStateMachineContextRepository;

    @Autowired
    private BizOrderStateMachineContextRepository bizOrderStateMachineContextRepository;

    //  加入存储到DB的数据repository, biz_order_state_machine_context表结构：
    //  bizOrderId
    //  contextStr
    //  curStatus
    //  updateTime

    /**
     * Write a {@link StateMachineContext} into a persistent store
     * with a context object {@code T}.
     *
     * @param context    the context
     * @param contextObj the context ojb
     * @throws Exception the exception
     */
    @Override
    @Transactional
    public void write(StateMachineContext<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> context, String contextObj) throws Exception {

        redisStateMachineContextRepository.save(context, contextObj);
        //  save to db
        BizOrderStateMachineContext queryResult = bizOrderStateMachineContextRepository.selectByOrderId(contextObj);

        if (null == queryResult) {
            BizOrderStateMachineContext bosmContext = new BizOrderStateMachineContext(contextObj,
                    context.getState().getStatus(), serialize(context));
            bizOrderStateMachineContextRepository.insertSelective(bosmContext);
        } else {
            queryResult.setCurOrderStatus(context.getState().getStatus());
            queryResult.setContext(serialize(context));
            bizOrderStateMachineContextRepository.updateByPrimaryKeySelective(queryResult);
        }
    }

    /**
     * Read a {@link StateMachineContext} from a persistent store
     * with a context object {@code T}.
     *
     * @param contextObj the context ojb
     * @return the state machine context
     * @throws Exception the exception
     */
    @Override
    public StateMachineContext<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> read(String contextObj) throws Exception {

        StateMachineContext<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> context = redisStateMachineContextRepository.getContext(contextObj);
        //redis 访缓存击穿
        if (null != context && BizOrderConstants.STATE_MACHINE_CONTEXT_ISNULL.equalsIgnoreCase(context.getId())) {
            return null;
        }
        //redis 为空走db
        if (null == context) {
            BizOrderStateMachineContext boSMContext = bizOrderStateMachineContextRepository.selectByOrderId(contextObj);
            if (null != boSMContext) {
                context = deserialize(boSMContext.getContext());
                redisStateMachineContextRepository.save(context, contextObj);
            } else {
                context = new StateMachineContextIsNull();
                redisStateMachineContextRepository.save(context, contextObj);
            }
        }
        return context;
    }

    private String serialize(StateMachineContext<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> context) throws UnsupportedEncodingException {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        kryo.writeObject(output, context);
        output.close();
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    @SuppressWarnings("unchecked")
    private StateMachineContext<BizOrderStatusEnum, BizOrderStatusChangeEventEnum> deserialize(String data) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        Input input = new Input(in);
        return kryo.readObject(input, StateMachineContext.class);
    }

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {

        @SuppressWarnings("rawtypes")
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer());
            kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
            kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
            return kryo;
        }
    };
}