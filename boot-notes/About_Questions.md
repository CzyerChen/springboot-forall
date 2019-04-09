
## Spring 核心
### Spring基础
1.开发中主要使用 Spring 的什么技术 ?
- IOC 容器管理各层的组件
- 使用 AOP 配置声明式事务
- 整合其他框架


2.简述 AOP 和 IOC 概念
- AOP: Aspect Oriented Program, 面向(方面)切面的编程;Filter(过滤器)也是一种AOP.  AOP是一种新的方法论 ,  是对传统OOP(Object-Oriented Programming, 面向对象编程) 的补充. AOP 的主要编程对像是切面(aspect), 而切面模块化横切关注点.可以举例通过事务说明. 
- IOC: Invert Of Control, 控制反转. 也成为 DI(依赖注入)其思想是反转资源获取的方向. 传统的资源查找方式要求组件向容器发起请求查找资源.作为回应, 容器适时的返回资源. 而应用了 IOC 之后, 则是容器主动地将资源推送给它所管理的组件,组件所要做的仅是选择一种合适的方式来接受资源. 这种行为也被称为查找的被动形式.


3.在 Spring 中如何配置 Bean ?
- Bean 的配置方式: 通过全类名 （反射）、 通过工厂方法 （静态工厂方法 & 实例工厂方法）、FactoryBean


4.IOC 容器对 Bean 的生命周期:
  ①. 通过构造器或工厂方法创建 Bean 实例
  ②. 为 Bean 的属性设置值和对其他 Bean 的引用
  ③. 将Bean实例传递给Bean后置处理器的postProcessBeforeInitialization 方法
  ④. 调用 Bean 的初始化方法(init-method)
  ⑤. 将Bean实例传递给Bean后置处理器的postProcessAfterInitialization 方法
  ⑦. Bean 可以使用了
  ⑧. 当容器关闭时, 调用 Bean 的销毁方法(destroy-method)


5.说一下struts2-hibernate-Spring 的工作流程？

  1). Struts2 负责显示页面和接受请求
  2). Spring 的 IOC 容器管理各个组件: 整合 Struts2，Hibernate 和 其他组件，AOP 完成声明式事务
  3). Hibernate 提供 DAO 操作.

6. Spring 如何整合 Struts2 ?

  整合 Struts2, 即由 IOC 容器管理 Struts2 的 Action:
    > 安装 Spring 插件: 把 struts2-spring-plugin-2.2.1.jar 复制到
  当前 WEB 应用的 WEB-INF/lib 目录下
    > 在 Spring 的配置文件中配置 Struts2 的 Action 实例
    > 在 Struts 配置文件中配置 action,  但其 class 属性不再指向该Action 的实现类, 而是指向 Spring 容器中 Action 实例的 ID


7. Spring 如何整合 Hibernate
  整合  Hibernate,  即由  IOC  容器生成  SessionFactory  对象,  并使用Spring 的声明式事务
    > 利用 LocalSessionFactoryBean 工厂 Bean, 声明一个使用 XML 映射文件的 SessionFactory 实例.
    > 利用 HibernateTransactionManager 配置 Hibernate 的事务管理器


8. Spring MVC 比较 Struts2
  ①. Spring MVC 的入口是 Servlet, 而 Struts2 是 Filter
  ②. Spring MVC 会稍微比 Struts2 快些. Spring MVC 是基于方法设计, 而 Sturts2 是基于类, 每次发一次请求都会实例一个 Action.
  ③. Spring MVC 使用更加简洁, 开发效率 Spring MVC 确实比 struts2 高: 支持 JSR303, 处理 ajax 的请求更方便
  ④. Struts2 的 OGNL 表达式使页面的开发效率相比 Spring MVC 更高些. 


