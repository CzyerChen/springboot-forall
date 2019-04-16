> 之前有提到缓存可以减轻数据库的访问，那缓存也可以在存储会话这种典型的临时数据

> session不仅仅是能够缓存会话，还能够实现一些在线人数统计、剔除人之类的操作

### 什么是会话
- 会话，即用户访问应用时保持的连接关系，在多次交互中应用能够识别出当前访问的用户是谁，且可以在多次交互中保存一些数据

### 创建会话
- 在shiro当中，不需要去显式地create一个session
- 登录成功后使用Subject.getSession()即可获取会话；其等价于Subject.getSession(true)
- 如果当前没有创建Session 对象会创建一个；另外Subject.getSession(false)，如果当前没有创建Session 则返回null（不过默认情况下如果启用会话存储功能的话在创建Subject 时会主动创建一个Session）
```text
Subject subject = SecurityUtils.getSubject();
Session session = subject.getSession();
```
- getSession 默认创建为true ,会生成一个session上下文，然后通过securitymanager.start 开启一个会话，最终通过装饰返回一个带有session信息的对象
```text
session.getId();  //session.getId();

session.getHost() // 获取当前Subject的主机地址 这个host还用在session 的创建中 

session.getTimeout();
session.setTimeout(毫秒); // 获取/设置当前Session的过期时间

session.getStartTimestamp();
session.getLastAccessTime();

session.touch();
session.stop();

```
### 会话管理器：sessionManager
- 会话管理器管理着应用中所有Subject的会话的创建、维护、删除、失效、验证等工作
- 是Shiro 的核心组件，顶层组件SecurityManager 直接继承了SessionManager，且提供了SessionsSecurityManager 实现直接把会话管理委托给相应的SessionManager 
- DefaultSecurityManager 及DefaultWebSecurityManager 默认SecurityManager 都继承了SessionsSecurityManager
- 提供的接口
```text
Session start(SessionContext context); //启动会话
Session getSession(SessionKey key) throws SessionException; //根据会话Key获取会话
```
- 三个sessionManager的实现
```text
DefaultSessionManager --- DefaultSecurityManager使用的默认实现，用于JavaSE 环境

ServletContainerSessionManager --- DefaultWebSecurityManager使用的默认实现，用于Web环境，其直接使用Servlet 容器的会话

DefaultWebSessionManager : 用于Web 环境的实现， 可以替代ServletContainerSessionManager，自己维护着会话，直接废弃了Servlet容器的会话管理

```
- 在 Servlet容器中，默认使用JSESSIONID Cookie 维护会话，且会话默认是跟容器绑定的；通常浏览器都会在cookie中存储JSESSIONID
- 一些cookie的配置 
```text
 SimpleCookie cookie = new SimpleCookie();
        cookie.setName("token");//设置Cookie 名字，默认为JSESSIONID；
        cookie.setMaxAge(1800);//设置Cookie 的过期时间，秒为单位，默认-1 表示关闭浏览器时
                                 过期Cookie
        cookie.setHttpOnly(true); //如果设置为true，则客户端不会暴露给客户端脚本代码，使用
                                    HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；此特性需要实现了Servlet 2.5 MR6
                                    及以上版本的规范的Servlet容器支持；
        cookie.setDomain("test.com");//设置Cookie的域名，默认空，即当前访问的域名
        cookie.setPath();//：设置Cookie 的路径，默认空，即存储在域名根下
        defaultWebSessionManager.setSessionIdCookie(cookie);  //sessionManager创建会话Cookie的模板
        defaultWebSessionManager.setSessionIdCookieEnabled(true);//是否启用/禁用Session Id Cookie，默认是启用的
```

### 会话监听器：SessionListener
- 用于监听会话创建、过期、停止等，比如进行全局会话的统计，日志的记录等，类似于AOP的实现
- 方法一：实现接口，需要实现start stop expire的多重方法的实现，全面完整地实现监听器
```text
public class ShiroSessionListener implements SessionListener {

    private final AtomicLong sessionCount = new AtomicLong(0L);
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
    }

    @Override
    public void onStop(Session session) {
          sessionCount.decrementAndGet();
    }

    @Override
    public void onExpiration(Session session) {
       sessionCount.decrementAndGet();
    }
}

```
- 方法二：继承适配器类，进行方法的重写，来实现某一方面的监听
```text
public class SelfSeesionListner extends SessionListenerAdapter {

    @Override
    public void onStart(Session session) {
        
    }
}
```
- 关系
```text
----------- SessionListener I : start stop expire
|                       |
|                       |
|                       |
|     SessionListenerAdapter class: 空的实现类，包含start stop expire三种空方法
|                       |                  
|                       |
|                       |
|---------------继承或者实现接口，实现对应的方法，实现会话的监听
                  
```

