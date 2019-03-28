> 来自《SpringBoot实战（第四版）》

> 相对基础偏知识点一些
 
### 一、什么是Spring Springboot
#### 1.Spring的出世
- Spring诞生时是Java企业版（Java Enterprise Edition，JEE，也称J2EE）的轻量级代替品
- 通过依赖注入和面向切面编程，用简单的Java对象（Plain Old Java Object，POJO）实现了EJB的功能
- 虽然Spring的组件代码是轻量级的，但它的配置却是重量级,需要大量的XML进行Bean的配置，属性注入等
- 开启事务、Spring MVC，还是需要用XML或Java进行显式配置

#### 2.Spring项目
- 一个项目结构，一个web.xml，一个启用SpringMVC的Spring配置，一个控制器响应HTTP请求，一个WEB部署容器，例如TOMCAT

#### 3.Springboot的出世
- Spring Boot的命令行界面（Command Line Interface，CLI）：spring run HelloController.groovy
- Spring Boot的出现就是为了减轻Spring的配置，关于实现原理还是一致的

### 4.Springboot 特性
- 自动配置---会根据配置里面包含的Bean来建立对应的Java Bean
```text
1.Spring Boot在应用程序的Classpath里发现H2数据库的库，那么它就自动配置一个嵌入式H2数据库。如果在Classpath里发现JdbcTemplate，那么它还会为你配置一个JdbcTemplate的Bean

2.自动配置涉及Java持久化API（Java Persistence API，JPA）、Thymeleaf模板、安全和Spring MVC

```

- 起步依赖---如果maven里面添加了很多jar包，版本的管理和冲突时一个很麻烦的事情
```text
1.Spring Boot通过起步依赖为项目的依赖管理提供帮助。起步依赖其实就是特殊的Maven依赖和Gradle依赖，利用了传递依赖解析，把常用库聚合在一起，组成了几个为特定功能而定制的依赖

2.例如你需要web相关的功能，可以添加web模块，需要持久化相关的功能，添加jpa模块，需要security相关的功能，添加security模块
```

- 命令行界面
```text
1.Spring Boot CLI利用了起步依赖和自动配置，让你专注于代码本身

2.编写者只需要重视代码的编写，不需要关心依赖，CLI能检测到你使用了哪些类，它知道要向Classpath中添加哪些起步依赖才能让它运转起来。一旦那些依赖出现在Classpath中，一系列自动配置就会接踵而来，确保启用DispatcherServlet和Spring MVC，这样控制器就能响应HTTP请求了。
```

- actuator---做应用层面的监控
```text
1.提供在运行时检视应用程序内部情况的能力。安装了Actuator就能窥探应用程序的内部情况

2.可以监控Spring应用程序上下文里配置的Bean,
Spring Boot的自动配置做的决策,
应用程序取到的环境变量、系统属性、配置属性和命令行参数,
应用程序里线程的当前状态,
应用程序最近处理过的HTTP请求的追踪情况,
各种和内存用量、垃圾回收、Web请求以及数据源用量相关的指标
```


### 二、安装 Spring Boot CLI
- 用下载的分发包进行安装。
- 用Groovy Environment Manager进行安装。
- 通过OS X Homebrew进行安装。
- 使用MacPorts进行安装。


### 三、Spring Initializer 初始化Springboot 项目
- 1）通过Web界面使用。
- 2）通过Spring Tool Suite使用。
- 3）通过IntelliJ IDEA使用。
- 4）使用Spring Boot CLI使用。

那最常用的是两种 （1）（3）
- 网页就是通过当问http://start.spring.io。根据需求设置版本、设置需要加载的模板，下载压缩包即可
- IDEA是直接新建spring的项目，输入initializr Service URL :http://start.spring.io 然后也是选择模块等就能生成