9. Spring MVC 的运行流程
  ①. 在整个 Spring MVC 框架中， DispatcherServlet 处于核心位置，负责协调和组织不同组件以完成请求处理并返回响应的工作
  ②. SpringMVC 处理请求过程：
    >  若一个请求匹配  DispatcherServlet 的请求映射路径(在  web.xml 中指定), WEB 容器将该请求转交给 DispatcherServlet 处理
    > DispatcherServlet 接收到请求后, 将根据请求信息(包括 URL、 HTTP 方法、 请求头、 请求参数、 Cookie 等)及 HandlerMapping 的配置找到处理请求的处理器(Handler). 可将 HandlerMapping 看成路由控制器， 将 Handler 看成目标主机。
    > 当 DispatcherServlet 根据 HandlerMapping 得到对应当前请求的Handler 后，通过 HandlerAdapter 对 Handler 进行封装，再以统一的适配器接口调用 Handler。
    >  处 理 器 完 成 业 务 逻 辑 的 处 理 后 将 返 回 一 个   ModelAndView  给DispatcherServlet, ModelAndView 包含了视图逻辑名和模型数据信息
    > DispatcherServlet 借助 ViewResoler 完成逻辑视图名到真实视图对象的解析
    > 得到真实视图对象 View 后, DispatcherServlet 使用这个 View 对ModelAndView 中的模型数据进行视图渲染
    
10. 说出 Spring MVC 常用的 5 个注解:
  @RequestMapping 、 @PathVariable 、 @RequestParam 、 @RequestBoy 、@ResponseBody


11. 如何使用 SpringMVC 完成 JSON 操作：
  ①. 配置 MappingJacksonHttpMessageConverter
  ②. 使用 @RequestBody 注解或 ResponseEntity 作为返回值


12.比较 SpringSecurity 和 Shiro

  相比 Spring Security, Shiro 在保持强大功能的同时, 使用简单性和灵活性
    > SpringSecurity: 即使是一个一个简单的请求，最少得经过它的 8 个Filter
    > SpringSecurity 必须在 Spring 的环境下使用
    > 初学 Spring Security, 曲线还是较大, 需要深入学习其源码和框架, 配置起来也较费力.


13.Shiro 的优点

  > 简单的身份认证, 支持多种数据源
  > 对角色的简单的授权, 支持细粒度的授权(方法级)
  > 支持一级缓存，以提升应用程序的性能；
  > 内置的基于 POJO 企业会话管理, 适用于 Web 以及非 Web 的环境
  > 非常简单的加密 API
  > 不跟任何的框架或者容器捆绑, 可以独立运行


14.简述 Shiro 的核心组件

  Shiro 架构 3 个核心组件:
    > Subject: 正与系统进行交互的人, 或某一个第三方服务. 
    所有 Subject 实例都被绑定到（且这是必须的）一个SecurityManager 上。
    > SecurityManager: Shiro 架构的心脏, 用来协调内部各安全组件, 管理内部组件实例, 并通过它来提供安全管理的各种服务. 
    当 Shiro 与一个 Subject 进行交互时, 实质上是幕后的 SecurityManager 处理所有繁重的 Subject 安全操作。
    > Realms: 本质上是一个特定安全的 DAO. 当配置 Shiro 时, 必须指定至少一个 Realm 用来进行身份验证和/或授权. 
    Shiro 提供了多种可用的 Realms 来获取安全相关的数据. 如关系数据库(JDBC), INI 及属性文件等. 
    可以定义自己 Realm 实现来代表自定义的数据源。

15.Shiro认证过程

  ①. 应用程序代码调用 Subject.login 方法，传递创建好的包含终端用户的 Principals(身份)和 Credentials(凭证)的 AuthenticationToken 实例
  ②. Subject 实例: 通常为 DelegatingSubject(或子类)委托应用程序的 SecurityManager 通过调用securityManager.login(token) 开始真正的验证。
  ③. SubjectManager 接收 token，调用内部的 Authenticator 实例调用 authenticator.authenticate(token).Authenticator 通常是一个 ModularRealmAuthenticator 实例, 支持在身份验证中协调一个或多个Realm 实例
  ④. 如果应用程序中配置了一个以上的 Realm, ModularRealmAuthenticator 实例将利用配置好的AuthenticationStrategy 来启动 Multi-Realm 认证尝试. 在Realms 被身份验证调用之前, 期间和以后,AuthenticationStrategy 被调用使其能够对每个Realm 的结果作出反应.
  ⑤. 每个配置的 Realm 用来帮助看它是否支持提交的 AuthenticationToken. 如果支持, 那么支持 Realm 的 getAuthenticationInfo 方法将会伴随着提交的 token 被调用. getAuthenticationInfo 方法有效地代表一个特定 Realm 的单一的身份验证尝试。
  
