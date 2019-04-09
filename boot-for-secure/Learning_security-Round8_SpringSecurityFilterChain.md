> SpringSecurityFilterChain 作为 SpringSecurity 的核心过滤器链在整个认证授权过程中起着举足轻重的地位，每个请求到来，都会经过该过滤器链

> 之前也有通过解析SpringSecurityFilterChain到底有哪些作用的过滤器，并且讲述了各自的作用，这边就对几个关键类了解一下

### 配置过滤器
#### 1.java config 方式
```text
public class SecurityWebApplicationInitializer
	extends AbstractSecurityWebApplicationInitializer {

}
```

#### 2.xml方式
web.xml
```text
<filter>
	<filter-name>springSecurityFilterChain</filter-name>
	<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>

<filter-mapping>
	<filter-name>springSecurityFilterChain</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```
#### 3.springboot的自动配置
```text
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties
@ConditionalOnClass({ AbstractSecurityWebApplicationInitializer.class,
      SessionCreationPolicy.class })
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class SecurityFilterAutoConfiguration {

   private static final String DEFAULT_FILTER_NAME = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;//springSecurityFilterChain

    // <1>
   @Bean
   @ConditionalOnBean(name = DEFAULT_FILTER_NAME)
   public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration(
         SecurityProperties securityProperties) {
      DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean(
            DEFAULT_FILTER_NAME);
      registration.setOrder(securityProperties.getFilterOrder());
      registration.setDispatcherTypes(getDispatcherTypes(securityProperties));
      return registration;
   }

   @Bean
   @ConditionalOnMissingBean
   public SecurityProperties securityProperties() {
      return new SecurityProperties();
   }
}
```

### DelegatingFilterProxy - 委托过滤代理 
- 在org.springframework.web.filter包下，负责纵览web请求的过滤，自己并不负责执行过滤的详细过程
```text
protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
 		String targetBeanName = getTargetBeanName();
 		Assert.state(targetBeanName != null, "No target bean name set");
 		Filter delegate = wac.getBean(targetBeanName, Filter.class);
 		if (isTargetFilterLifecycle()) {
 			delegate.init(getFilterConfig());
 		}
 		return delegate;
 	}
```
- 通过跟踪源码，在Filter delegate = wac.getBean(targetBeanName, Filter.class);返回了FilterChainProxy,DelegatingFilterProxy在filter阶段只是init了很多FilterChainProxy，因而进一步的执行要看FilterChainProxy

### FilterChainProxy
- 在org.springframework.security.web;，负责执行过滤器的核心逻辑，保证安全和认证
```text
@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean clearContext = request.getAttribute(FILTER_APPLIED) == null;
		if (clearContext) {
			try {
				request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
				doFilterInternal(request, response, chain);
			}
			finally {
				SecurityContextHolder.clearContext();
				request.removeAttribute(FILTER_APPLIED);
			}
		}
		else {
			doFilterInternal(request, response, chain);
		}
	}

	private void doFilterInternal(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		FirewalledRequest fwRequest = firewall
				.getFirewalledRequest((HttpServletRequest) request);
		HttpServletResponse fwResponse = firewall
				.getFirewalledResponse((HttpServletResponse) response);

		List<Filter> filters = getFilters(fwRequest);

		if (filters == null || filters.size() == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug(UrlUtils.buildRequestUrl(fwRequest)
						+ (filters == null ? " has no matching filters"
								: " has an empty filter list"));
			}

			fwRequest.reset();

			chain.doFilter(fwRequest, fwResponse);

			return;
		}

		VirtualFilterChain vfc = new VirtualFilterChain(fwRequest, chain, filters);
		vfc.doFilter(fwRequest, fwResponse);
	}
```
- 在过滤阶段获取内部的所有过滤链，也就是程序启动打印日志的哪些过滤链，并按照顺序存放在List<Filter> filters里面
- 其中上面的list中的每一个filter就是SecurityFilterChain，它最终的matches方法就是匹配每一个过滤器的功能，这是最终实现过滤功能的地方