### sessionDAO的创建
- sesssionDAO主要用于对session信息的持久化，一般会和缓存框架结合实现
- 框架结构
```text
                    SessionDAO  I
                        |
                        |
                 AbstreactSessionDAO C  --- 一般需要自己实现，可以继承这个类,提供了SessionDAO的基础实现，如生成会话ID等
                        |
         |--------------------------------------|
    CachingSessionDAO            MemorySessionDAO:new ConcurrentHashMap<Serializable, Session>()
    提供了对开发者透明的              直接在内存中进行会话维护
    会话缓存的功能         
            |
 EnterpriseCacheSessionDAO
 缓存功能的会话维护，默认情况下使用MapCache 实现，
 内部使用ConcurrentHashMap 保存缓存的会话
 new MapCache<Serializable, Session>(name, new ConcurrentHashMap<Serializable, Session>())


                             Cache I 
                                |
                                |
         |----------------------|---------------------|
    Ehcache(shiro实现)     MapCache(shiro实现)   RedisCache（三方实现 redisSessionDAO）
    
        
                          Cache Manager I
                                |
                                |
    |-------------------------|------------------------|-------------------------|                            
AbstreactCacheManager   EhCacheManager   MemoryConstrainedCacheManager    RedisCacheManger               
```
- EhCacheManager
```text
需要文件shiro-ehcache.xml
  @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
        return ehCacheManager;
    }
```
- RedisCacheManager
```text
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
```
- sessionID创建策略
```text
    public AbstractSessionDAO() {
        this.sessionIdGenerator = new JavaUuidSessionIdGenerator();
    }


 protected Serializable generateSessionId(Session session) {
        if (this.sessionIdGenerator == null) {
            String msg = "sessionIdGenerator attribute has not been configured.";
            throw new IllegalStateException(msg);
        }
        return this.sessionIdGenerator.generateId(session);
    }
      
                         SessionIdGenerator I
                                |
             |---------------------------------------|
  JavaUuidSessionIdGenerator 默认        RandomSessionIdGenerator
  UUID.randomUUID().toString()           Long.toString(getRandom().nextLong())
```

#### 实现一个sessionDAO的持久化
- 如果自定义一个SessionDAO
```text
public class SelfSessionDAO  extends CachingSessionDAO {

    public SelfSessionDAO(){
        setSessionIdGenerator(new RandomSessionIdGenerator());
    }
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        //持久化session 可以缓存可以DB
        return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        //从缓存或者DB获取缓存信息
        return null;
    }
    
    @Override
    protected void doUpdate(Session session) {
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()){
            return;//会话已停止，无需更新
        }
        //不然就通过缓存或者DB更新session
    }

    @Override
    protected void doDelete(Session session) {
        //缓存操作或者DB操作删除session信息
    }
    
}
```

#### redisSessionDAO
- 注入SeesionDAO
```text
 @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
```

- 实现SeesionListner接口 可以做全局的监听
```text
public class ShiroSessionListener implements SessionListener {

    private final AtomicLong sessionCount = new AtomicLong(0L);
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
    }

    @Override
    public void onStop(Session session) {
          sessionCount.decrementAndGet();
    }

    @Override
    public void onExpiration(Session session) {
       sessionCount.decrementAndGet();
    }
}

```

- 注入SessionManager
```text
 @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        ((ArrayList<SessionListener>) listeners).add(new ShiroSessionListener());
        defaultWebSessionManager.setSessionListeners(listeners);
        defaultWebSessionManager.setSessionDAO(redisSessionDAO());
        return defaultWebSessionManager;
    }
```

- 添加sessionManager
```text
 manager.setSessionManager(sessionManager());
```

