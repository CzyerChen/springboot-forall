> 由于面试被问到，那就逼迫我把以前想放放的问题搞清楚了
[学习资料](https://blog.csdn.net/isea533/article/details/78072133)


### Differences between @Component @Configuration
- 我们知道的相同点：都可以把一个类进行注入，给容器管理
- 那有哪些是不知道的区别呢？
-注入的流程不一样，注入的对象是一个还是多个，怎么样实现注入的是同一个对象的操作

#### @Configuration
```text
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component  //它就是一个Component
public @interface Configuration {
    @AliasFor(
        annotation = Component.class 
    )
    String value() default "";
}

```
- 因为本质就是Component ，所以可以轻松地被注入
- @Configuration 标记的类必须符合下面的要求：
    - 配置类必须以类的形式提供（不能是工厂方法返回的实例），允许通过生成子类在运行时增强（cglib 动态代理）。
    - 配置类不能是 final 类（没法动态代理）。
    - 配置注解通常为了通过 @Bean 注解生成 Spring 容器管理的类，
    - 配置类必须是非本地的（即不能在方法中声明，不能是 private）。
    - 任何嵌套配置类都必须声明为static。
    - @Bean 方法可能不会反过来创建进一步的配置类（也就是返回的 bean 如果带有 @Configuration，也不会被特殊处理，只会作为普通的 bean）

#### @Configuration 如何加载
- 在Bean的加载流程中，有一个环节是，如果实现了BeanFactoryPostProcessor 需要在类加载完成后，初始化之前进行对应的实现
- ConfigurationClassPostProcessor，这个后置处理程序专门处理带有 @Configuration 注解的类，这个程序会在 bean 定义加载完成后，在 bean 初始化前进行处理
- 使用 cglib 动态代理增强类，而且是对其中带有 @Bean 注解的方法进行处理
- 具体流程：
```text
1. 从ConfigurationClassPostProcessor开始，我们知道它被后置处理器捕获了

2. postProcessBeanFactory方法
 public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        int factoryId = System.identityHashCode(beanFactory);
        //后置处理器工厂已经被调用
        if (this.factoriesPostProcessed.contains(factoryId)) {
            throw new IllegalStateException("postProcessBeanFactory already called on this post-processor against " + beanFactory);
        } else {//没有被调用
            this.factoriesPostProcessed.add(factoryId);
            if (!this.registriesPostProcessed.contains(factoryId)) {
                this.processConfigBeanDefinitions((BeanDefinitionRegistry)beanFactory);
            }
            //需要被CGLIB动态代理增强的类
            this.enhanceConfigurationClasses(beanFactory);
            beanFactory.addBeanPostProcessor(new ConfigurationClassPostProcessor.ImportAwareBeanPostProcessor(beanFactory));
        }
    }

3.enhanceConfigurationClasses 方法
 public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory) {
        Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap();
        //获取该类中的实体说明名字
        String[] var3 = beanFactory.getBeanDefinitionNames();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String beanName = var3[var5];
            //获取BeanDefinition
            BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
            //跳过
            if (ConfigurationClassUtils.isFullConfigurationClass(beanDef)) {
                if (!(beanDef instanceof AbstractBeanDefinition)) {
                    throw new BeanDefinitionStoreException("Cannot enhance @Configuration bean definition '" + beanName + "' since it is not stored in an AbstractBeanDefinition subclass");
                }

                if (this.logger.isWarnEnabled() && beanFactory.containsSingleton(beanName)) {
                    this.logger.warn("Cannot enhance @Configuration bean definition '" + beanName + "' since its singleton instance has been created too early. The typical cause is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor return type: Consider declaring such methods as 'static'.");
                }

                configBeanDefs.put(beanName, (AbstractBeanDefinition)beanDef);
            }
        }

//Bean非空，需要进行增强和代理
        if (!configBeanDefs.isEmpty()) {
          //我们常见的CGLIB的增强类
            ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
            Iterator var11 = configBeanDefs.entrySet().iterator();

            while(var11.hasNext()) {
                Entry<String, AbstractBeanDefinition> entry = (Entry)var11.next();
                AbstractBeanDefinition beanDef = (AbstractBeanDefinition)entry.getValue();
                beanDef.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);

                try {
                    Class<?> configClass = beanDef.resolveBeanClass(this.beanClassLoader);
                    if (configClass != null) {
                    //增强类
                        Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
                        if (configClass != enhancedClass) {
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug(String.format("Replacing bean definition '%s' existing class '%s' with enhanced class '%s'", entry.getKey(), configClass.getName(), enhancedClass.getName()));
                            }

                            beanDef.setBeanClass(enhancedClass);
                        }
                    }
                } catch (Throwable var9) {
                    throw new IllegalStateException("Cannot load configuration class: " + beanDef.getBeanClassName(), var9);
                }
            }

        }
    }
    
    4.enhance方法
    public Class<?> enhance(Class<?> configClass, @Nullable ClassLoader classLoader) {
            if (ConfigurationClassEnhancer.EnhancedConfiguration.class.isAssignableFrom(configClass)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Ignoring request to enhance %s as it has already been enhanced. This usually indicates that more than one ConfigurationClassPostProcessor has been registered (e.g. via <context:annotation-config>). This is harmless, but you may want check your configuration and remove one CCPP if possible", configClass.getName()));
                }
    
                return configClass;
            } else {
            //真正创建增强类
            }
                Class<?> enhancedClass = this.createClass(this.newEnhancer(configClass, classLoader));
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Successfully enhanced %s; enhanced class name is: %s", configClass.getName(), enhancedClass.getName()));
                }
    
                return enhancedClass;
            }
            
     
     
     5. 新建一个增强类
     private Enhancer newEnhancer(Class<?> configSuperClass, @Nullable ClassLoader classLoader) {
             Enhancer enhancer = new Enhancer();
             enhancer.setSuperclass(configSuperClass);//需要增强的类
             enhancer.setInterfaces(new Class[]{ConfigurationClassEnhancer.EnhancedConfiguration.class});//接口
             enhancer.setUseFactory(false);
             enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);//命名规范
             enhancer.setStrategy(new ConfigurationClassEnhancer.BeanFactoryAwareGeneratorStrategy(classLoader));//策略
             enhancer.setCallbackFilter(CALLBACK_FILTER);//回调
             enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
             return enhancer;
         }
       
     6.上面的流程，我们已经建立好了增强类，那么实际的访问最重要就是回调里面，怎么描述方法的调用啦
     CALLBACK_FILTER
     静态块中描述的内容，给出了两个增强
     static {
             CALLBACKS = new Callback[]{new ConfigurationClassEnhancer.BeanMethodInterceptor(), new ConfigurationClassEnhancer.BeanFactoryAwareMethodInterceptor(), NoOp.INSTANCE};
             CALLBACK_FILTER = new ConfigurationClassEnhancer.ConditionalCallbackFilter(CALLBACKS);
             logger = LogFactory.getLog(ConfigurationClassEnhancer.class);
             objenesis = new SpringObjenesis();
         }
         
      7.BeanMethodInterceptor，对于出现@Bean的处理
      首先是isMatch
       public boolean isMatch(Method candidateMethod) {
                  return candidateMethod.getDeclaringClass() != Object.class && BeanAnnotationHelper.isBeanAnnotated(candidateMethod);
              }

      8.BeanFactoryAwareMethodInterceptor,实现BeanFactoryAware 接口
       public boolean isMatch(Method candidateMethod) {
                  return candidateMethod.getName().equals("setBeanFactory") && candidateMethod.getParameterCount() == 1 && BeanFactory.class == candidateMethod.getParameterTypes()[0] && BeanFactoryAware.class.isAssignableFrom(candidateMethod.getDeclaringClass());
              }
      
      9.最核心的方法调用阶段 intercept,为什么Configuration能够注入同一个对象
      private static class BeanMethodInterceptor implements MethodInterceptor, ConfigurationClassEnhancer.ConditionalCallback {
         @Nullable
        public Object intercept(Object enhancedConfigInstance, Method beanMethod, Object[] beanMethodArgs, MethodProxy cglibMethodProxy) throws Throwable {
        //获取对应的BeanFactory
            ConfigurableBeanFactory beanFactory = this.getBeanFactory(enhancedConfigInstance);
            //获取BeanName 或者@Bean有指定
            String beanName = BeanAnnotationHelper.determineBeanNameFor(beanMethod);
            //确认Bean的代理范围
            Scope scope = (Scope)AnnotatedElementUtils.findMergedAnnotation(beanMethod, Scope.class);
            if (scope != null && scope.proxyMode() != ScopedProxyMode.NO) {
                String scopedBeanName = ScopedProxyCreator.getTargetBeanName(beanName);
                if (beanFactory.isCurrentlyInCreation(scopedBeanName)) {
                    beanName = scopedBeanName;
                }
            }

           //判断是否存在当前Bean
            if (this.factoryContainsBean(beanFactory, "&" + beanName) && this.factoryContainsBean(beanFactory, beanName)) {
                Object factoryBean = beanFactory.getBean("&" + beanName);
                if (!(factoryBean instanceof ScopedProxyFactoryBean)) {
                    return this.enhanceFactoryBean(factoryBean, beanMethod.getReturnType(), beanFactory, beanName);
                }
            }

           //如果是当前正在执行的方法
            if (this.isCurrentlyInvokedFactoryMethod(beanMethod)) {//是当前类需要被调用
                if (ConfigurationClassEnhancer.logger.isWarnEnabled() && BeanFactoryPostProcessor.class.isAssignableFrom(beanMethod.getReturnType())) {
                    ConfigurationClassEnhancer.logger.warn(String.format("@Bean method %s.%s is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. This will result in a failure to process annotations such as @Autowired, @Resource and @PostConstruct within the method's declaring @Configuration class. Add the 'static' modifier to this method to avoid these container lifecycle issues; see @Bean javadoc for complete details.", beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName()));
                }
                //常规的通过MenthodProxy的invokeSuper进行调用
                return cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
            } else {
                return this.resolveBeanReference(beanMethod, beanMethodArgs, beanFactory, beanName);
            }
        }
```
- 通过以上的理论，我们看看
```text
@Configuration
public class AopBeanConfig {

    @Bean
    public Country country(){
        return new Country();
    }

    @Bean
    public UserInfo userInfo(){
        return new UserInfo(country());//这个的country()会使用已经在内存中country Bean
    }

}
```

### @Component的情况有点不一样
- 因为@Configuration有ConfigurationClassPostProcessor类的支持，使用了AOP动态代理增强的模式，可是Component没有
- 因而上面的注意单一对象问题，会出现在Component注解上
```text
@Component
public class AopBeanConfig {

    @Bean
    public Country country(){
        return new Country();
    }

    @Bean
    public UserInfo userInfo(){
        return new UserInfo(country());//这个并有用已经存在的对象，而是又注入了一个
    }

}
```
- 想要使用单一的对象，可以这么做
```text
@Configuration
public class AopBeanConfig {
    @Autowired
    private Country country;

    @Bean
    public Country country(){
        return new Country();
    }

    @Bean
    public UserInfo userInfo(){
        return new UserInfo(country);//同一个，就是注入的那一个
    }

}
```