16.Shiro授权过程

  ①. 应用程序或框架代码调用任何 Subject 的hasRole*, checkRole*, isPermitted*,或者checkPermission*方法的变体, 传递任何所需的权限
  ②. Subject 的实例—通常是 DelegatingSubject(或子类), 调用securityManager 的对应的方法. 
  ③. SecurityManager 调用 org.apache.shiro.authz.Authorizer 接口的对应方法.默认情况下，authorizer 实例是一个 ModularRealmAuthorizer 实例, 它支持协调任何授权操作过程中的一个或多个Realm 实例
  ④. 每个配置好的 Realm 被检查是否实现了相同的 Authorizer 接口. 如果是, Realm 各自的 hasRole*, checkRole*,isPermitted*，或 checkPermission* 方法将被调用。
  
17.Shiro 如何自实现认证

  Shiro 的认证过程由 Realm 执行, SecurityManager 会调用 org.apache.shiro.realm.Realm 的 getAuthenticationInfo(AuthenticationToken token) 方法. 实际开发中, 通常提供 org.apache.shiro.realm.AuthenticatingRealm 的实现类, 并在该实现类中提供 doGetAuthenticationInfo(AuthenticationToken token)方法的具体实现

18.如何实现自实现授权

  实际开发中, 通常提供  org.apache.shiro.realm.AuthorizingRealm 的实现类, 
  并提供 doGetAuthorizationInfo(PrincipalCollection principals) 方法的具体实现

19.如何配置在 Spring 中配置使用 Shiro

  ①. 在 web.xml 中配置 Shiro 的 Filter
  ②. 在 Spring 的配置文件中配置  Shiro:
    > 配置自定义 Realm：实现自定义认证和授权
    > 配置 Shiro 实体类使用的缓存策略
    > 配置 SecurityManager
    > 配置保证 Shiro 内部 Bean 声明周期都得到执行的 Lifecycle Bean 后置处理器
    > 配置AOP 式方法级权限检查 
    > 配置 Shiro Filter  
    
1. 什么是spring?
Spring 是个java企业级应用的开源开发框架。Spring主要用来开发Java应用，但是有些扩展是针对构建J2EE平台的web应用。Spring 框架目标是简化Java企业级应用开发，并通过POJO为基础的编程模型促进良好的编程习惯。

2. 使用Spring框架的好处是什么？
轻量：Spring 是轻量的，基本的版本大约2MB。
控制反转：Spring通过控制反转实现了松散耦合，对象们给出它们的依赖，而不是创建或查找依赖的对象们。
面向切面的编程(AOP)：Spring支持面向切面的编程，并且把应用业务逻辑和系统服务分开。
容器：Spring 包含并管理应用中对象的生命周期和配置。
MVC框架：Spring的WEB框架是个精心设计的框架，是Web框架的一个很好的替代品。
事务管理：Spring 提供一个持续的事务管理接口，可以扩展到上至本地事务下至全局事务（JTA）。
异常处理：Spring 提供方便的API把具体技术相关的异常（比如由JDBC，Hibernate or JDO抛出的）转化为一致的unchecked 异常。
3.  Spring由哪些模块组成?
以下是Spring 框架的基本模块：

Core module
Bean module
Context module
Expression Language module
JDBC module
ORM module
OXM module
Java Messaging Service(JMS) module
Transaction module
Web module
Web-Servlet module
Web-Struts module
Web-Portlet module
4. 核心容器（应用上下文) 模块。
这是基本的Spring模块，提供spring 框架的基础功能，BeanFactory 是 任何以spring为基础的应用的核心。Spring 框架建立在此模块之上，它使Spring成为一个容器。

5. BeanFactory – BeanFactory 实现举例。
Bean 工厂是工厂模式的一个实现，提供了控制反转功能，用来把应用的配置和依赖从正真的应用代码中分离。

最常用的BeanFactory 实现是XmlBeanFactory 类。

6. XMLBeanFactory 
最常用的就是org.springframework.beans.factory.xml.XmlBeanFactory ，它根据XML文件中的定义加载beans。该容器从XML 文件读取配置元数据并用它去创建一个完全配置的系统或应用。

7. 解释AOP模块
AOP模块用于发给我们的Spring应用做面向切面的开发， 很多支持由AOP联盟提供，这样就确保了Spring和其他AOP框架的共通性。这个模块将元数据编程引入Spring。

8. 解释JDBC抽象和DAO模块。
通过使用JDBC抽象和DAO模块，保证数据库代码的简洁，并能避免数据库资源错误关闭导致的问题，它在各种不同的数据库的错误信息之上，提供了一个统一的异常访问层。它还利用Spring的AOP 模块给Spring应用中的对象提供事务管理服务。

