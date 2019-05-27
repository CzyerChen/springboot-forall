[转：IoC容器在Web容器中的创建及初始化](https://www.jianshu.com/p/83ea2bc4c221)

- IOC容器创建的前提就是Bean的注册和依赖注入，依赖BeanFactory来实现，ApplicationContext进行全局信息的注册，例如国际化的一些属性

### IoC容器在Web容器中是如何进行初始化的
在Spring IoC容器系列的设计中，我们可以看到两个主要的容器系列：
一个是实现BeanFactory接口的简单容器系列，这个系列容器只实现了容器的最基本功能；
另一个是ApplicationContext应用上下文，它作为容器的高级形态而存在

#### 什么时候进行初始化？
```text
public class ContextLoaderListener extends ContextLoader implements ServletContextListener {
    public ContextLoaderListener() {
    }

    public ContextLoaderListener(WebApplicationContext context) {
        super(context);
    }

    public void contextInitialized(ServletContextEvent event) {
        this.initWebApplicationContext(event.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent event) {
        this.closeWebApplicationContext(event.getServletContext());
        ContextCleanupListener.cleanupAttributes(event.getServletContext());
    }
}
```
- 我们可以发现ContextLoaderListener继承自ContextLoader,并且还实现了ServletContextListener。
- 并且它的构造函数中传入了一个WebApplicationContext,它是继承自ApplicationContext接口的高级IoC容器。
- contextInitialized方法的入参或是监听的Event是ServletContextEvent事件，也就是Tomcat启动加载完web.xml会产生的事件，ServletContextEvent持有了从web.xml加载的初始化配置的ServletContext上下文。
- ContextDestroyed方法的入参或是监听的Event是ServletContextEvent事件，在Tomcat关闭的时候执行该方法。
- 加载流程：
```text
1.当Servlet容器启动事件发生时，将被ContextLoaderListen监听器监听到。

2.此时ContextLoaderListener会调用实现ServletContextListener接口后实现的contextInitialized方法，

3.并把在web.xml加载初始化后获取的ServletContext传入initWebApplicationContext函数中进行IoC容器的初始化。

```
- ContextLoaderListener initWebApplicationContext方法调用的是ContextLoader ,那么在ContextLoader的初始化时候，需要执行一个静态代码块
```text
 static {
        try {
            ClassPathResource resource = new ClassPathResource("ContextLoader.properties", ContextLoader.class);
            defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException var1) {
            throw new IllegalStateException("Could not load 'ContextLoader.properties': " + var1.getMessage());
        }

        currentContextPerThread = new ConcurrentHashMap(1);
    }
```
- ContextLoader.properties文件内容如下：
```text
org.springframework.web.context.WebApplicationContext=
org.springframework.web.context.support.XmlWebApplicationContext
```
- ContextLoaderListener initWebApplicationContext
```text
public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
        if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {  // 已经有上下文容器了
            throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ContextLoader* definitions in your web.xml!");
        } else {
            Log logger = LogFactory.getLog(ContextLoader.class);
            servletContext.log("Initializing Spring root WebApplicationContext");
            if (logger.isInfoEnabled()) {
                logger.info("Root WebApplicationContext: initialization started");
            }

            long startTime = System.currentTimeMillis();

            try {
                if (this.context == null) {
                //创建上下文容器，并将servletContext的信息也传入
                    this.context = this.createWebApplicationContext(servletContext);
                }

                if (this.context instanceof ConfigurableWebApplicationContext) {
                    ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)this.context;
                    if (!cwac.isActive()) {
                        if (cwac.getParent() == null) {
                            ApplicationContext parent = this.loadParentContext(servletContext);
                            cwac.setParent(parent);
                        }
                       //configureAndRefreshWebApplicationContext来对该IoC进行初始化
                        this.configureAndRefreshWebApplicationContext(cwac, servletContext);
                    }
                }

                servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
                ClassLoader ccl = Thread.currentThread().getContextClassLoader();
                if (ccl == ContextLoader.class.getClassLoader()) {
                    currentContext = this.context;
                } else if (ccl != null) {
                    currentContextPerThread.put(ccl, this.context);
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
                }

                if (logger.isInfoEnabled()) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
                }

                return this.context;
            } catch (RuntimeException var8) {
                logger.error("Context initialization failed", var8);
                servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, var8);
                throw var8;
            } catch (Error var9) {
                logger.error("Context initialization failed", var9);
                servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, var9);
                throw var9;
            }
        }
    }
```
- 创建create 初步拿到一个可配置的web上下文对象
```text
protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
//判断这个上下文的种类，是WEB呢或是其他的
        Class<?> contextClass = this.determineContextClass(sc);
        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
            throw new ApplicationContextException("Custom context class [" + contextClass.getName() + "] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
        } else {
            return (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
        }
    }
    
     protected Class<?> determineContextClass(ServletContext servletContext) {
            String contextClassName = servletContext.getInitParameter("contextClass");
            if (contextClassName != null) {
                try {
                    return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
                } catch (ClassNotFoundException var4) {
                    throw new ApplicationContextException("Failed to load custom context class [" + contextClassName + "]", var4);
                }
            } else {
                contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
    
                try {
                    return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
                } catch (ClassNotFoundException var5) {
                    throw new ApplicationContextException("Failed to load default context class [" + contextClassName + "]", var5);
                }
            }
        }
```
- 整个IOC容器的初始化，里面对一个空对象设置了很多参数，并且通过refresh将这些参数生效
```text
 protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
        String configLocationParam;
        if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
            configLocationParam = sc.getInitParameter("contextId");
            if (configLocationParam != null) {
                wac.setId(configLocationParam);
            } else {
                wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + ObjectUtils.getDisplayString(sc.getContextPath()));
            }
        }

        wac.setServletContext(sc);
        configLocationParam = sc.getInitParameter("contextConfigLocation");
        if (configLocationParam != null) {
            wac.setConfigLocation(configLocationParam);
        }

        ConfigurableEnvironment env = wac.getEnvironment();
        if (env instanceof ConfigurableWebEnvironment) {
            ((ConfigurableWebEnvironment)env).initPropertySources(sc, (ServletConfig)null);
        }

        this.customizeContext(sc, wac);
        wac.refresh();
    }

```
