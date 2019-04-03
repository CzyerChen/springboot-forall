- 要实现访问控制的⽅法多种多样，可以通过Aop、拦截器实现，也可以通过框架实现（如：Apache Shiro、Spring Security
- 以下学习Spring Security ,如何简便的通过用户角色权限来控制页面的访问

[Spring 官方Security文档](https://docs.spring.io/spring-security/site/docs/4.1.0.RELEASE/reference/htmlsingle/)

[跟上大神脚步--徐靖峰](https://www.cnkirito.moe/categories/Spring-Security/)
> Spring Security -- 继承spring思想，依靠简单的配置，实现权限的管理

### 一、了解SpringSecurity的架构之组件
#### 1.SecurityContextHolder
```text
用于存储安全上下文（security context）的信息

当前用户，用户角色，相对应的角色权限，都保存在SecurityContextHolder中

SecurityContextHolder默认使用ThreadLocal 策略来存储认证信息，线程绑定和隐藏的内存泄漏问题

Spring Security在用户登录时自动绑定认证信息到当前线程，在用户退出时，自动清除当前线程的认证信息

```
1.1 获取当前用户信息
```text
public  void testMethod(){
        Object principal = SecurityContextHolder.getContext()
        .getAuthentication() //返回认证信息
        .getPrincipal();  //返回身份信息
        
        if(principal instanceof UserDetails){//UserDetails 是对身份信息的封装
            String username = ((UserDetails) principal).getUsername();
        }else {
            String s = principal.toString();
        }
        
    }
```
1.2 Authentication
- 源码
```text
public interface Authentication extends Principal, Serializable {
	
	Collection<? extends GrantedAuthority> getAuthorities();

	Object getCredentials();

	Object getDetails();

	
	Object getPrincipal();

	
	boolean isAuthenticated();

	void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}

1. 继承自Principal，能够获取用户的全部身份信息
2. 能够获取用户拥有的权限信息列表，密码，用户细节信息，用户身份信息，认证信息
3. getAuthorities()，权限信息列表
4. getCredentials()，密码信息，用户输入的密码字符串，在认证过后通常会被移除，用于保障安全
5. getDetails()，细节信息
6. getPrincipal()，最重要的身份信息，大部分情况下返回的是UserDetails接口的实现类，就是直接作用在DB存储上的，用户名密码等

```

#### 1.3 Spring Security 身份认证的过程
```text
                        1. 用户名+密码
                                |
                                |
                         2. 过滤器过滤
                                |
                                |
            3.封装成Authentication-->UsernamePasswordAuthenticationToken
                                |
                                |
            4.AuthenticationManager 身份管理器负责验证这个Authentication
                                |
                                |
       5.认证成功，AuthenticationManager返回一个Authentication实例
                                |
                                |
6.SecurityContextHolder安全上下文容器，通过SecurityContextHolder.getContext().setAuthentication(…)方法，
                    将Authentication实例设置进去
                 
```
- 以上流程，主要就是将存入DB的继承UserDetails的对象加载进来，通过认证生成认证实例，并交给安全容器

#### 1.4 AuthenticationManager
```text
AuthenticationManager（接口）是认证相关的核心接口，也是发起认证的出发点

ProviderManager 内部会维护一个List<AuthenticationProvider>列表，存放多种认证方式,来实现用户名登录，手机号码登录，刷脸登录等业务逻辑

整体采用设计模式中的委托者模式
```
- ProviderManager最核心的authenticate方法
```text

    private AuthenticationEventPublisher eventPublisher = new NullEventPublisher();
    //一个提供认证方法的列表
	private List<AuthenticationProvider> providers = Collections.emptyList();
	
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	private AuthenticationManager parent;
	private boolean eraseCredentialsAfterAuthentication = true;
	
public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Class<? extends Authentication> toTest = authentication.getClass();
		AuthenticationException lastException = null;
		Authentication result = null;
		boolean debug = logger.isDebugEnabled();

//对认证方法列表进行依次认证
		for (AuthenticationProvider provider : getProviders()) {
			if (!provider.supports(toTest)) {
				continue;
			}

			if (debug) {
				logger.debug("Authentication attempt using "
						+ provider.getClass().getName());
			}

			try {
				result = provider.authenticate(authentication);

				if (result != null) {
					copyDetails(authentication, result);
					break;
				}
			}
			catch (AccountStatusException e) {
				prepareException(e, authentication);
				// SEC-546: Avoid polling additional providers if auth failure is due to
				// invalid account status
				throw e;
			}
			catch (InternalAuthenticationServiceException e) {
				prepareException(e, authentication);
				throw e;
			}
			catch (AuthenticationException e) {
				lastException = e;
			}
		}

//如果认证列表没有匹配的，就委派给Manager来认证
		if (result == null && parent != null) {
			// Allow the parent to try.
			try {
				result = parent.authenticate(authentication);
			}
			catch (ProviderNotFoundException e) {
				// ignore as we will throw below if no other exception occurred prior to
				// calling parent and the parent
				// may throw ProviderNotFound even though a provider in the child already
				// handled the request
			}
			catch (AuthenticationException e) {
				lastException = e;
			}
		}

        //认证有结果
		if (result != null) {
		//移除密码
			if (eraseCredentialsAfterAuthentication
					&& (result instanceof CredentialsContainer)) {
				// Authentication is complete. Remove credentials and other secret data
				// from authentication
				((CredentialsContainer) result).eraseCredentials();
			}
        //事件发送
			eventPublisher.publishAuthenticationSuccess(result);
			return result;
		}
        
        //如果manager也不能处理认证，就抛出异常
        
		// Parent was null, or didn't authenticate (or throw an exception).
		if (lastException == null) {
			lastException = new ProviderNotFoundException(messages.getMessage(
					"ProviderManager.providerNotFound",
					new Object[] { toTest.getName() },
					"No AuthenticationProvider found for {0}"));
		}

		prepareException(lastException, authentication);

		throw lastException;
	}
```

#### 1.5 DaoAuthenticationProvider
- 对于最核心的校验部分，是拿出认证方法列表中的方法去认证，我们一般最常用就是通过数据库查询，查找是否有用户，并且用户加密后的密码和数据库中存储的是否一致
- 这个部分DAO层的查询，就依照普通的mybatis或者jpa等方法去查询，在service层进行业务逻辑的判断

1.6 UserDetails与UserDetailsService
- UserDetails用于标识一个存储用户名密码的接口，实现了序列化，有账户过期，账户被锁等默认方法
- UserDetailsService 用于标识一个可以处理身份认证的方法，默认有根据用户名的一个查询，loadUserByUsername
- UserDetailsService仅表示从数据库加载用户数据，AuthenticationProvider指从内存加载用户数据，这就是区别