### sessionDAO的运用
- 在线人数的统计
```text
@Override
    public List<UserStatistic> list() {
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        List<UserStatistic> list = new ArrayList<>();

        for(Session session : activeSessions){
            UserStatistic userStatistic = new UserStatistic();
            //TUser user = new TUser();
            SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null){
                continue;
            }else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                String primaryPrincipal = (String)principalCollection.getPrimaryPrincipal();
                userStatistic.setUsername(primaryPrincipal);
                userStatistic.setUserId(primaryPrincipal);
            }
            userStatistic.setId(String.valueOf(session.getId()));
            userStatistic.setHost(session.getHost());
            userStatistic.setStartTimestamp(session.getStartTimestamp());
            userStatistic.setLastAccessTime(session.getLastAccessTime());
            long timeout = session.getTimeout();
            if(timeout == 0){
                userStatistic.setStatus("OFFLINE");
            }else {
                userStatistic.setStatus("ONLINE");
            }
            userStatistic.setTimeout(timeout);
           list.add(userStatistic);
        }
        return list;
    }
```
- 踢人
```text
  @Override
    public boolean kickOut(String sId) {
        Session session = sessionDAO.readSession(sId);
        //让它状态为离线
        //session.setTimeout(0);
        sessionDAO.delete(session);
        return true;
    }
```


### 会话验证
- Shiro 提供了会话验证调度器，用于定期的验证会话是否已过期，如果过期将停止会话，Shiro 提供了会话验证调度器SessionValidationScheduler来做这件事情
- 默认是JDK的ScheduledExecutorService进行定期调度并验证会话是否过期
```text
                      SessionValidationScheduler I   会话验证调度器
                                 |
                                 |
                ExecutorServiceSessionValidationScheduler C  sessionManager 默认
                
                
                
                
                
                  DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
                                   onExpiration()
                                        /|\
                                         |
                                         |
                             extends DefaultSessionManager
                                        /|\
                                         |
                         extends AbstractValidatingSessionManager
                                        /|\
                                         |
                  /**
                      * Scheduler used to validate sessions on a regular basis.
                      */
                     protected SessionValidationScheduler sessionValidationScheduler;
                     
           
           在sessionManager中配置：
                   defaultWebSessionManager.setSessionValidationScheduler();
                   ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
                   scheduler.setInterval();  //设置调度时间间隔，单位毫秒，默认就是1 小时
                   scheduler.setSessionManager();
                   scheduler.setThreadNamePrefix();
                   defaultWebSessionManager.setSessionValidationInterval();
                   defaultWebSessionManager.setGlobalSessionTimeout();  //设置全局会话超时时间，默认30 分钟，即如果30分钟内没有访问会话将过期
                   defaultWebSessionManager.setSessionValidationSchedulerEnabled();//是否开启会话验证器，默认是开启的
                   
                   

                   
```

### shiro-Quartz 
- 导入依赖
```text
shiro-quartz依赖
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-quartz</artifactId>
    <version>1.2.2</version>
</dependency>

```
- 接着上面的会话验证调度器, Shiro 也提供了使用Quartz会话验证调度器，默认是JDK的ScheduledExecutorService进行定期调度并验证会话是否过期
```text
结合quartz定时器，定制灵活多样的调度器
        QuartzSessionValidationScheduler  scheduler = new QuartzSessionValidationScheduler();
        scheduler.setScheduler();
        scheduler.setSessionValidationInterval();
        scheduler.setSessionManager();
```

### SessionFactory
```text
               SessionManager 
                     |
                     |
              成员变量：SessionFactory
                     |
                     |
             构造方法：sessionFactory = new SimpleSessionFactory
             getSessionFactory().createSession(context)
                      | 
                      |
              创建Session,建立，未存储
                      |
                      |
             sessionDAO.create(session)
                      |
                   generateId
                   assignID
                   saveSession  
                       |
             最终将session存储在缓存或者DB
                   
```
- 自定义Session 自定义SessionFactory
```text
public class OnlineSession extends SimpleSession {
public static enum OnlineStatus {
on_line("在线"), hidden("隐身"), force_logout("强制退出");
private final String info;
private OnlineStatus(String info) {
this.info = info;
}
public String getInfo() {
return info;
}
}
private String userAgent; //用户浏览器类型
private OnlineStatus status = OnlineStatus.on_line; //在线状态
private String systemHost; //用户登录时系统IP
//省略其他
}
```
```text
public class OnlineSessionFactory implements SessionFactory {
@Override
public Session createSession(SessionContext initData) {
OnlineSession session = new OnlineSession();
if (initData != null && initData instanceof WebSessionContext) {
WebSessionContext sessionContext = (WebSessionContext) initData;
HttpServletRequest request = (HttpServletRequest)
sessionContext.getServletRequest();
if (request != null) {
session.setHost(IpUtils.getIpAddr(request));
session.setUserAgent(request.getHeader("User-Agent"));
session.setSystemHost(request.getLocalAddr() + ":" +
request.getLocalPort());
}
}
return session;
}
}
```