### SecurityFilterChain
- 通过以上追踪，发现过滤的执行通过DelegatingFilterProxy进入，初始化委托,生成FilterChainProxy，FilterChainProxy通过获取最终的Filter,作用到每一个SecurityFilterChain的matches方法上去执行
- 也能够很明白的看到，前面的两个类都写明了是代理，并不是真正执行的类，因而SpringSecurityFilterChain中最终执行的类就是继承SecurityFilterChain的类
```text
public final class DefaultSecurityFilterChain implements SecurityFilterChain {
	private static final Log logger = LogFactory.getLog(DefaultSecurityFilterChain.class);
	private final RequestMatcher requestMatcher;
	private final List<Filter> filters;

	public DefaultSecurityFilterChain(RequestMatcher requestMatcher, Filter... filters) {
		this(requestMatcher, Arrays.asList(filters));
	}

	public DefaultSecurityFilterChain(RequestMatcher requestMatcher, List<Filter> filters) {
		logger.info("Creating filter chain: " + requestMatcher + ", " + filters);
		this.requestMatcher = requestMatcher;
		this.filters = new ArrayList<>(filters);
	}

	public RequestMatcher getRequestMatcher() {
		return requestMatcher;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public boolean matches(HttpServletRequest request) {
		return requestMatcher.matches(request);
	}

	@Override
	public String toString() {
		return "[ " + requestMatcher + ", " + filters + "]";
	}
}
```

### SpringSecurityFilterChain注册的流程
- 看到了SpringSecurityFilterChain怎样最终调用每一个Filter的执行，可是SpringSecurityFilterChain又是怎么被注册进来的呢
- SecurityConfig -> WebSecurityConfigurerAdapter -> WebSecurityConfigurer<WebSecurity> -> WebSecurity -> 回写WebSecurityConfiguration
- WebSecurity里面有很多builder的方法注入了很多参数，通过performBuild 返回了一个参数完整的对象，里面就包括了ignoredRequests,securityFilterChainBuilders,httpFirewall
```text
@Override
	protected Filter performBuild() throws Exception {
		Assert.state(
				!securityFilterChainBuilders.isEmpty(),
				"At least one SecurityBuilder<? extends SecurityFilterChain> needs to be specified. Typically this done by adding a @Configuration that extends WebSecurityConfigurerAdapter. More advanced users can invoke "
						+ WebSecurity.class.getSimpleName()
						+ ".addSecurityFilterChainBuilder directly");
		int chainSize = ignoredRequests.size() + securityFilterChainBuilders.size();
		List<SecurityFilterChain> securityFilterChains = new ArrayList<>(
				chainSize);
		for (RequestMatcher ignoredRequest : ignoredRequests) {
			securityFilterChains.add(new DefaultSecurityFilterChain(ignoredRequest));
		}
		for (SecurityBuilder<? extends SecurityFilterChain> securityFilterChainBuilder : securityFilterChainBuilders) {
			securityFilterChains.add(securityFilterChainBuilder.build());
		}
		FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChains);
		if (httpFirewall != null) {
			filterChainProxy.setFirewall(httpFirewall);
		}
		filterChainProxy.afterPropertiesSet();

		Filter result = filterChainProxy;
		if (debugEnabled) {
			logger.warn("\n\n"
					+ "********************************************************************\n"
					+ "**********        Security debugging is enabled.       *************\n"
					+ "**********    This may include sensitive information.  *************\n"
					+ "**********      Do not use in a production system!     *************\n"
					+ "********************************************************************\n\n");
			result = new DebugFilter(filterChainProxy);
		}
		postBuildAction.run();
		return result;
	}

```
- 最终这个build的结果，在WebSecurityConfiguration中被注册为Bean
```text
@Bean(
        name = {"springSecurityFilterChain"}
    )
    public Filter springSecurityFilterChain() throws Exception {
        boolean hasConfigurers = this.webSecurityConfigurers != null && !this.webSecurityConfigurers.isEmpty();
        if (!hasConfigurers) {
            WebSecurityConfigurerAdapter adapter = (WebSecurityConfigurerAdapter)this.objectObjectPostProcessor.postProcess(new WebSecurityConfigurerAdapter() {
            });
            this.webSecurity.apply(adapter);
        }

        return (Filter)this.webSecurity.build();
    }
```