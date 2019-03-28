> 来自[程序猿DD-Springboot基础教程](http://blog.didispace.com/spring-boot-learning-1x/)

> 感觉偏实践，但是例子相对不太多


### 工程目录
实体Entity   ---domain
逻辑层Serivce  ----service 
Web层 web     ---controller


### WEB 开发
#### Springboot开发RestFul接口服务
@RestController
@Controller + @ResponseBody
@RequestMapping




#### Springboot + 模板引擎，静态资源
/static
/public 
/resources
/META-INF/resources



/src/main/resources/templates

Thymeleaf  --- Thymeleaf是⼀个XML/XHTML/HTML5模板引擎，可⽤于Web与⾮Web环境中的应⽤开发,提供了⼀个⽤于整合Spring MVC的可选模块
```text
# Enable template caching.
spring.thymeleaf.cache=true
# Check that the templates location exists.
spring.thymeleaf.check-template-location=true
# Content-Type value.
spring.thymeleaf.content-type=text/html
# Enable MVC Thymeleaf view resolution.
spring.thymeleaf.enabled=true
# Template encoding.
spring.thymeleaf.encoding=UTF-8
# Comma-separated list of view names that should be excluded from resolution.
spring.thymeleaf.excluded-view-names=
# Template mode to be applied to templates. See also StandardTemplateModeHandlers.
spring.thymeleaf.mode=HTML5
# Prefix that gets prepended to view names when building a URL.
spring.thymeleaf.prefix=classpath:/templates/
# Suffix that gets appended to view names when building a URL.
spring.thymeleaf.suffix=.html spring.thymeleaf.template-resolver-order= # Order of
the template resolver in the chain. spring.thymeleaf.view-names= # Comma-separated
list of view names that can be resolved.
```

FreeMarker
Velocity
Groovy
Mustache

#### Springboot + 统一异常处理
之前也有尝试过了
@ControllerAdvice
@RestControllerAdvice
@ExceptionHandler

可以设计一个ErrorInfo的对象，那之前使用的十ApiResponse 统一返回


#### Springboot + swaggerui2
这个之前也有尝试过了
```text
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.8.0</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.8.0</version>
</dependency>
```
@EnableSwagger2
@Api
@ApiOperation(value="更新⽤户详细信息", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新⽤户详细信息")
@ApiImplicitParam(name = "user", value = "⽤户详细实体user", required = true, dataType = "User")

### Springboot xml请求转换
```text
public interface HttpMessageConverter<T> {
boolean canRead(Class<?> clazz, @Nullable MediaType mediaType);
boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType);
List<MediaType> getSupportedMediaTypes();
T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOExcept
ion, HttpMessageNotReadableException;
void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessag
e) throws IOException, HttpMessageNotWritableException;
}
```
- Spring MVC中默认已经有⼀套采⽤Jackson实现的转换器 MappingJackson2XmlHttpMessageConverter
- 引入maven配置就可以拥有MappingJackson2XmlHttpMessageConverter实现：
```text
<dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-xml</artifactId>
        </dependency>
        
        
public class MappingJackson2XmlHttpMessageConverter extends AbstractJackson2HttpMessageConverter {

	/**
	 * Construct a new {@code MappingJackson2XmlHttpMessageConverter} using default configuration
	 * provided by {@code Jackson2ObjectMapperBuilder}.
	 */
	public MappingJackson2XmlHttpMessageConverter() {
		this(Jackson2ObjectMapperBuilder.xml().build());
	}

	/**
	 * Construct a new {@code MappingJackson2XmlHttpMessageConverter} with a custom {@link ObjectMapper}
	 * (must be a {@link XmlMapper} instance).
	 * You can use {@link Jackson2ObjectMapperBuilder} to build it easily.
	 * @see Jackson2ObjectMapperBuilder#xml()
	 */
	public MappingJackson2XmlHttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper, new MediaType("application", "xml"),
				new MediaType("text", "xml"),
				new MediaType("application", "*+xml"));
		Assert.isInstanceOf(XmlMapper.class, objectMapper, "XmlMapper required");
	}


	/**
	 * {@inheritDoc}
	 * The {@code ObjectMapper} parameter must be a {@link XmlMapper} instance.
	 */
	@Override
	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.isInstanceOf(XmlMapper.class, objectMapper, "XmlMapper required");
		super.setObjectMapper(objectMapper);
	}

}
```  
- 可以通过xml请求http接口，然后返回也会是XML格式的,可以尝试
```text
<Person>
    <id>1</id>
    <name>claire</name>
    <age>8</age>
</Person>
```
#### spring-Security
- 这个也有通过学习Book1中学习到了一些
- 可以通过配置用户，设置权限，设置用户可以访问和不能访问的请求
- 内置一个用户用于测试，在configure内部配置路由规则，和需要的权限等
```text
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected  void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/","/home").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("pass")
                .roles("ADMIN");
    }
}
```


### Springboot 日志
#### 1.Springboot 日志管理
logback:
logback.xml ---- 不推荐
logback-spring.xml  ---- 推荐
logback-spring.groovy
logback.groovy

Log4j： 
log4j-spring.properties 
log4j-spring.xml 
log4j.properties 
log4j.xml
 
Log4j2：
log4j2-spring.xml 
log4j2.xml


JDK (Java Util Logging)： 
logging.properties


#### 2.Springboot + AOP请求
@Aspect   ---- 开启AOP标识
@Pointcut  ---- 指定切入点

@Before ---- 从切入点开始切入内容
@After   ----- 在切入点结尾处切入内容
@AfterReturning  ----- 在切⼊点return内容之后切⼊内容
@Around  ---- 在切⼊点前后切⼊内容，并⾃⼰控制何时执⾏切⼊点⾃身的内容
@AfterThrowing ----- ⽤来处理当切⼊内容部分抛出异常之后的处理逻辑

- 使用AOP做一次数据拦截，添加日志
```text
@Aspect
@Component
public class LogAscept {
    private Logger log = LoggerFactory.getLogger(LogAscept.class);
    ThreadLocal<Long> startTime = new ThreadLocal<Long>(); //使用线程变量，准确计算时间


    @Pointcut("execution(public * com.notes.web..*.*(..))")
    public  void webLog(){

    }


    @Before("webLog()")
    public void doBeforeAction(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();

        //记录下请求的内容，或者参数等，进行请求次数的统计等
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));



    }

    @AfterReturning(returning = "obj",pointcut = "webLog()")
    public void doAfterAction(Object obj){
        //记录请求返回的对象，比如对controller请求的返回JSON值
       log.info("after returning :"+obj);
       //记录接口请求时间
       long time = System.currentTimeMillis() - startTime.get();
       log.info("request time :"+time);
    }
}

```

#### 3.切面的处理顺序
- 通过AOP实现，程序得到了很好的解耦，但是也会带来⼀些问题，⽐如：我们可能会对Web层做多个切⾯，校验⽤户，校验头信息等等，这个时候经常会碰到切⾯的处理顺序问题
- 需要 @Order(i) 注解来标识切⾯的优先级。i的值越⼩，优先级越⾼
```text
在切⼊点前before的操作，按order的值由⼩到⼤执⾏ ,先Order(1),再Order(10)
在切⼊点后的操作，按order的值由⼤到⼩执⾏，先Order(10)，再Order(1)
```

#### 4.Springboot + log4j配置
- 添加maven依赖，注意需要去除web模块中默认的logback日志
- 添加Log4j.properies
- 一般日志需要注意的，是不同输出地方的日志级别，有没有需要对不同包或者路径下的输出做特殊处理，是否需要对输出到文件做大小数量的限制和切分，是否需要对接mongodb 或者elasticsearch 做日志的特殊输出
```text
# LOG4J配置
log4j.rootCategory=INFO, stdout,file
log4j.category.com.notes.web=DEBUG, webfile
# 控制台输出
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n

# 文件⽇志输出
log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
log4j.appender.file.file=logs/app.log
log4j.appender.file.DatePattern='.'yyyy-MM-dd
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n

## webfile
log4j.appender.webfile=org.apache.log4j.DailyRollingFileAppender
log4j.appender.webfile.file=logs/my.log
log4j.appender.webfile.DatePattern='.'yyyy-MM-dd
log4j.appender.webfile.layout=org.apache.log4j.PatternLayout
log4j.appender.webfile.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %5p %c{1}:%L ---- %m%n
```

#### 5.Springboot +mongodb + log
#### 6. [Log4j2、Log4j、Logback自定义Appender实现](https://blog.csdn.net/u014263388/article/details/78608045)

#### 7. 动态修改日志级别
```text
1.application.properties中修改：management.security.enabled=false,关闭安全认证校验

2.发送POST请求到 /loggers/com.notes 端点,查看com.notes下日志输出的等级，其中请求体Body内容为
{
"configuredLevel":":"DEBUG"
}

3. GET 获取日志输出等级，发送GET请求到 /loggers/com.notes 端点
{
"configuredLevel":":"INFO",
"effectiveLevel":":"INFO"
}
```



### Springboot 数据访问
#### 1.Springboot + JdbcTemplate
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.21</version>
</dependency>

```
```text
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=xxxx
spring.datasource.password=xxxx
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```
- 利用原生的DataSource 注入JdbcTemplate对象，执行sql
```text
jdbcTemplate.queryForObject("select count(1) from USER", Integer.class);
```

#### 2.Springboot + jpa
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
```text
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop //⾃动创建、更新、验证数据库表结构,一般用update

```
```text
1.create ：每次加载hibernate时都会删除上⼀次的⽣成的表，然后根据你的model类再重新来⽣
成新表，哪怕两次没有任何改变也要这样执⾏，这就是导致数据库表数据丢失的⼀个重要原因。

2.create-drop ：每次加载hibernate时根据model类⽣成表，但是sessionFactory⼀关闭,表就⾃动
删除。

3.update ：最常⽤的属性，第⼀次加载hibernate时根据model类会⾃动建⽴起表的结构（前提是
先建⽴好数据库），以后加载hibernate时根据model类⾃动更新表结构，即使表结构改变了但表
中的⾏仍然存在不会删除以前的⾏。要注意的是当部署到服务器后，表结构是不会被⻢上建⽴起
来的，是要等应⽤第⼀次运⾏起来后才会。

4.validate ：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进⾏⽐较，不
会创建新表，但是会插⼊新值
```

- 利用简洁的接口书写方式，实现约定优于配置的查询，遵守驼峰规则，大大简化了查询，对于多表复杂的查询力不从心
- 支持注解形式，做自定义的sql @Query("from User u where u.name=:name")

#### 3.Springboot + 多数据源
@Primary
配置多个数据源，设置一个主库

#### 4.Springboot + Mybatis 
使用那些mapper实现单表操作
可以使用反向生成mapper模板
也可以使用注解：@Select("SELECT * FROM USER WHERE NAME = #{name}")
@Select @Update @Insert @Delete
@Results({
@Result(property = "name", column = "name")
})


#### 5.Springboot + Ehcache
@EnableCaching 开启缓存
@CacheConfig(cacheNames = "users")
@Cacheable 查询数据的缓存
CacheManager
@CachePut  与 @Cacheable 不同的是，它每次都会真是调⽤函数，所以主要⽤于数据新增和修改操作上
@CacheEvict  删除数据，移除缓存中数据

通过@EnableCaching就自动化配置了一个CacheManager,会按照以下顺序配置缓存
```text
Generic
JCache (JSR-107)
EhCache 2.x
Hazelcast
Infinispan
Redis
Guava
Simple
```
也可以通过spring.cache.type指定
ehcache的使用非常便利
src/main/resources ehcache.xml
```text
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:noNamespaceSchemaLocation="ehcache.xsd">
<cache name="users" maxEntriesLocalHeap="200" timeToLiveSeconds="600">
</cache>
</ehcache>
```
添加依赖
```text
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
</dependency>
```


#### 6.Springboot + redis
分布式缓存
内存数据库
持久化
分布式锁
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-redis</artifactId>
</dependency>


spring.redis.host=localhost
spring.redis.port=6379
spring.redis.pool.max-idle=8
spring.redis.pool.min-idle=0
spring.redis.pool.max-active=8
spring.redis.pool.max-wait=-1
```
使⽤ RedisCacheManager 初始化 CacheManager


#### 7.Springboot + MongoDB
连接nosql数据库
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

public interface UserRepository extends MongoRepository<User, Long>

```

连接池的配置
```text
<dependency>
    <groupId>com.spring4all</groupId>
    <artifactId>mongodb-plus-spring-boot-starter</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```
在主类上添加@EnableMongoPlus
```text
spring.data.mongodb.option.min-connection-per-host=0
spring.data.mongodb.option.max-connection-per-host=100
spring.data.mongodb.option.threads-allowed-to-block-for-connection-multiplier=5
spring.data.mongodb.option.server-selection-timeout=30000
spring.data.mongodb.option.max-wait-time=120000
spring.data.mongodb.option.max-connection-idle-time=0
spring.data.mongodb.option.max-connection-life-time=0
spring.data.mongodb.option.connect-timeout=10000
spring.data.mongodb.option.socket-timeout=0
spring.data.mongodb.option.socket-keep-alive=false
spring.data.mongodb.option.ssl-enabled=false
spring.data.mongodb.option.ssl-invalid-host-name-allowed=false
spring.data.mongodb.option.always-use-m-beans=false
spring.data.mongodb.option.heartbeat-socket-timeout=20000
spring.data.mongodb.option.heartbeat-connect-timeout=20000
spring.data.mongodb.option.min-heartbeat-frequency=500
spring.data.mongodb.option.heartbeat-frequency=10000
spring.data.mongodb.option.local-threshold=15

```

#### 8.Springboot + LDAP
用户统一数据管理
用户数据的同步
LDAP（轻量级⽬录访问协议，Lightweight Directory Access Protocol)是实现提供被称为⽬录服务的信息服务
LDAP⽬录中的信息是是按照树型结构组织，具体信息存储在条⽬(entry)的数据结构
```text
o：organization（组织-公司）
ou：organization unit（组织单元-部⻔）
c：countryName（国家）
dc：domainComponent（域名）
sn：surname（姓⽒）
cn：common name（常⽤名称）
```
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-ldap</artifactId>
</dependency>
<dependency>
    <groupId>com.unboundid</groupId> //一个ldap的服务端用于测试
    <artifactId>unboundid-ldapsdk</artifactId>
    <scope>test</scope>
</dependency>
```

src/main/resources/ldap-server.ldif
```text
dn: dc=xxx,dc=com
objectClass: top
objectClass: domain

dn: ou=people,dc=xxx,dc=com
objectclass: top
objectclass: organizationalUnit
ou: people

dn: uid=ben,ou=people,dc=xxx,dc=com
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: xxx
sn: ccc
uid: xxx
userPassword: {SHA}nFCebWjxfaLbHHG1Qk5UU4trbvQ=
```

```text
spring.ldap.embedded.ldif=ldap-server.ldif
spring.ldap.embedded.base-dn=dc=xxx,dc=com
```
```text
spring.ldap.urls=ldap://localhost:1235
spring.ldap.base=dc=xxx,dc=com
spring.ldap.username=xxxx
spring.ldap.password=ddddd
```

#### 9.Springboot + transaction
spring 事务@Transactional
使⽤了spring-boot-starter-jdbc或spring-boot-starter-data-jpa依赖的时候，框架会⾃动默认分别注⼊DataSourceTransactionManager或JpaTransactionManager
所以，不需要做其他的配置，就可以使用@Transactional注解，将这个注解放置在需要的方法上即可
这个注解还可以针对service的一个方法上，对于真个业务逻辑的回滚
在多数据源的情况下，需要对执行回滚的事务管理器进行指定，@Transactional(value="transactionManagerPrimary")
 transaction中定义了5中数据库隔离界别的枚举
```text
public enum Isolation {
DEFAULT(-1),      ---- 默认的read_committed
READ_UNCOMMITTED(1),   ---⼀个事务可以读取另⼀个事务修改但还没有提交的数据
READ_COMMITTED(2),  --- ⼀个事务只能读取另⼀个事务已经提交的数据
REPEATABLE_READ(4),  ---  示⼀个事务在整个过程中可以多次重复执⾏某个查询，并且每次返回的记录都相同。
SERIALIZABLE(8);  --- 所有的事务依次逐个执⾏，这样事务之间就完全不可能产⽣⼲扰
}
```
设置执行事务的隔离级别@Transactional(isolation = Isolation.DEFAULT)

事务的传播行为
```text
public enum Propagation {
REQUIRED(0),   -----如果当前存在事务，则加⼊该事务；如果当前没有事务，则创建⼀个新的事务
SUPPORTS(1), ---- 如果当前存在事务，则加⼊该事务；如果当前没有事务，则以⾮事务的⽅式继续运⾏
MANDATORY(2),   ---- 如果当前存在事务，则加⼊该事务；如果当前没有事务，则抛出异常
REQUIRES_NEW(3),  ---- 创建⼀个新的事务，如果当前存在事务，则把当前事务挂起
NOT_SUPPORTED(4),  ----以⾮事务⽅式运⾏，如果当前存在事务，则把当前事务挂起
NEVER(5),     ---- 以⾮事务⽅式运⾏，如果当前存在事务，则抛出异常
NESTED(6);     ---- 如果当前存在事务，则创建⼀个事务作为当前事务的嵌套事务来运⾏；如果当前没有事务，则该取值等价于 REQUIRED
}
```
同样可以通过属性进行配置@Transactional(propagation = Propagation.REQUIRED)


#### 10.Springboot + flyway/liquibase
数据迁移，数据库版本的维护
 
### 定时任务
异步，线程池方式之前有尝试过
#### 1.Springboot + Schduled
#### 2.Springboot + Async

### 消息中间件
#### Springboot + rabbitMQ
#### rocketMQ
#### kafka

### 其他配置
#### 自动化配置
#### 后台运行配置
部署
- nohup java -jar yourapp.jar &
- stop.sh脚本
```text
#!/bin/bash
PID=$(ps -ef | grep yourapp.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
    echo Application is already stopped
else
    echo kill $PID
    kill $PID
fi
```
- start.sh
```text
#!/bin/bash
nohup java -jar app.jar --server.port=8888 &
```
- run.sh
```text
#!/bin/bash
echo stop application
source stop.sh
echo start application
source start.sh
```
```text
<plugins>
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
            <executable>true</executable>
        </configuration>
    </plugin>
</plugins>
```
- centos6 
```text
sudo ln -s /var/xxx/app.jar /etc/init.d/app
/etc/init.d/yourapp start|stop|restart
```
- centos7 参考官方service方式发布

#### 发邮件
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```
```text
spring.mail.host=smtp.qq.com
spring.mail.username=⽤户名
spring.mail.password=密码
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```
```text
    @Autowired
    private JavaMailSender mailSender;

public void sendSimpleMail() throws Exception {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("xxxxx@qq.com");
    message.setTo("xxxxxx@qq.com");
    message.setSubject("主题：邮件");
    message.setText("测试邮件内容");
    mailSender.send(message);
}

```
JavaMailSender 实例会自动配置
还可以发送带附件的邮件，发送静态资源
```text
FileSystemResource file = new FileSystemResource(new File("weixin.jpg"));
helper.addAttachment("附件-1.jpg", file);
helper.addAttachment("附件-2.jpg", file);

helper.setText("<html><body><img src=\"cid:weixin\" ></body></html>", true)
```

定制一个模板邮件
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-velocity</artifactId>
</dependency>
```


### actuator监控
监控Spring容器内Bean的状况，内存消耗情况，应用的存活，请求参数，停止等
```text
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
Book1中已经详细介绍了

添加 git-commit-id-plugin 插件，该插件⽤来产⽣git的版本信息,能够git.properties，包含git的数据
```text
<plugin>
    <groupId>pl.project13.maven</groupId>
    <artifactId>git-commit-id-plugin</artifactId>
    <version>2.1.15</version>
    <executions>
        <execution>
            <goals>
                <goal>revision</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <dotGitDirectory>${project.basedir}/.git</dotGitDirectory>
    </configuration>
</plugin>
```