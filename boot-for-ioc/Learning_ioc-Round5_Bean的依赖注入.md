- 手动调用getBean,会触发依赖注入
```text
  public Object getBean(String name) throws BeansException {
        return this.doGetBean(name, (Class)null, (Object[])null, false);
    }

    public <T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException {
        return this.doGetBean(name, requiredType, (Object[])null, false);
    }

    public Object getBean(String name, Object... args) throws BeansException {
        return this.doGetBean(name, (Class)null, args, false);
    }

    public <T> T getBean(String name, @Nullable Class<T> requiredType, @Nullable Object... args) throws BeansException {
        return this.doGetBean(name, requiredType, args, false);
    }

    //真正作用的方法
    protected <T> T doGetBean(String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly) throws BeansException {
        String beanName = this.transformedBeanName(name);
        //获取缓存中的实例
        Object sharedInstance = this.getSingleton(beanName);
        Object bean;
        if (sharedInstance != null && args == null) {
            if (this.logger.isDebugEnabled()) {
                if (this.isSingletonCurrentlyInCreation(beanName)) {
                    this.logger.debug("Returning eagerly cached instance of singleton bean '" + beanName + "' that is not fully initialized yet - a consequence of a circular reference");
                } else {
                    this.logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
                }
            }

//利用BeanFactory获取实例  object = AccessController.doPrivileged(factory::getObject, acc);最终获取没有缓存的新势力
            bean = this.getObjectForBeanInstance(sharedInstance, name, beanName, (RootBeanDefinition)null);
        } else {
            if (this.isPrototypeCurrentlyInCreation(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName);
            }

// // 检查IoC容器中的BeanDefinition是否存在，若在当前工厂不存在则去顺着双亲BeanFactory链一直向上找
            BeanFactory parentBeanFactory = this.getParentBeanFactory();
            if (parentBeanFactory != null && !this.containsBeanDefinition(beanName)) {
                String nameToLookup = this.originalBeanName(name);
                if (parentBeanFactory instanceof AbstractBeanFactory) {
                    return ((AbstractBeanFactory)parentBeanFactory).doGetBean(nameToLookup, requiredType, args, typeCheckOnly);
                }

                if (args != null) {
                    return parentBeanFactory.getBean(nameToLookup, args);
                }

                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }

            if (!typeCheckOnly) {
                this.markBeanAsCreated(beanName);
            }

            try {
            ////根据Bean的名字取得BeanDefinition 
                RootBeanDefinition mbd = this.getMergedLocalBeanDefinition(beanName);
                this.checkMergedBeanDefinition(mbd, beanName, args);
                
                //递归获得当前Bean依赖的所有Bean（如果有的话）
                String[] dependsOn = mbd.getDependsOn();
                String[] var11;
                if (dependsOn != null) {
                    var11 = dependsOn;
                    int var12 = dependsOn.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        String dep = var11[var13];
                        if (this.isDependent(beanName, dep)) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }

                        this.registerDependentBean(dep, beanName);

                        try {
                            this.getBean(dep);
                        } catch (NoSuchBeanDefinitionException var24) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "'" + beanName + "' depends on missing bean '" + dep + "'", var24);
                        }
                    }
                }

    // 通过调用createBean方法创建Singlton bean实例
                if (mbd.isSingleton()) {
                    sharedInstance = this.getSingleton(beanName, () -> {
                        try {
                            return this.createBean(beanName, mbd, args);
                        } catch (BeansException var5) {
                            this.destroySingleton(beanName);
                            throw var5;
                        }
                    });
                    bean = this.getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
                } else if (mbd.isPrototype()) {//支持多例的，不用判断有没有缓存，直接新生成一个
                    var11 = null;

                    Object prototypeInstance;
                    try {
                        this.beforePrototypeCreation(beanName);
                        prototypeInstance = this.createBean(beanName, mbd, args);
                    } finally {
                        this.afterPrototypeCreation(beanName);
                    }

                    bean = this.getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
                } else {
                    String scopeName = mbd.getScope();
                    Scope scope = (Scope)this.scopes.get(scopeName);
                    if (scope == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                    }

                    try {
                        Object scopedInstance = scope.get(beanName, () -> {
                            this.beforePrototypeCreation(beanName);

                            Object var4;
                            try {
                                var4 = this.createBean(beanName, mbd, args);
                            } finally {
                                this.afterPrototypeCreation(beanName);
                            }

                            return var4;
                        });
                        bean = this.getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                    } catch (IllegalStateException var23) {
                        throw new BeanCreationException(beanName, "Scope '" + scopeName + "' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", var23);
                    }
                }
            } catch (BeansException var26) {
                this.cleanupAfterBeanCreationFailure(beanName);
                throw var26;
            }
        }

        if (requiredType != null && !requiredType.isInstance(bean)) {
            try {
                T convertedBean = this.getTypeConverter().convertIfNecessary(bean, requiredType);
                if (convertedBean == null) {
                    throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
                } else {
                    return convertedBean;
                }
            } catch (TypeMismatchException var25) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to convert bean '" + name + "' to required type '" + ClassUtils.getQualifiedName(requiredType) + "'", var25);
                }

                throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
            }
        } else {
            return bean;
        }
    }

```
- getBean -- > doGetBean --> createBean  --->resolveBeanClass --->doResolveBeanClass --->ClassUtils.forName((String)evaluated, classLoaderToUse);
- 首先要获取一个纯净的Bean ,通过反射获取，其次对于一些init-method属性定义，Bean后置处理器，自定义序列化，重写hashcode等等附加的方法，会通过包装器的方式