### 四、构建一个简单的spring boot项目
- 建立一个使用web、Thymeleaf、JPA、Mysql模块的spring项目
```text
pom.xml
src -----main
 |        |-------java
                     |---com
 |        |               |---example
 |        |                      |------ExampleApplication.java  ----执行主类
 |        |-------resources
 |                   |---application.properties ---主配置文件
 |                   |---static
 |                   |---templates
 | 
 |-------test
          |---java
                |---com
                     |---example
                           |-------ExampleApplicationTests.java  ---主测试类

```
- @SpringBootApplication ---- 就是开启组建扫描和自动配置
- @Configuration ---- 标明该类使用Spring基于Java的配置
- @ComponentScan ---- 启用组件扫描，这样你写的Web控制器类和其他组件才能被自动发现并注册为Spring应用程序上下文里的Bean
- @Controller @RestController(增加@ResponseBody部分，返回JSON) ----  Spring MVC 控制器
-  @RequestMapping @RequestParam @PathVariable
- @RunWith(SpringJUnit4ClassRunner.class)@SpringApplicationConfiguration(classes=ExampleApplication.class)@WebAppConfiguration @Test
- 条件注解
```text
@ConditionalOnBean 配置了某个特定Bean
@ConditionalOnMissingBean 没有配置特定的Bean
@ConditionalOnClass Classpath里有指定的类
@ConditionalOnMissingClass Classpath里缺少指定的类
@ConditionalOnExpression 给定的Spring Expression Language（SpEL）表达式计算结果为true
@ConditionalOnJava Java的版本匹配特定值或者一个范围值
@ConditionalOnJndi 参数中给定的JNDI位置必须存在一个，如果没有给参数，则要有JNDI
InitialContext
@ConditionalOnProperty 指定的配置属性要有一个明确的值
@ConditionalOnResource Classpath里有指定的资源
@ConditionalOnWebApplication 这是一个Web应用程序
@ConditionalOnNotWebApplication 这不是一个Web应用程序
@Entity @Id @GeneratedValue
```

### 五、Springboot 构建过程解析和自动配置
- 可以利用gradle或者maven来打包项目，它们对SpringBoot都有很好的支持
```text
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>

```
- 起步依赖：通过parent标签可以通过依赖传递，自动给下面引用包含的依赖指定的版本号，无需自己书写
- 也可以通过一下排除依赖：<exclusions><exclusion></exclusion></exclusions>
- spring boot的自动配置需要考虑：jdbcTemlate是不是在classpath，如果在就需要准备一个DataSource的Bean,再配置jdbcTemplate的Bean
- 每当应用程序启动的时候，Spring Boot的自动配置都要做将近200个这样的决定，涵盖安全、集成、持久化、Web开发等诸多方面
- datasource的自动配置
```text
@Configuration
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ Registrar.class, DataSourcePoolMetadataProvidersConfiguration.class })
public class DataSourceAutoConfiguration {
}


@Configuration
@Conditional(DataSourceAutoConfiguration.DataSourceAvailableCondition.class)
protected static class JdbcTemplateConfiguration {
    @Autowired(required = false)
    private DataSource dataSource;
    
    @Bean
    @ConditionalOnMissingBean(JdbcOperations.class)//只有在不存在JdbcOperations（即JdbcTemplate实现的接口）类型的Bean时，才会创建JdbcTemplate Bean
    public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(this.dataSource);
    }
...
}

```
- 自动配置一个指定前端技术的Error page


### 六、Springboot 中的自定义配置
- 通过自定义一个security对象，覆盖spring boot的自动配置
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    protected  void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/").access("hasRole('READER')")
                .antMatchers("/").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
               auth
                .userDetailsService(new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                       return  readerRepository.findById(name).get();
                    }
                });

    }
}
```
- 要覆盖Spring Boot的自动配置，需要编写一个显式的配置。Spring Boot会发现你的配置，根据@ConditionOnMissingBean 覆盖自动配置，以你的配置为准，JdbcTemplate的注入中就有这个注解
```text
##以上我们自己定义了一个SecurityConfig ,@EnableWebSecurity ,我们项目也是一个WEB项目，因而，spring boot的自动装配就在@ConditionalOnMissingBean(WebSecurityConfiguration.class)上面卡住了，以我们配置的为准
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass({ EnableWebSecurity.class })
@ConditionalOnMissingBean(WebSecurityConfiguration.class)
@ConditionalOnWebApplication
public class SpringBootWebSecurityConfiguration {
...
}
```
- 

### 七、Springboot 中的配置文件
- springboot的配置文件实现的方式有多种，可以是原生支持的配置，按照spring 的要求进行书写配置，可以是自定义配置文件，通过@Value或者@PropertySource进行注入，或者通过JVM或者外部参数等
- 以下配置优先级从高到低，低优先级的配置项会被高优先级替代
```text
(1) 命令行参数
(2) java:comp/env里的JNDI属性
(3) JVM系统属性
(4) 操作系统环境变量
(5) 随机生成的带random.*前缀的属性（在设置其他属性时，可以引用它们，比如${random.
long}）
(6) 应用程序以外的application.properties或者appliaction.yml文件
(7) 打包在应用程序内的application.properties或者appliaction.yml文件
(8) 通过@PropertySource标注的属性源
(9) 默认属性
```
- 对于spring自带配置文件优先级也有讲究,以下优先级从高到低，/config目录下的优先级最高，能覆盖里面所有的，，如果在同一优先级位置同时有application.properties和application.yml，那么application.yml里的属性会覆盖application.properties里的属性
```text
# application.properties和application.yml文件能放在以下四个位置。
(1) 外置，在相对于应用程序运行目录的/config子目录里。
(2) 外置，在应用程序运行的目录里。
(3) 内置，在config包内。
(4) 内置，在Classpath根目录。
```
- 以上优先级的用处就是，你可以再提交jar包，比如java -jar的时候，通过外部命令--server.port临时修改配置文件配置，而不需要重新打包，并且这个是临时的，如果需要长久修改就最好作用在内部配置文件中

### 八、测试环境配置HTTPS访问
```text
server:
    port: 8443
    ssl:
        key-store: file:///path/to/mykeys.jks
        key-store-password: xxxxx
        key-password: xxxxxx
