> shiro中加入缓存，可以缓存权限等数据库数据，可以缓存session等会话数据，可以做简单的访问次数限制等

> 缓存有内存缓存和分布式缓存，内存缓存是简单的使用，分布式缓存在现在分布式应用的情况下是必然趋势

> 目前很多场景下，这两种缓存都是共同使用的

## 减轻数据库压力
### Shiro - Redis
#### 1.添加依赖
```text
<dependency>
    <groupId>org.crazycake</groupId>
    <artifactId>shiro-redis</artifactId>
    <version>2.4.2.1-RELEASE</version>
</dependency>
```
#### 2.添加连接配置
```text
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    pool:
      max-active: 8
      max-wait: -1
      max-idle: 8
      min-idle: 0
    timeout: 0
  password: redis
```
#### 3.添加redisCacheManager配置
```text
public RedisManager redisManager() {
    RedisManager redisManager = new RedisManager();
    return redisManager;
}

public RedisCacheManager redisCacheManager() {
    RedisCacheManager redisCacheManager = new RedisCacheManager();
    redisCacheManager.setRedisManager(redisManager());
    return redisCacheManager;
}
securityManager.setCacheManager(redisCacheManager());

```

#### 4.配置完毕就可以通过缓存减少对数据库的访问了

### Shiro - EhCache
- 这个和redis非常的类似
- 依赖
```text
 <!-- shiro ehcache -->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-ehcache</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!-- ehchache -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <dependency>
            <groupId>net.sf.ehcache</groupId>
            <artifactId>ehcache</artifactId>
        </dependency>

```
- 配置文件 classpath:config/shiro-ehcache.sml
```text
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
         updateCheck="false">
    <diskStore path="java.io.tmpdir/Tmp_EhCache" />
    <defaultCache
            maxElementsInMemory="10000"
            eternal="false"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            overflowToDisk="false"
            diskPersistent="false"
            diskExpiryThreadIntervalSeconds="120" />

    <!-- 登录记录缓存锁定1小时 -->
    <cache
            name="passwordRetryCache"
            maxEntriesLocalHeap="2000"
            eternal="false"
            timeToIdleSeconds="3600"
            timeToLiveSeconds="0"
            overflowToDisk="false"
            statistics="true" />
</ehcache>
```
- 注入Bean
```text
  @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
        return ehCacheManager;
    }
    
    
      manager.setCacheManager(ehCacheManager());
```