#### 生产Bean所包含的Java对象
- 看两个核心步骤
- createBean --> doCreateBean ---> createBeanInstance ---> instantiateUsingFactoryMethod
                                                          （instantiateBean）
```text
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
        Class<?> beanClass = this.resolveBeanClass(mbd, beanName, new Class[0]);
        if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
        } else {
            Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
            if (instanceSupplier != null) {
                return this.obtainFromSupplier(instanceSupplier, beanName);
            } else if (mbd.getFactoryMethodName() != null) {
            //利用工厂进行创建
                return this.instantiateUsingFactoryMethod(beanName, mbd, args);
            } else {
                boolean resolved = false;
                boolean autowireNecessary = false;
                if (args == null) {
                    Object var8 = mbd.constructorArgumentLock;
                    synchronized(mbd.constructorArgumentLock) {
                        if (mbd.resolvedConstructorOrFactoryMethod != null) {
                            resolved = true;
                            autowireNecessary = mbd.constructorArgumentsResolved;
                        }
                    }
                }

                if (resolved) {
                    return autowireNecessary ? this.autowireConstructor(beanName, mbd, (Constructor[])null, (Object[])null) : this.instantiateBean(beanName, mbd);
                } else {
                //使用构造函数对Bean实例化
                    Constructor<?>[] ctors = this.determineConstructorsFromBeanPostProcessors(beanClass, beanName);
                    return ctors == null && mbd.getResolvedAutowireMode() != 3 && !mbd.hasConstructorArgumentValues() && ObjectUtils.isEmpty(args) ? this.instantiateBean(beanName, mbd) : this.autowireConstructor(beanName, mbd, ctors, args);
                }
            }
        }
    }

```
```text
protected BeanWrapper instantiateBean(String beanName, RootBeanDefinition mbd) {
//使用CGLIB作为实例化类的方式
        try {
            Object beanInstance;
            if (System.getSecurityManager() != null) {
                beanInstance = AccessController.doPrivileged(() -> {
                    return thisx.getInstantiationStrategy().instantiate(mbd, beanName, this);
                }, this.getAccessControlContext());
            } else {
                beanInstance = this.getInstantiationStrategy().instantiate(mbd, beanName, this);
            }

            BeanWrapper bw = new BeanWrapperImpl(beanInstance);
            this.initBeanWrapper(bw);
            return bw;
        } catch (Throwable var6) {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", var6);
        }
    }
```
- 走到最后面会有点迷茫，一开始看不到CGLIB的踪影，就看到了判断clazz是否是接口的判断，主要看SimpleInstantiationStrategy --》instantiate
- 以上这个类是Spring默认实例化类的一个类,提供了两种方式，一种是最为常见的反射生成，一种就是CGLIB的代理方式
```text
 public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
        if (!bd.hasMethodOverrides()) {
            Object var5 = bd.constructorArgumentLock;
            Constructor constructorToUse;
            synchronized(bd.constructorArgumentLock) {
                constructorToUse = (Constructor)bd.resolvedConstructorOrFactoryMethod;
                if (constructorToUse == null) {
                    Class<?> clazz = bd.getBeanClass();
                    if (clazz.isInterface()) {
                        throw new BeanInstantiationException(clazz, "Specified class is an interface");
                    }

                    try {
                        if (System.getSecurityManager() != null) {
                            clazz.getClass();
                            constructorToUse = (Constructor)AccessController.doPrivileged(() -> {
                                return clazz.getDeclaredConstructor();
                            });
                        } else {
                            constructorToUse = clazz.getDeclaredConstructor();
                        }

                        bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                    } catch (Throwable var9) {
                        throw new BeanInstantiationException(clazz, "No default constructor found", var9);
                    }
                }
            }
             //反射的方式
            return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
        } else {
        //CGLIB的方式
            return this.instantiateWithMethodInjection(bd, beanName, owner);
        }
    }
```
- 看CGLIB的实现，一下子就调到了CglibSubclassingInstantiationStrategy这个类，最终看到了我们比较熟悉的增强类，并且给出了回调类，进行方法的调用
```text
public Object instantiate(@Nullable Constructor<?> ctor, @Nullable Object... args) {
            Class<?> subclass = this.createEnhancedSubclass(this.beanDefinition);
            Object instance;
            if (ctor == null) {
                instance = BeanUtils.instantiateClass(subclass);
            } else {
                try {
                    Constructor<?> enhancedSubclassConstructor = subclass.getConstructor(ctor.getParameterTypes());
                    instance = enhancedSubclassConstructor.newInstance(args);
                } catch (Exception var6) {
                    throw new BeanInstantiationException(this.beanDefinition.getBeanClass(), "Failed to invoke constructor for CGLIB enhanced subclass [" + subclass.getName() + "]", var6);
                }
            }

            Factory factory = (Factory)instance;
            factory.setCallbacks(new Callback[]{NoOp.INSTANCE, new CglibSubclassingInstantiationStrategy.LookupOverrideMethodInterceptor(this.beanDefinition, this.owner), new CglibSubclassingInstantiationStrategy.ReplaceOverrideMethodInterceptor(this.beanDefinition, this.owner)});
            return instance;
        }
```