```

### 九、springboot 配置日志
- 默认使用logback记录日志，并使用INFO级别输出到Console
- 如果你需要使用Log4j,你就需要排除logback的依赖
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```
- 配置logback.xml
```text
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>
    <logger name="root" level="INFO"/>
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```
- 自己写Logback.xml可以完全控制日志的输出情况，如果只想简单支配日志的配置，可以直接通过spring的配置
```text
logging.path=/var/logs/
logging.file=BookWorm.log
logging.level.root=WARN
logging.level.root.org.springframework.security=DEBUG

#或者通过自定义日志配置文件
logging:
    config:
        classpath:logging-config.xml
```

### 十、springboot 属性解析器
- 我们注入配置的方式有几种，一种就是使用spring给的配置，利用它的自动装配，就能把值映射上去，但是自定义的就需要自己处理了，那原理是什么
- 我们自定义属性，通常就要自定义配置类，利用@Configuration/@Component的注解，利用@PropertySource或者@ConfigurationProperties进行注入，但是从技术来看，@ConfigurationProperties一个注解是不足够的，那spring是怎么帮你注入的呢？
- 它利用在每一个部配置类都已经加上了@EnableConfigurationProperties注解，是自己书写的注解能够生效，这就很只能了
- 并且它能够根据驼峰映射到配置类的属性上，不需要做过多配置，amazon.associateId 这个属性和amazon.associate_id以及amazon.associate-id都是等价的
```text
@Component
@ConfigurationProperties("amazon")
public class AmazonProperties {
    private String associateId;
    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }
    public String getAssociateId() {
        return associateId;
    }
}
```       
### 十一、springboot 分环境配置
- @Profile的注解能够接收一个环境的value,与之对应的是application-%s.properties，例如@Peofile("dev"),说明当前类使用dev配置
- 通过soring.profiles.active=dev也可以全局配置环境，或者通过提交的时候--spring.profiles.active=dev实现
- 既然是分环境了，就可以针对不同的环境给定不同的配置，比如JDBC连接，比如日志输出情况等
```text
application-dev.properties/application-development.properties   本地开发环境

application-test.properties        继承测试环境

application-prod.properties/application-production.properties   产线部署环境

```   
### 十二、Springboot test测试
- 当你创建了一个spring的项目，会有一个test文件夹，用于放置test文件
- test文件只是一个class,如何加载spring环境，支持接口或者服务的测试呢？
- 首先肯定需要引入junit测试相关的@RunWith(SpringJUnit4ClassRunner.class)，开启Spring集成测试支持
- 其次需要装配spring的上下文，需要用到spring的日志、
                              加载外部属性（application.properties或application.yml），以及其他Spring Boot特性需要使用@SpringApplicationConfiguration，单纯的@ContextConfiguration上下文加载无法达成spring的要求