9. 解释对象/关系映射集成模块。
Spring 通过提供ORM模块，支持我们在直接JDBC之上使用一个对象/关系映射映射(ORM)工具，Spring 支持集成主流的ORM框架，如Hiberate,JDO和 iBATIS SQL Maps。Spring的事务管理同样支持以上所有ORM框架及JDBC。

10.  解释WEB 模块。
Spring的WEB模块是构建在application context 模块基础之上，提供一个适合web应用的上下文。这个模块也包括支持多种面向web的任务，如透明地处理多个文件上传请求和程序级请求参数的绑定到你的业务对象。它也有对Jakarta Struts的支持。

12.  Spring配置文件
Spring配置文件是个XML 文件，这个文件包含了类信息，描述了如何配置它们，以及如何相互调用。

13.  什么是Spring IOC 容器？
Spring IOC 负责创建对象，管理对象（通过依赖注入（DI），装配对象，配置对象，并且管理这些对象的整个生命周期。

14.  IOC的优点是什么？
IOC 或 依赖注入把应用的代码量降到最低。它使应用容易测试，单元测试不再需要单例和JNDI查找机制。最小的代价和最小的侵入性使松散耦合得以实现。IOC容器支持加载服务时的饿汉式初始化和懒加载。

15. ApplicationContext通常的实现是什么?
FileSystemXmlApplicationContext ：此容器从一个XML文件中加载beans的定义，XML Bean 配置文件的全路径名必须提供给它的构造函数。
ClassPathXmlApplicationContext：此容器也从一个XML文件中加载beans的定义，这里，你需要正确设置classpath因为这个容器将在classpath里找bean配置。
WebXmlApplicationContext：此容器加载一个XML文件，此文件定义了一个WEB应用的所有bean。
16. Bean 工厂和 Application contexts  有什么区别？
Application contexts提供一种方法处理文本消息，一个通常的做法是加载文件资源（比如镜像），它们可以向注册为监听器的bean发布事件。另外，在容器或容器内的对象上执行的那些不得不由bean工厂以程序化方式处理的操作，可以在Application contexts中以声明的方式处理。Application contexts实现了MessageSource接口，该接口的实现以可插拔的方式提供获取本地化消息的方法。

17. 一个Spring的应用看起来象什么？
一个定义了一些功能的接口。
这实现包括属性，它的Setter ， getter 方法和函数等。
Spring AOP。
Spring 的XML 配置文件。
使用以上功能的客户端程序。
依赖注入
18. 什么是Spring的依赖注入？
依赖注入，是IOC的一个方面，是个通常的概念，它有多种解释。这概念是说你不用创建对象，而只需要描述它如何被创建。你不在代码里直接组装你的组件和服务，但是要在配置文件里描述哪些组件需要哪些服务，之后一个容器（IOC容器）负责把他们组装起来。

19.  有哪些不同类型的IOC（依赖注入）方式？
构造器依赖注入：构造器依赖注入通过容器触发一个类的构造器来实现的，该类有一系列参数，每个参数代表一个对其他类的依赖。
Setter方法注入：Setter方法注入是容器通过调用无参构造器或无参static工厂 方法实例化bean之后，调用该bean的setter方法，即实现了基于setter的依赖注入。
20. 哪种依赖注入方式你建议使用，构造器注入，还是 Setter方法注入？
你两种依赖方式都可以使用，构造器注入和Setter方法注入。最好的解决方案是用构造器参数实现强制依赖，setter方法实现可选依赖。

Spring Beans
21.什么是Spring beans?
Spring beans 是那些形成Spring应用的主干的java对象。它们被Spring IOC容器初始化，装配，和管理。这些beans通过容器中配置的元数据创建。比如，以XML文件中<bean/> 的形式定义。

Spring 框架定义的beans都是单件beans。在bean tag中有个属性”singleton”，如果它被赋为TRUE，bean 就是单件，否则就是一个 prototype bean。默认是TRUE，所以所有在Spring框架中的beans 缺省都是单件。

22. 一个 Spring Bean 定义 包含什么？
一个Spring Bean 的定义包含容器必知的所有配置元数据，包括如何创建一个bean，它的生命周期详情及它的依赖。

23. 如何给Spring 容器提供配置元数据?
这里有三种重要的方法给Spring 容器提供配置元数据。

XML配置文件。

基于注解的配置。

