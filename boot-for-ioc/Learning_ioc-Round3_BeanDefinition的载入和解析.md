### BeanDefinition的载入和解析
- 载入过程相当于把定义的BeanDefinition在IoC容器中转化为一个Spring内部数据结构的过程
- IoC容器对Bean的管理和依赖注入功能的实现是通过对其持有的BeanDefinition进行各种相关的操作来完成的
- 这些BeanDefinition数据在IoC容器中通过一个HashMap来保持和维护
- DefaultListableBeanFacotry 通过默认的Bean工厂加载的Bean ，就会被存在Map中，包括bean的名字，类型，依赖，实例，引用，等等
```text
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {
    @Nullable
    private static Class<?> javaxInjectProviderClass;
    private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories;
    @Nullable
    private String serializationId;
    private boolean allowBeanDefinitionOverriding = true;
    private boolean allowEagerClassLoading = true;
    @Nullable
    private Comparator<Object> dependencyComparator;
    private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
    private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap(16);
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);
    private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap(64);
    private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap(64);
    private volatile List<String> beanDefinitionNames = new ArrayList(256);
    private volatile Set<String> manualSingletonNames = new LinkedHashSet(16);
    @Nullable
    private volatile String[] frozenBeanDefinitionNames;
    private volatile boolean configurationFrozen = false;
```
- 之前讲的ApplicationContext 通过加载了BeanDefinition后，需要根据配置进行依赖注入和实例化，
```text
public void refresh() throws BeansException, IllegalStateException {
        Object var1 = this.startupShutdownMonitor;
        //锁住全局的监视器
        synchronized(this.startupShutdownMonitor) {
        //准备好Context,验证refresh开始的时间，验证所需的参数的合法性
            this.prepareRefresh();
            //在子类中执行refreshBeanFactory 只要是对Resource进行定位，然后BeanDefinitionReader会进行加载并使用
            //将其解析为Document对象，然后DocumentReader按照Spring Bean的规则进行解析
            ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
            //Bean的初始化做一些准备工作
            this.prepareBeanFactory(beanFactory);

            try {
            //后只处理器
                this.postProcessBeanFactory(beanFactory);
                //调用后置处理器，定义在Bean的定义时就像容器注册的，比如实现InitializingBean
                this.invokeBeanFactoryPostProcessors(beanFactory);
                //注册后只处理器
                this.registerBeanPostProcessors(beanFactory);
                //初始化上下文消息源
                this.initMessageSource();
                //初始化上下文事件机制
                this.initApplicationEventMulticaster();
                //初始化上下文子类的Bean
                this.onRefresh();
                //检查listener 并把他们向容器注册
                this.registerListeners();
                //实例化Bean 或者遵循lazy init
                //依赖注入在第一次getBean的时候实现，或者在该方法内完成
                this.finishBeanFactoryInitialization(beanFactory);
                //发布容器时间，结束refresh过程
                this.finishRefresh();
            } catch (BeansException var9) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + var9);
                }
                //出现异常，回收生成的单例Bean
                this.destroyBeans();
                //重置active标志
                this.cancelRefresh(var9);
                throw var9;
            } finally {
                this.resetCommonCaches();
            }

        }
    }
```
  