```text
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
classes=AddressBookConfiguration.class)
public class AddressServiceTests {
...
}
```
- 如果我们需要用到spring mvc相关的测试环节的时候，就需要一个测试控制器，要用webAppContextSetup()，webAppContextSetup()接受一个WebApplicationContext参数。因此，我们需要为测试类加上@WebAppConfiguration注解，使用@Autowired将WebApplicationContext作为实例变量注入测试类。
```text
@SpringApplicationConfiguration(
classes = ReadingListApplication.class)
@WebAppConfiguration
public class MockMvcWebTests {
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders
                    .webAppContextSetup(webContext)
                    .build();
    }
}
```
- 做一个页面请求测试
```text
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Test
public void homePage() throws Exception {
    mockMvc.perform(get("/readingList"))
        .andExpect(status().isOk())
        .andExpect(view().name("readingList"))
        .andExpect(model().attributeExists("books"))
        .andExpect(model().attribute("books", is(empty())));
}



@Test
public void postBook() throws Exception {
//POST
        mockMvc.perform(post("/readingList")      
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "BOOK TITLE")
                .param("author", "BOOK AUTHOR")
                .param("isbn", "1234567890")
                .param("description", "DESCRIPTION"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/readingList"));
                
        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setReader("craig");
        expectedBook.setTitle("BOOK TITLE");
        expectedBook.setAuthor("BOOK AUTHOR");
        expectedBook.setIsbn("1234567890");
        expectedBook.setDescription("DESCRIPTION");
        
        //GET
        mockMvc.perform(get("/readingList"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)))
                .andExpect(model().attribute("books",
                contains(samePropertyValuesAs(expectedBook))));
}
```
- 页面安全测试
```text
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>


@Before
public void setupMockMvc() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webContext)
        .apply(springSecurity()) //，为Mock MVC开启了Spring Security支持,Spring Security会介入MockMvc上执行的每个请求
        .build();
}


@Test
@WithMockUser(username="craig",   //自定义一个用户，不需要去查询UserDetails
                password="password",
                roles="READER")
/*@WithUserDetails("craig")  // 指定加载这一用户的信息*/
public void homePage_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location","http://localhost/login"));
}
```
- 在测试类中进行HTTP请求，加载一个嵌入式Tomcat
```text
@RunWith(SpringJUnit4ClassRunner.class)
 @SpringApplicationConfiguration(
 classes=ReadingListApplication.class)
 @WebIntegrationTest  //加载一个嵌入的容器
 public class SimpleWebTest {
 @Test(expected=HttpClientErrorException.class)
 public void pageNotFound() {
        try {
            RestTemplate rest = new RestTemplate();
            rest.getForObject("http://localhost:8080/bogusPage", String.class);
            fail("Should result in HTTP 404");
        } catch (HttpClientErrorException e) {
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
         throw e;
        }
    }
 }

```
- 随意设置端口，自己指定：@WebIntegrationTest(value={"server.port=0"})，随机生成：@WebIntegrationTest(randomPort=true)，自己配置：@Value("${local.server.port}")




### 十三、springboot + groovy / grails
### 十四、Springboot + Actuator
- actuator是监控spring容器，内部资源情况的一个很好的手段
```text
GET /autoconfig 提供了一份自动配置报告，记录哪些自动配置条件通过了，哪些没通过
GET /configprops 描述配置属性（包含默认值）如何注入Bean
GET /beans 描述应用程序上下文里全部的Bean，以及它们的关系  ---- Bean的配置（bean,resource,dependency,scope,type）
GET /dump 获取线程活动的快照 
GET /env 获取全部环境属性  ---- 加载环境属性
GET /env/{name} 根据名称获取特定的环境属性值
GET /health 报告应用程序的健康指标，这些值由HealthIndicator的实现类提供
GET /info 获取应用程序的定制信息，这些信息由info打头的属性提供
GET /mappings 描述全部的URI路径，以及它们和控制器（包含Actuator端点）的映射关系
GET /metrics 报告各种应用程序度量信息，比如内存用量和HTTP请求计数   -----运行数据*****（垃圾收集器,内存，堆，系统，线程池，数据源，tomcat，http）
GET /metrics/{name} 报告指定名称的应用程序度量值
POST /shutdown 关闭应用程序，要求endpoints.shutdown.enabled设置为true
GET /trace 提供基本的HTTP请求跟踪信息（时间戳、HTTP头等）------- 可以追踪web请求
```
- 添加依赖
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
- 除了通过Rest端点，还可以通过嵌入任意Java应用程序的shell，但是可读性不高并不推荐