基于java的配置。

24. 你怎样定义类的作用域? 
当定义一个<bean> 在Spring里，我们还能给这个bean声明一个作用域。它可以通过bean 定义中的scope属性来定义。如，当Spring要在需要的时候每次生产一个新的bean实例，bean的scope属性被指定为prototype。另一方面，一个bean每次使用的时候必须返回同一个实例，这个bean的scope 属性 必须设为 singleton。

25. 解释Spring支持的几种bean的作用域。
Spring框架支持以下五种bean的作用域：

singleton : bean在每个Spring ioc 容器中只有一个实例。
prototype：一个bean的定义可以有多个实例。
request：每次http请求都会创建一个bean，该作用域仅在基于web的Spring ApplicationContext情形下有效。
session：在一个HTTP Session中，一个bean定义对应一个实例。该作用域仅在基于web的Spring ApplicationContext情形下有效。
global-session：在一个全局的HTTP Session中，一个bean定义对应一个实例。该作用域仅在基于web的Spring ApplicationContext情形下有效。
缺省的Spring bean 的作用域是Singleton.

26. Spring框架中的单例bean是线程安全的吗?
不，Spring框架中的单例bean不是线程安全的。

27. 解释Spring框架中bean的生命周期。
Spring容器 从XML 文件中读取bean的定义，并实例化bean。
Spring根据bean的定义填充所有的属性。
如果bean实现了BeanNameAware 接口，Spring 传递bean 的ID 到 setBeanName方法。
如果Bean 实现了 BeanFactoryAware 接口， Spring传递beanfactory 给setBeanFactory 方法。
如果有任何与bean相关联的BeanPostProcessors，Spring会在postProcesserBeforeInitialization()方法内调用它们。
如果bean实现IntializingBean了，调用它的afterPropertySet方法，如果bean声明了初始化方法，调用此初始化方法。
如果有BeanPostProcessors 和bean 关联，这些bean的postProcessAfterInitialization() 方法将被调用。
如果bean实现了 DisposableBean，它将调用destroy()方法。
28.  哪些是重要的bean生命周期方法？ 你能重载它们吗？
有两个重要的bean 生命周期方法，第一个是setup ， 它是在容器加载bean的时候被调用。第二个方法是 teardown  它是在容器卸载类的时候被调用。

The bean 标签有两个重要的属性（init-method和destroy-method）。用它们你可以自己定制初始化和注销方法。它们也有相应的注解（@PostConstruct和@PreDestroy）。

29. 什么是Spring的内部bean？
当一个bean仅被用作另一个bean的属性时，它能被声明为一个内部bean，为了定义inner bean，在Spring 的 基于XML的 配置元数据中，可以在 <property/>或 <constructor-arg/> 元素内使用<bean/> 元素，内部bean通常是匿名的，它们的Scope一般是prototype。

30. 在 Spring中如何注入一个java集合？
Spring提供以下几种集合的配置元素：

<list>类型用于注入一列值，允许有相同的值。
<set> 类型用于注入一组值，不允许有相同的值。
<map> 类型用于注入一组键值对，键和值都可以为任意类型。
<props>类型用于注入一组键值对，键和值都只能为String类型。
31. 什么是bean装配? 
装配，或bean 装配是指在Spring 容器中把bean组装到一起，前提是容器需要知道bean的依赖关系，如何通过依赖注入来把它们装配到一起。

32. 什么是bean的自动装配？
Spring 容器能够自动装配相互合作的bean，这意味着容器不需要<constructor-arg>和<property>配置，能通过Bean工厂自动处理bean之间的协作。

33. 解释不同方式的自动装配 。
有五种自动装配的方式，可以用来指导Spring容器用自动装配方式来进行依赖注入。

no：默认的方式是不进行自动装配，通过显式设置ref 属性来进行装配。
byName：通过参数名 自动装配，Spring容器在配置文件中发现bean的autowire属性被设置成byname，之后容器试图匹配、装配和该bean的属性具有相同名字的bean。
byType:：通过参数类型自动装配，Spring容器在配置文件中发现bean的autowire属性被设置成byType，之后容器试图匹配、装配和该bean的属性具有相同类型的bean。如果有多个bean符合条件，则抛出错误。
constructor：这个方式类似于byType， 但是要提供给构造器参数，如果没有确定的带参数的构造器参数类型，将会抛出异常。
autodetect：首先尝试使用constructor来自动装配，如果无法工作，则使用byType方式。
34.自动装配有哪些局限性 ?
自动装配的局限性是：

