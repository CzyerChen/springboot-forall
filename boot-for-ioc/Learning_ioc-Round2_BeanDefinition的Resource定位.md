### 以上描述了Context的初始化，那BeanFactory什么时候实例化类，依赖注入呢？
- 我们在使用类的时候，往往非常关注依赖注入和Bean的实例化，需要BeanDefinition的Resource定位、BeanDefinition的载入和解析以及BeanDefinition在IoC容器中的注册等
#### BeanDefinition的Resource定位
- DefaultListableBeanFactory非常重要，是我们经常要用到的一个IoC容器的实现， 比如在设计应用上下文ApplicationContext时就会用到它
- 这个DefaultListableBeanFactory实际上包含了基本IoC容器所具有的重要功能，也是在很多地方都会用到的容器系列中的一个基本产品。
- 在Spring中，实际上是把DefaultListableBeanFactory作为一个默认的功能完整的IoC容器来使用的

- 编程式使用DefaultListableBeanFactory容器
```text
ClassPathResource res = new ClassPathResource("bean.xml");
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);//PropertiesBeanDefinitionReader
reader.loadBeanDefinition(res);


流程说明：
1. 创建IoC配置文件的抽象资源，这个抽象资源包含了BeanDefinition的定义信息，这里使用的是ClassPathResource

2.创建一个BeanFactory，这里使用的是DefaultListableBeanFactory；

3.创建一个载入BeanDefinition的读取器，这里使用的是XmlBeanDefinitionReader来载入XML文件形式的BeanDefinition，然后通过一个回调配置给BeanFactory。

4.从定义好的资源位置读取配置信息，具体的解析过程由BeanDefinitionReader来完成。完成整个在如何注册Bean定义之后，需要的IoC容器就建立起来了。这个时候就可以直接使用IoC容器了

FileSystemXmlApplicationContext可以从文件系统载入Resource，
ClassPathXmlApplicationContext可以从Class Path载入Resource，
XmlWebApplicationContext可以在Web容器中载入Resource等。

```
- 对于Spring ，bean的配置就是读取配置文件啦，然后解析xml就能够获取Bean的信息，对于Springboot，由于采用了注解的方式，默认就是包扫描，然后获取对应注解下的类，变量和方法的参数，主要利用反射
```text
SpringBoot

public class AnnotationConfigWebApplicationContext extends AbstractRefreshableWebApplicationContext implements AnnotationConfigRegistry {
    @Nullable
    private BeanNameGenerator beanNameGenerator;
    @Nullable
    private ScopeMetadataResolver scopeMetadataResolver;
    private final Set<Class<?>> annotatedClasses = new LinkedHashSet();
    private final Set<String> basePackages = new LinkedHashSet();
    
   .....
   
   public void scan(String... basePackages) {
           Assert.notEmpty(basePackages, "At least one base package must be specified");
           Collections.addAll(this.basePackages, basePackages);
       }
   
       protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
           AnnotatedBeanDefinitionReader reader = this.getAnnotatedBeanDefinitionReader(beanFactory);
           ClassPathBeanDefinitionScanner scanner = this.getClassPathBeanDefinitionScanner(beanFactory);
           BeanNameGenerator beanNameGenerator = this.getBeanNameGenerator();
           if (beanNameGenerator != null) {
               reader.setBeanNameGenerator(beanNameGenerator);
               scanner.setBeanNameGenerator(beanNameGenerator);
               beanFactory.registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
           }
   
           ScopeMetadataResolver scopeMetadataResolver = this.getScopeMetadataResolver();
           if (scopeMetadataResolver != null) {
               reader.setScopeMetadataResolver(scopeMetadataResolver);
               scanner.setScopeMetadataResolver(scopeMetadataResolver);
           }
   
           if (!this.annotatedClasses.isEmpty()) {
               if (this.logger.isInfoEnabled()) {
                   this.logger.info("Registering annotated classes: [" + StringUtils.collectionToCommaDelimitedString(this.annotatedClasses) + "]");
               }
   
               reader.register(ClassUtils.toClassArray(this.annotatedClasses));
           }
   
           if (!this.basePackages.isEmpty()) {
               if (this.logger.isInfoEnabled()) {
                   this.logger.info("Scanning base packages: [" + StringUtils.collectionToCommaDelimitedString(this.basePackages) + "]");
               }
   
               scanner.scan(StringUtils.toStringArray(this.basePackages));
           }
   
           String[] configLocations = this.getConfigLocations();
           if (configLocations != null) {
               String[] var7 = configLocations;
               int var8 = configLocations.length;
   
               for(int var9 = 0; var9 < var8; ++var9) {
                   String configLocation = var7[var9];
   
                   try {
                       Class<?> clazz = ClassUtils.forName(configLocation, this.getClassLoader());
                       if (this.logger.isInfoEnabled()) {
                           this.logger.info("Successfully resolved class for [" + configLocation + "]");
                       }
   
                       reader.register(new Class[]{clazz});
                   } catch (ClassNotFoundException var13) {
                       if (this.logger.isDebugEnabled()) {
                           this.logger.debug("Could not load class for config location [" + configLocation + "] - trying package scan. " + var13);
                       }
   
                       int count = scanner.scan(new String[]{configLocation});
                       if (this.logger.isInfoEnabled()) {
                           if (count == 0) {
                               this.logger.info("No annotated classes found for specified class/package [" + configLocation + "]");
                           } else {
                               this.logger.info("Found " + count + " annotated classes in package [" + configLocation + "]");
                           }
                       }
                   }
               }
           }
   
       }



Spring classpathXmlApplicationContext

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext {
    private boolean validating = true;

 protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
        this.initBeanDefinitionReader(beanDefinitionReader);
        this.loadBeanDefinitions(beanDefinitionReader);
    }

  protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
        Resource[] configResources = this.getConfigResources();
        if (configResources != null) {
            reader.loadBeanDefinitions(configResources);
        }

        String[] configLocations = this.getConfigLocations();
        if (configLocations != null) {
            reader.loadBeanDefinitions(configLocations);
        }

    }
    ......
    }
    
  
  public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz, @Nullable ApplicationContext parent) throws BeansException {
          super(parent);
          Assert.notNull(paths, "Path array must not be null");
          Assert.notNull(clazz, "Class argument must not be null");
          this.configResources = new Resource[paths.length];
  
          for(int i = 0; i < paths.length; ++i) {
              this.configResources[i] = new ClassPathResource(paths[i], clazz);
          }
  
          this.refresh();
      }

       
```


