> 之前有提到缓存可以减轻数据库的访问，那缓存也可以在存储会话这种典型的临时数据

> session不仅仅是能够缓存会话，还能够实现一些在线人数统计、剔除人之类的操作

### sessionDAO的创建
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