重写： 你仍需用 <constructor-arg>和 <property> 配置来定义依赖，意味着总要重写自动装配。
基本数据类型：你不能自动装配简单的属性，如基本数据类型，String字符串，和类。
模糊特性：自动装配不如显式装配精确，如果有可能，建议使用显式装配。
35. 你可以在Spring中注入一个null 和一个空字符串吗？
可以。

Spring注解
36. 什么是基于Java的Spring注解配置? 给一些注解的例子.
基于Java的配置，允许你在少量的Java注解的帮助下，进行你的大部分Spring配置而非通过XML文件。

以@Configuration 注解为例，它用来标记类可以当做一个bean的定义，被Spring IOC容器使用。另一个例子是@Bean注解，它表示此方法将要返回一个对象，作为一个bean注册进Spring应用上下文。

37. 什么是基于注解的容器配置?
相对于XML文件，注解型的配置依赖于通过字节码元数据装配组件，而非尖括号的声明。

开发者通过在相应的类，方法或属性上使用注解的方式，直接组件类中进行配置，而不是使用xml表述bean的装配关系。

38. 怎样开启注解装配？
注解装配在默认情况下是不开启的，为了使用注解装配，我们必须在Spring配置文件中配置 <context:annotation-config/>元素。

39. @Required  注解
这个注解表明bean的属性必须在配置的时候设置，通过一个bean定义的显式的属性值或通过自动装配，若@Required注解的bean属性未被设置，容器将抛出BeanInitializationException。

40. @Autowired 注解
@Autowired 注解提供了更细粒度的控制，包括在何处以及如何完成自动装配。它的用法和@Required一样，修饰setter方法、构造器、属性或者具有任意名称和/或多个参数的PN方法。

41. @Qualifier 注解
当有多个相同类型的bean却只有一个需要自动装配时，将@Qualifier 注解和@Autowire 注解结合使用以消除这种混淆，指定需要装配的确切的bean。

Spring数据访问
42.在Spring框架中如何更有效地使用JDBC? 
使用SpringJDBC 框架，资源管理和错误处理的代价都会被减轻。所以开发者只需写statements 和 queries从数据存取数据，JDBC也可以在Spring框架提供的模板类的帮助下更有效地被使用，这个模板叫JdbcTemplate （例子见这里here）

43. JdbcTemplate
JdbcTemplate 类提供了很多便利的方法解决诸如把数据库数据转变成基本数据类型或对象，执行写好的或可调用的数据库操作语句，提供自定义的数据错误处理。

44. Spring对DAO的支持
Spring对数据访问对象（DAO）的支持旨在简化它和数据访问技术如JDBC，Hibernate or JDO 结合使用。这使我们可以方便切换持久层。编码时也不用担心会捕获每种技术特有的异常。

45. 使用Spring通过什么方式访问Hibernate? 
在Spring中有两种方式访问Hibernate：

控制反转  Hibernate Template和 Callback。
继承 HibernateDAOSupport提供一个AOP 拦截器。
46. Spring支持的ORM
Spring支持以下ORM：

Hibernate
iBatis
JPA (Java Persistence API)
TopLink
JDO (Java Data Objects)
OJB
47.如何通过HibernateDaoSupport将Spring和Hibernate结合起来？
用Spring的 SessionFactory 调用 LocalSessionFactory。集成过程分三步：

配置the Hibernate SessionFactory。
继承HibernateDaoSupport实现一个DAO。
在AOP支持的事务中装配。
48. Spring支持的事务管理类型
Spring支持两种类型的事务管理：

编程式事务管理：这意味你通过编程的方式管理事务，给你带来极大的灵活性，但是难维护。
声明式事务管理：这意味着你可以将业务代码和事务管理分离，你只需用注解和XML配置来管理事务。
49. Spring框架的事务管理有哪些优点？
它为不同的事务API  如 JTA，JDBC，Hibernate，JPA 和JDO，提供一个不变的编程模式。
它为编程式事务管理提供了一套简单的API而不是一些复杂的事务API如
它支持声明式事务管理。
它和Spring各种数据访问抽象层很好得集成。
50. 你更倾向用那种事务管理类型？
大多数Spring框架的用户选择声明式事务管理，因为它对应用代码的影响最小，因此更符合一个无侵入的轻量级容器的思想。声明式事务管理要优于编程式事务管理，虽然比编程式事务管理（这种方式允许你通过代码控制事务）少了一点灵活性。