#### Bean对象生成之后，把这些Bean对象的依赖关系设置好
- 依赖的注入和管理在populateBean的方法里,判断是否需要后置处理，判断属性，判断是否需要依赖注入，方法也是判断是否有这个Bean ,并且通过getBean来获取
```text
 protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
        if (bw == null) {
            if (mbd.hasPropertyValues()) {
                throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");
            }
        } else {
            boolean continueWithPropertyPopulation = true;
            if (!mbd.isSynthetic() && this.hasInstantiationAwareBeanPostProcessors()) {
                Iterator var5 = this.getBeanPostProcessors().iterator();

                while(var5.hasNext()) {
                    BeanPostProcessor bp = (BeanPostProcessor)var5.next();
                    if (bp instanceof InstantiationAwareBeanPostProcessor) {
                        InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
                        if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                            continueWithPropertyPopulation = false;
                            break;
                        }
                    }
                }
            }

            if (continueWithPropertyPopulation) {
                PropertyValues pvs = mbd.hasPropertyValues() ? mbd.getPropertyValues() : null;
                if (mbd.getResolvedAutowireMode() == 1 || mbd.getResolvedAutowireMode() == 2) {
                    MutablePropertyValues newPvs = new MutablePropertyValues((PropertyValues)pvs);
                    //通过名字还是类型来实现依赖注入
                    if (mbd.getResolvedAutowireMode() == 1) {
                        this.autowireByName(beanName, mbd, bw, newPvs);
                    }

                    if (mbd.getResolvedAutowireMode() == 2) {
                        this.autowireByType(beanName, mbd, bw, newPvs);
                    }

                    pvs = newPvs;
                }

                boolean hasInstAwareBpps = this.hasInstantiationAwareBeanPostProcessors();
                boolean needsDepCheck = mbd.getDependencyCheck() != 0;
                if (hasInstAwareBpps || needsDepCheck) {
                    if (pvs == null) {
                        pvs = mbd.getPropertyValues();
                    }

                    PropertyDescriptor[] filteredPds = this.filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
                    if (hasInstAwareBpps) {
                        Iterator var9 = this.getBeanPostProcessors().iterator();

                        while(var9.hasNext()) {
                            BeanPostProcessor bp = (BeanPostProcessor)var9.next();
                            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
                                pvs = ibp.postProcessPropertyValues((PropertyValues)pvs, filteredPds, bw.getWrappedInstance(), beanName);
                                if (pvs == null) {
                                    return;
                                }
                            }
                        }
                    }

                    if (needsDepCheck) {
                        this.checkDependencies(beanName, mbd, filteredPds, (PropertyValues)pvs);
                    }
                }

                if (pvs != null) {
                    this.applyPropertyValues(beanName, mbd, bw, (PropertyValues)pvs);
                }

            }
        }
    }




protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {
        String[] propertyNames = this.unsatisfiedNonSimpleProperties(mbd, bw);
        String[] var6 = propertyNames;
        int var7 = propertyNames.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String propertyName = var6[var8];
            if (this.containsBean(propertyName)) {
                Object bean = this.getBean(propertyName);
                pvs.add(propertyName, bean);
                this.registerDependentBean(propertyName, beanName);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Added autowiring by name from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
                }
            } else if (this.logger.isTraceEnabled()) {
                this.logger.trace("Not autowiring property '" + propertyName + "' of bean '" + beanName + "' by name: no matching bean found");
            }
        }

    }

```
```text
- 接着需要对Bean Reference进行解析，在对ManageList、ManageSet、ManageMap等进行解析完之后，就已经为依赖注入准备好了条件
- 这是真正把Bean对象设置到它所依赖的另一个Bean属性中去的地方，其中处理的属性是各种各样的
- 依赖注入发生在BeanWrapper的setPropertyValues中，具体的完成却是在BeanWrapper的子类BeanWrapperImpl中实现的
- 它会完成Bean的属性值的注入，其中包括对Array的注入、对List等集合类以及对非集合类的域进行注入。进过一系列的注入，这样就完成了对各种Bean属性的依赖注入过程。

```




