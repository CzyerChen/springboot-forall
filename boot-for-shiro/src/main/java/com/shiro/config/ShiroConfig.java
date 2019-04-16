package com.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.shiro.filter.KickoutSessionControlFilter;
import com.shiro.repository.TUserRepository;
import com.shiro.security.SelfRealm;
import com.shiro.security.ShiroSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 16:30
 */
@Configuration
@Slf4j
public class ShiroConfig {


    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

    /**
     * 一个自定义的Realm进行身份认证
     *
     * @return
     */
    @Bean(name = "selfRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public SelfRealm selfRealm() {
   /*     SelfRealm selfRealm = new SelfRealm();
        selfRealm.setCredentialsMatcher();
        selfRealm.setCachingEnabled();  //默认为true
        selfRealm.setAuthorizationCachingEnabled(); //默认为true
        selfRealm.setAuthenticationCacheName();  //默认getClass().getName() + DEFAULT_AUTHORIZATION_CACHE_SUFFIX
        selfRealm.setAuthorizationCache();  //Cache<Object, AuthorizationInfo> authorizationCache
        selfRealm.setAuthorizationCacheName();//name + DEFAULT_AUTHORIZATION_CACHE_SUFFIX*/


        return new SelfRealm();
    }

    /*
    会话ID生成器
     */
    public JavaUuidSessionIdGenerator generator(){
        return  new JavaUuidSessionIdGenerator();
    }

    /**
     * 认证的入口，securityManager，并且把自定义的认证赋给它,安全管理器
     *
     * @return
     */
    @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(selfRealm());
        manager.setRememberMeManager(rememberMeManager());
        manager.setCacheManager(redisCacheManager());
        //manager.setCacheManager(ehCacheManager());
        manager.setSessionManager(sessionManager());
        return manager;
    }

  /*
    public SslFilter sslFilter(){
        SslFilter sslFilter = new SslFilter();
        sslFilter.setPort(8443);
        return sslFilter;
    }

    public FormAuthenticationFilter authenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        formAuthenticationFilter.setLoginUrl();
        formAuthenticationFilter.setUsernameParam();
        formAuthenticationFilter.setPasswordParam();
        formAuthenticationFilter.setRememberMeParam();
        return formAuthenticationFilter;
    }

  public KickoutSessionControlFilter kickoutSessionControlFilter(){
      KickoutSessionControlFilter kickout = new KickoutSessionControlFilter();
      kickout.setKickoutUrl();
      kickout.setKickoutAfter();
      kickout.setMaxSession();
      kickout.setCacheManager();
      kickout.setSessionManager();
      return kickout;
  }
 */

    /**
     * 过滤器，表示接口的拦截和跳转
     *
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shirFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

       /* Map<String,Filter> map = new HashMap<>();
        map.put("sslFilter",sslFilter());
        map.put("formAuthenticationFilter",authenticationFilter());
        map.put("kickoutSessionControlFilter",kickoutSessionControlFilter())
        shiroFilterFactoryBean.setFilters(map);*/


        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/", "anon");
        //filterChainDefinitionMap.put("/**", "authc");
        //user指的是用户认证通过或者配置了Remember Me记住用户登录状态后可访问,而不是每一次都需要认证
        filterChainDefinitionMap.put("/**", "user");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * cookie对象
     *
     * @return
     */
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //单位为秒
        cookie.setMaxAge(60);
      /*  cookie.setPath();
        cookie.setHttpOnly();
        cookie.setDomain();
        cookie.setName();
        cookie.setComment();
        .....
        */
        return cookie;
    }

    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //cookie加密密钥
        //这个key是为了给带缓存的cookie进行加密，避免安全问题
        //这个key可以自行生成
        cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }


    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    /**
     * spring mvc 相关的配置
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 缓存管理器
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        return redisManager;
    }

    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
        return ehCacheManager;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

   /*
   EhCache版本
   @Bean
    public SessionDAO sessionDAO() {
        MemorySessionDAO sessionDAO = new MemorySessionDAO();
        return sessionDAO;
    }*/

    /**
     * 会话DAO
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 会话验证调度器
     * @return
     */
    public ExecutorServiceSessionValidationScheduler scheduler(){
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(1800000);
        scheduler.setThreadNamePrefix("scheduler-shiro-%d");
        return  scheduler;
    }

    /**
     * 会话管理器
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        ((ArrayList<SessionListener>) listeners).add(new ShiroSessionListener());
        defaultWebSessionManager.setSessionListeners(listeners);
        defaultWebSessionManager.setSessionDAO(redisSessionDAO());

       /* defaultWebSessionManager.setSessionValidationScheduler();
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval();
        scheduler.setSessionManager();
        scheduler.setThreadNamePrefix();
        defaultWebSessionManager.setSessionValidationInterval();
        defaultWebSessionManager.setGlobalSessionTimeout();
        defaultWebSessionManager.setSessionValidationSchedulerEnabled();*/

/*
        QuartzSessionValidationScheduler  scheduler = new QuartzSessionValidationScheduler();
        scheduler.setScheduler();
        scheduler.setSessionValidationInterval();
        scheduler.setSessionManager();*/

      /*  SimpleCookie cookie = new SimpleCookie();
        cookie.setName("token");
        cookie.setMaxAge(1800);
        cookie.setHttpOnly(true);
        cookie.setDomain("test.com");
        cookie.setPath();
        defaultWebSessionManager.setSessionIdCookie(cookie);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);*/


        return defaultWebSessionManager;
    }


}