Spring面向切面编程（AOP）
51.  解释AOP
面向切面的编程，或AOP， 是一种编程技术，允许程序模块化横向切割关注点，或横切典型的责任划分，如日志和事务管理。

52. Aspect 切面
AOP核心就是切面，它将多个类的通用行为封装成可重用的模块，该模块含有一组API提供横切功能。比如，一个日志模块可以被称作日志的AOP切面。根据需求的不同，一个应用程序可以有若干切面。在Spring AOP中，切面通过带有@Aspect注解的类实现。

52. 在Spring AOP 中，关注点和横切关注的区别是什么？
关注点是应用中一个模块的行为，一个关注点可能会被定义成一个我们想实现的一个功能。
横切关注点是一个关注点，此关注点是整个应用都会使用的功能，并影响整个应用，比如日志，安全和数据传输，几乎应用的每个模块都需要的功能。因此这些都属于横切关注点。

54. 连接点
连接点代表一个应用程序的某个位置，在这个位置我们可以插入一个AOP切面，它实际上是个应用程序执行Spring AOP的位置。

55. 通知
通知是个在方法执行前或执行后要做的动作，实际上是程序执行时要通过SpringAOP框架触发的代码段。

Spring切面可以应用五种类型的通知：

before：前置通知，在一个方法执行前被调用。
after: 在方法执行之后调用的通知，无论方法执行是否成功。
after-returning: 仅当方法成功完成后执行的通知。
after-throwing: 在方法抛出异常退出时执行的通知。
around: 在方法执行之前和之后调用的通知。
56. 切点
切入点是一个或一组连接点，通知将在这些位置执行。可以通过表达式或匹配的方式指明切入点。

57. 什么是引入? 
引入允许我们在已存在的类中增加新的方法和属性。

58. 什么是目标对象? 
被一个或者多个切面所通知的对象。它通常是一个代理对象。也指被通知（advised）对象。

59. 什么是代理?
代理是通知目标对象后创建的对象。从客户端的角度看，代理对象和目标对象是一样的。

60. 有几种不同类型的自动代理？
BeanNameAutoProxyCreator

DefaultAdvisorAutoProxyCreator

Metadata autoproxying

61. 什么是织入。什么是织入应用的不同点？
织入是将切面和到其他应用类型或对象连接或创建一个被通知对象的过程。

织入可以在编译时，加载时，或运行时完成。

62. 解释基于XML Schema方式的切面实现。
在这种情况下，切面由常规类以及基于XML的配置实现。

63. 解释基于注解的切面实现
在这种情况下(基于@AspectJ的实现)，涉及到的切面声明的风格与带有java5标注的普通java类一致。

Spring 的MVC
64. 什么是Spring的MVC框架？
Spring 配备构建Web 应用的全功能MVC框架。Spring可以很便捷地和其他MVC框架集成，如Struts，Spring 的MVC框架用控制反转把业务对象和控制逻辑清晰地隔离。它也允许以声明的方式把请求参数和业务对象绑定。

65. DispatcherServlet
Spring的MVC框架是围绕DispatcherServlet来设计的，它用来处理所有的HTTP请求和响应。

66. WebApplicationContext
WebApplicationContext 继承了ApplicationContext  并增加了一些WEB应用必备的特有功能，它不同于一般的ApplicationContext ，因为它能处理主题，并找到被关联的servlet。

67. 什么是Spring MVC框架的控制器？
控制器提供一个访问应用程序的行为，此行为通常通过服务接口实现。控制器解析用户输入并将其转换为一个由视图呈现给用户的模型。Spring用一个非常抽象的方式实现了一个控制层，允许用户创建多种用途的控制器。

68. @Controller 注解
该注解表明该类扮演控制器的角色，Spring不需要你继承任何其他控制器基类或引用Servlet API。

69. @RequestMapping 注解
该注解是用来映射一个URL到一个类或一个特定的方处理法上。



### 认证安全
#### Spring Security 简介
#### Spring Security 的核心配置有哪些
#### Spring Security 的核心过滤链如何注入
#### Spring Security  和 shiro的区别
#### Shiro简介
#### shiro有哪些特性，分别是什么功能
#### shiro的架构，主要组件，以及它们的作用
#### shiro认证的流程