### 总 -- 顶层Bean的创建和对它属性依赖注入
```text
递归都是以getBean为入口的

一个递归是在上下文体系中查找需要的Bean和创建Bean的递归调用

另一个递归是在依赖注入时，通过递归调用容器的getBean方法，得到当前Bean的依赖Bean，同时也触发对依赖Bean的创建和注入
```
- 所以，其实初步反射的对象实体，和BeanFactory当中的Bean是有区别的，他们有上下文，有相关依赖关系，一个赤裸裸的对象是没有的


#### 附：纯粹引用：lazy-init属性和预实例化
- refresh方法中，我们可以看到调用了finishBeanFactoryInitialization来对配置了lazy-init的Bean进行处理。
- 其实在这个方法中，封装了对lazy-init属性的处理，实际的处理是在DefaultListableBeanFactory这个基本容器的preInstantiateSingleton方法中完成的。
- 该方法对单例Bean完成预实例化，这个预实例化的完成巧妙地委托给容器来实现。
- 如果需要预实例化，那么就直接在这里采用getBean去触发依赖注入，与正常依赖注入的触发相比，只有触发的时间和场合不同
- 在这里，依赖注入发生在容器执行refresh的过程中，即IoC容器初始化的过程中
- 而不像一般的依赖注入一样发生在IoC容器初始化完成以后，第一次通过getBean想容器索要Bean的时候。
   