#### 1.谈谈自定义actuator
- 修改端点ID
```text
endpoints:
    shutdown:
        id: kill
```
- 启用和禁用端口
```text
#先禁用再启用
endpoints:
    enabled: false
    metrics:
        enabled: true
```
- 添加自定义度量量
```text
CounterService和GaugeService Bean注入Controller,然后调用
public interface CounterService {
    void increment(String metricName);
    void decrement(String metricName);
    void reset(String metricName);
}

public interface GaugeService {
    void submit(String metricName, double value);
}


public String addToReadingList(Reader reader, Book book) {
    book.setReader(reader);
    readingListRepository.save(book);
    
    counterService.increment("books.saved");
    gaugeService.submit("books.last.saved", System.currentTimeMillis());
    
    return "redirect:/";
}

```
- 创建自定义跟踪仓库
```text
/trace端点报告的跟踪信息都存储在内存仓库里，100个条目封顶。一旦仓库满了，就开始移除老的条目，给新的条目腾出空

@Configuration
public class ActuatorConfig {
    @Bean
    public InMemoryTraceRepository traceRepository() {
        InMemoryTraceRepository traceRepo = new InMemoryTraceRepository();
        traceRepo.setCapacity(1000);
        return traceRepo;
    }
}

或将数据存储到数据库中查询，public class MongoTraceRepository implements TraceRepository 

```

- 插入自定义健康指示器
```text
@Component
public class LiveHealth implements HealthIndicator {
    @Override
    public Health health() {
    try {
        RestTemplate rest = new RestTemplate();
        rest.getForObject("http://www.amazon.com", String.class);
        return Health.up().build();
    } catch (Exception e) {
    return Health.down().build();
    }
    }
}
```
- 保护端点
```text
比如/shutdown是很危险的端点，因而只允许管理员可以访问
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/").access("hasRole('READER')")
        .antMatchers("/shutdown").access("hasRole('ADMIN')")
        .antMatchers("/**").permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .failureUrl("/login?error=true");
}


## 添加一个内置用户
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(new UserDetailsService() {
    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {
        UserDetails user = readerRepository.findOne(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
    })
        .and()
        .inMemoryAuthentication()
        .withUser("admin").password("xxxxx")
        .roles("ADMIN", "READER");
}

```
### 十六、Springboot部署应用
```text
<packaging>war</packaging>
```
- war包放在tomcat或者jetty容器运行
- jar 通过maven或者gradle打包，然后通过java -jar 或者service应用的方式部署
```text
service方式很优雅
/app            ---- 存放应用可执行jar包
/config         ---- 存放外部配置文件，优先级最高
/log            ---- 日志存放位置，centos6/centos7的部署方式不同，配置地点也不同，/var/log下面也是不错的选择
/systemd        ---- service启动脚本  centos6中放在/etc/init.d下  

需要通过软连接设置系统脚本
ln -s 源地址/test.service   /etc/systemd

通过service test status /start /stop

```

### 十七、Springboot 数据库迁移--- Spring Boot为两款流行的数据库迁移库提供了自动配置支持
- flyway
    - 使用SQL来定义迁移脚本。它的理念是，每个脚本都有一个版本号，Flyway会顺序执行这些脚本，让数据库达到期望的状态。它也会记录已执行的脚本状态，不会重复执行
    - V2_initialize.sql   V+版本+“_”+描述+.sql
    - 脚本需要放在src/main/resources/db/migration
    - 将spring.jpa.hibernate.ddl-auto设置为none
    - maven依赖
```text
<dependency>
    <groupId>org.flywayfb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

- liquibase
    - Liquibase并不局限于特定平台的SQL，可以用多种格式书写迁移脚本，不用关心底层平台（其中包括XML、YAML和JSON）。如果你有这个期望的话，Liquibase当然也支持SQL脚本
    - 在/db/changelog（相对于Classpath根目录）里查找db.changelog-master.yaml文件 ，或者写成master.xml里面配置对应的changelog
    - 比较常用的是xml的配置，json或者sql，json,yaml都可以支持
    - Liquibase变更集都集中在一个文件里，flyway是没有一个版本就需要有一个文件
    - application.properties中路径配置：liquibase: change-log: classpath:/db/changelog/db.changelog-master.xml
    - maven依赖
```text
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```    
      

### 十八、Springboot 开发者工具
- 开发工具可以提供的功能，一般是用于测试过程中，产线不需要这个工具，但是它的存在并不会影响
```text
自动重启：当Classpath里的文件发生变化时，自动重启运行中的应用程序。
LiveReload支持：对资源的修改自动触发浏览器刷新 ----- 一些前端的修改的动态展示的支持 spring.devtools.livereload.enabled设置为false
远程开发：远程部署时支持自动重启和LiveReload。
默认的开发时属性值：为一些属性提供有意义的默认开发时属性值。
```