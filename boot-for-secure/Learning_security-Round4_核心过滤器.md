### 四、SpringSecurity的核心过滤器
- 之前有分析到外部引用配置的WebSecurityConfiguration，注入了springSecurityFillterChian，最后通过代理交给Spring web最终实现拦截
- 启动项目查看类加载过程：
```text
Creating filter chain: o.s.s.web.util.matcher.AnyRequestMatcher@1,                        ------springSecurityFillterChian
[o.s.s.web.context.SecurityContextPersistenceFilter@8851ce1,                              ------SecurityContextPersistenceFilter 
o.s.s.web.header.HeaderWriterFilter@6a472566,                                             ------HeaderWriterFilter
o.s.s.web.csrf.CsrfFilter@61cd1c71,                                                       ------CsrfFilter
o.s.s.web.authentication.logout.LogoutFilter@5e1d03d7,                                    ------LogoutFilter
o.s.s.web.authentication.UsernamePasswordAuthenticationFilter@122d6c22,                   ------UsernamePasswordAuthenticationFilter
o.s.s.web.savedrequest.RequestCacheAwareFilter@5ef6fd7f,                                  ------RequestCacheAwareFilter
o.s.s.web.servletapi.SecurityContextHolderAwareRequestFilter@4beaf6bd,                    ------SecurityContextHolderAwareRequestFilter
o.s.s.web.authentication.AnonymousAuthenticationFilter@6edcad64,                          ------AnonymousAuthenticationFilter
o.s.s.web.session.SessionManagementFilter@5e65afb6,                                       ------SessionManagementFilter
o.s.s.web.access.ExceptionTranslationFilter@5b9396d3,                                     ------ExceptionTranslationFilter
o.s.s.web.access.intercept.FilterSecurityInterceptor@3c5dbdf8                             ------FilterSecurityInterceptor
]



1.SecurityContextPersistenceFilter 第一个过滤器，请求来临时，创建SecurityContext安全上下文信息，请求结束时清空SecurityContextHolder

2.HeaderWriterFilter 能够从web header中看到，这是与http头信息相关的类，给http响应添加一些Header,比如X-Frame-Options, X-XSS-Protection*，X-Content-Type-Options

3.CsrfFilter 为了放置Csrf攻击，默认开启

4.LogoutFilter 处理注销的过滤器

5.UsernamePasswordAuthenticationFilter  表单提交了username和password，被封装成token进行一系列的认证 时表单提交数据验证的关键类

6.RequestCacheAwareFilter  用于缓存request请求

7.SecurityContextHolderAwareRequestFilter  对ServletRequest进行了一次包装，使得request具有更加丰富的API

8.AnonymousAuthenticationFilter 匿名访问的身份过滤器，由于配置中有对部分页面不需要做身份认证的，那就需要这一套匿名的认证流程

9.SessionManagementFilter 和session相关的过滤器，内部维护了一个SessionAuthenticationStrategy

10.ExceptionTranslationFilter 将认证过程中出现的异常交给内部维护的一些类去处理，之前也有看到过源码，对于一些内部一场，会经过封装之后抛出来

11.FilterSecurityInterceptor  决定了访问特定路径应该具备的权限

```

1.SecurityContextPersistenceFilter
- 作为第一个过滤器，会初始化安全上下文和结束请求一些清理工作，是非常重要的
- 用户在登录过一次之后，后续的访问便是通过sessionId来识别，从而认为用户已经被认证，这个session的信息就会放在SecurityContextHolder中
- 因为当前请求http的无状态性，可以通过setAllowSessionCreation(false) 实现
- 源码
```text
public class SecurityContextPersistenceFilter extends GenericFilterBean {

	static final String FILTER_APPLIED = "__spring_security_scpf_applied";
    //安全上下文仓库
	private SecurityContextRepository repo;
    //是否允许存在上下文
	private boolean forceEagerSessionCreation = false;

  //注入一个基本的SecurityContextRepository，HttpSessionSecurityContextRepository是一个实现类，用于存储HttpSession
	public SecurityContextPersistenceFilter() {
		this(new HttpSessionSecurityContextRepository());
	}

   //手动注入一个自定义的仓库
	public SecurityContextPersistenceFilter(SecurityContextRepository repo) {
		this.repo = repo;
	}

   //过滤方法
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (request.getAttribute(FILTER_APPLIED) != null) {
			// ensure that filter is only applied once per request
			chain.doFilter(request, response);
			return;
		}

		final boolean debug = logger.isDebugEnabled();

		request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

		if (forceEagerSessionCreation) {//判断是否允许session,允许就获取请求中的session信息
			HttpSession session = request.getSession();

			if (debug && session.isNew()) {
				logger.debug("Eagerly created session: " + session.getId());
			}
		}
        
        //封装请求和响应
		HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request,
				response);
		//加载安全上下文
		SecurityContext contextBeforeChainExecution = repo.loadContext(holder);

		try {
		//设置安全上下文
			SecurityContextHolder.setContext(contextBeforeChainExecution);

			chain.doFilter(holder.getRequest(), holder.getResponse());

		}
		finally {
			SecurityContext contextAfterChainExecution = SecurityContextHolder
					.getContext();
			// Crucial removal of SecurityContextHolder contents - do this before anything
			// else.
			//请求安全上下文
			SecurityContextHolder.clearContext();
			repo.saveContext(contextAfterChainExecution, holder.getRequest(),
					holder.getResponse());
			request.removeAttribute(FILTER_APPLIED);

			if (debug) {
				logger.debug("SecurityContextHolder now cleared, as request processing completed");
			}
		}
	}

	public void setForceEagerSessionCreation(boolean forceEagerSessionCreation) {
		this.forceEagerSessionCreation = forceEagerSessionCreation;
	}
}
```
- 利用职责分离模式，将上下文的设置和清除解决，将上下文的存储和获取交给安全上下文仓库来处理


2.HttpSessionSecurityContextRepository
- 刚才上面提到了Filter是进行设置和清除，而repo负责存储和获取，现在就来看一下这个HttpSessionSecurityContextRepository
- 它主要有几个方法，在Filter调用也有看到，需要loadContext. saveContext.内部还会需要判断contains,getnerateNewContext等
```text
public class HttpSessionSecurityContextRepository implements SecurityContextRepository {
	//y一个security key会保存在session中
	public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
	protected final Log logger = LogFactory.getLog(this.getClass());

    //创建一个空的上下文
	private final Object contextObject = SecurityContextHolder.createEmptyContext();
	private boolean allowSessionCreation = true;//允许session,如果不被允许就将它设置为空
	private boolean disableUrlRewriting = false;//不允许url重写
	private boolean isServlet3 = ClassUtils.hasMethod(ServletRequest.class, "startAsync");
	private String springSecurityContextKey = SPRING_SECURITY_CONTEXT_KEY;

	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

//加载上下文  HttpRequestResponseHolder是经过封装的http请求和响应
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest request = requestResponseHolder.getRequest();
		HttpServletResponse response = requestResponseHolder.getResponse();
		//获取session
		HttpSession httpSession = request.getSession(false);
        //从session获取安全上下文
		SecurityContext context = readSecurityContextFromSession(httpSession);
        //请求中无
		if (context == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No SecurityContext was available from the HttpSession: "
						+ httpSession + ". " + "A new one will be created.");
			}
			//创建一个新的
			context = generateNewContext();

		}
        //将session写入response，封装返回信息
		SaveToSessionResponseWrapper wrappedResponse = new SaveToSessionResponseWrapper(
				response, request, httpSession != null, context);
		requestResponseHolder.setResponse(wrappedResponse);

		if (isServlet3) {
			requestResponseHolder.setRequest(new Servlet3SaveToSessionRequestWrapper(
					request, wrappedResponse));
		}

		return context;
	}

    //保存上下文
	public void saveContext(SecurityContext context, HttpServletRequest request,
			HttpServletResponse response) {
		SaveContextOnUpdateOrErrorResponseWrapper responseWrapper = WebUtils
				.getNativeResponse(response,
						SaveContextOnUpdateOrErrorResponseWrapper.class);
		if (responseWrapper == null) {
			throw new IllegalStateException(
					"Cannot invoke saveContext on response "
							+ response
							+ ". You must use the HttpRequestResponseHolder.response after invoking loadContext");
		}
		// saveContext() might already be called by the response wrapper
		// if something in the chain called sendError() or sendRedirect(). This ensures we
		// only call it
		// once per request.
		if (!responseWrapper.isContextSaved()) {
			responseWrapper.saveContext(context);
		}
	}

    //判断是否存在session
	public boolean containsContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return false;
		}

		return session.getAttribute(springSecurityContextKey) != null;
	}

	// 获取session ，httpSession the session obtained from the request.
	private SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
		final boolean debug = logger.isDebugEnabled();

		if (httpSession == null) {
			if (debug) {
				logger.debug("No HttpSession currently exists");
			}

			return null;
		}

		// Session exists, so try to obtain a context from it.
		Object contextFromSession = httpSession.getAttribute(springSecurityContextKey);

		if (contextFromSession == null) {
			if (debug) {
				logger.debug("HttpSession returned null object for SPRING_SECURITY_CONTEXT");
			}

			return null;
		}

		// We now have the security context object from the session.
		if (!(contextFromSession instanceof SecurityContext)) {
			if (logger.isWarnEnabled()) {
				logger.warn(springSecurityContextKey
						+ " did not contain a SecurityContext but contained: '"
						+ contextFromSession
						+ "'; are you improperly modifying the HttpSession directly "
						+ "(you should always use SecurityContextHolder) or using the HttpSession attribute "
						+ "reserved for this class?");
			}

			return null;
		}

		if (debug) {
			logger.debug("Obtained a valid SecurityContext from "
					+ springSecurityContextKey + ": '" + contextFromSession + "'");
		}

		// Everything OK. The only non-null return from this method.

		return (SecurityContext) contextFromSession;
	}

   //初始化一个新的session
	protected SecurityContext generateNewContext() {
		return SecurityContextHolder.createEmptyContext();
	}

	//默认为true,设置为false表示对于session为空的请求，不会创建默认session去存储上下文信息，但是请求自带session则还是会被存储
	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}
	public void setDisableUrlRewriting(boolean disableUrlRewriting) {
		this.disableUrlRewriting = disableUrlRewriting;
	}

	public void setSpringSecurityContextKey(String springSecurityContextKey) {
		Assert.hasText(springSecurityContextKey,
				"springSecurityContextKey cannot be empty");
		this.springSecurityContextKey = springSecurityContextKey;
	}

	//......Inner Class
	public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
		Assert.notNull(trustResolver, "trustResolver cannot be null");
		this.trustResolver = trustResolver;
	}
}
```
- 以上思路还是比较清晰的，HttpSessionSecurityContextRepository SecurityContextPersistenceFilter两个结合，完成了上下文的初始化，存储和后续清除的工作

3.UsernamePasswordAuthenticationFilter
- 这是一个身份认证非常重要的类，通过加载的安全上下文，登陆就需要使用用户名密码进行身份的校验，以及权限的控制
- 源码 这个实现类的方法非常简单
```text
public class UsernamePasswordAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {
	
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
	private boolean postOnly = true;

	public UsernamePasswordAuthenticationFilter() { //构造方法， login的POST方法，提交表单
		super(new AntPathRequestMatcher("/login", "POST"));
	}


	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {//如果表单提交不是POST方法，则会提示异常
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
        //从请求中获取表单的用户名密码
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();
        //封装一个token
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);
		setDetails(request, authRequest);
        //交给AM认证用户名密码的token是否正确
		return this.getAuthenticationManager().authenticate(authRequest);
	}	
	protected void setDetails(HttpServletRequest request,
			UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

//可以不叫username
	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

//可以不叫password
	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}
//设置是否一定要是POST方法
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getUsernameParameter() {
		return usernameParameter;
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}
}
```
- 方法非常简单，最重要就是attemptAuthentication，最终return this.getAuthenticationManager().authenticate(authRequest)就是让AM对当前的token进行认证
- 当前类继承自AbstractAuthenticationProcessingFilter，内部包含有AM 成功验证的handler和失败认证的handler，以及对于session的一些处理

4.AnonymousAuthenticationFilter 
- 匿名认证的类，在一些permitAll的配置中发挥作用，为了与有身份的认证流程保持一致，对于没有访问限制的访问其实是设置了匿名的用户
- 这个类的执行顺序比较靠后，因而它会在确认全局没有为该请求配置用户和权限的情况下，才会去配置匿名的身份认证

- AnonymousAuthenticationFilter这个类的内部也相对比较简单一些
- 设置一个匿名用户anonymousUser
- 授予角色ROLE_ANONYMOUS
- 封装tokenAnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key,principal, authorities);
- 设置Detail auth.setDetails(authenticationDetailsSource.buildDetails(request));
```text
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			SecurityContextHolder.getContext().setAuthentication(
					createAuthentication((HttpServletRequest) req));

			if (logger.isDebugEnabled()) {
				logger.debug("Populated SecurityContextHolder with anonymous token: '"
						+ SecurityContextHolder.getContext().getAuthentication() + "'");
			}
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("SecurityContextHolder not populated with anonymous token, as it already contained: '"
						+ SecurityContextHolder.getContext().getAuthentication() + "'");
			}
		}

		chain.doFilter(req, res);
	}

	protected Authentication createAuthentication(HttpServletRequest request) {
		AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key,
				principal, authorities);
		auth.setDetails(authenticationDetailsSource.buildDetails(request));

		return auth;
	}

```

5.ExceptionTranslationFilter
- 这个类用于封装身份认证中的全局异常，在过滤链的最后，会转换整个链路中的异常
- 异常主要分类两大类：AccessDeniedException访问异常和AuthenticationException认证异常
- 滤器检测到AuthenticationException，则将会交给内部的AuthenticationEntryPoint去处理
- 如果检测到AccessDeniedException，需要先判断当前用户是不是匿名用户
- 如果是匿名访问，则和前面一样运行AuthenticationEntryPoint
- 否则会委托给AccessDeniedHandler去处理，而AccessDeniedHandler的默认实现，是AccessDeniedHandlerImpl
- 源码
```text
public class ExceptionTranslationFilter extends GenericFilterBean {
    //一些默认注入的对象，处理AccessDenied，AuthenticationEntryPoint
    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();
	private AuthenticationEntryPoint authenticationEntryPoint;
	private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	private RequestCache requestCache = new HttpSessionRequestCache();

	private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	//这个filter主要是做AuthenticationException，AccessDeniedException的区分
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    			throws IOException, ServletException {
    		HttpServletRequest request = (HttpServletRequest) req;
    		HttpServletResponse response = (HttpServletResponse) res;
    
    		try {
    			chain.doFilter(request, response);
    
    			logger.debug("Chain processed normally");
    		}
    		catch (IOException ex) {
    			throw ex;
    		}
    		catch (Exception ex) {
    			// Try to extract a SpringSecurityException from the stacktrace
    			Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
    			RuntimeException ase = (AuthenticationException) throwableAnalyzer
    					.getFirstThrowableOfType(AuthenticationException.class, causeChain);
    
    			if (ase == null) {
    				ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(
    						AccessDeniedException.class, causeChain);
    			}
    
    			if (ase != null) {
    				handleSpringSecurityException(request, response, chain, ase);
    			}
    			else {
    				// Rethrow ServletExceptions and RuntimeExceptions as-is
    				if (ex instanceof ServletException) {
    					throw (ServletException) ex;
    				}
    				else if (ex instanceof RuntimeException) {
    					throw (RuntimeException) ex;
    				}
    
    				// Wrap other Exceptions. This shouldn't actually happen
    				// as we've already covered all the possibilities for doFilter
    				throw new RuntimeException(ex);
    			}
    		}
    	}
    	
    	//主要处理异常的方法
    	private void handleSpringSecurityException(HttpServletRequest request,
        			HttpServletResponse response, FilterChain chain, RuntimeException exception)
        			throws IOException, ServletException {
        		if (exception instanceof AuthenticationException) {//认证异常
        			logger.debug(
        					"Authentication exception occurred; redirecting to authentication entry point",
        					exception);
                    //重新到登陆端点
        			sendStartAuthentication(request, response, chain,
        					(AuthenticationException) exception);
        		}
        		//如果是访问受限
        		else if (exception instanceof AccessDeniedException) {
        			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        			if (authenticationTrustResolver.isAnonymous(authentication) || authenticationTrustResolver.isRememberMe(authentication)) {
        				logger.debug(
        						"Access is denied (user is " + (authenticationTrustResolver.isAnonymous(authentication) ? "anonymous" : "not fully authenticated") + "); redirecting to authentication entry point",
        						exception);
                        //重新到登陆端点，进行匿名访问
        				sendStartAuthentication(
        						request,
        						response,
        						chain,
        						new InsufficientAuthenticationException(
        							messages.getMessage(
        								"ExceptionTranslationFilter.insufficientAuthentication",
        								"Full authentication is required to access this resource")));
        			}
        			else {//如果不允许匿名，则抛出异常
        				logger.debug(
        						"Access is denied (user is not anonymous); delegating to AccessDeniedHandler",
        						exception);
                        
        				accessDeniedHandler.handle(request, response,
        						(AccessDeniedException) exception);
        			}
        		}
        	}
```
- 请求不允许匿名又登陆失败的，抛出异常
```text
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		if (!response.isCommitted()) {
			if (errorPage != null) {
				// Put exception into request scope (perhaps of use to a view)
				request.setAttribute(WebAttributes.ACCESS_DENIED_403,
						accessDeniedException);

				// Set the 403 status code.
				response.setStatus(HttpStatus.FORBIDDEN.value());

				// forward to error page.
				RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
				dispatcher.forward(request, response);
			}
			else {
				response.sendError(HttpStatus.FORBIDDEN.value(),
					HttpStatus.FORBIDDEN.getReasonPhrase());
			}
		}
	}
	}
```
- 封装以下HTTP响应码，然后转到错误页，如果没有错误页就直接给出文字响应
- 关于SecurityConfig我们配置configure他是怎么做到登陆失败直接帮助跳转页面的？
- 顺着formLogin返回的FormLoginConfigurer往下找
- 最终在FormLoginConfigurer的父类AbstractAuthenticationFilterConfigurer中LoginUrlAuthenticationEntryPoint，AuthenticationFailureHandler ，一个用于处理正常的验证失败，一个用于处理验证过程中异常


6.FilterSecurityInterceptor
- 过滤链中最后一环，用于对上述加载认证的权限进行访问控制
- 它的控制是完全基于上面的认证和配置的，正常情况下也不需要修改
- 记录请求信息：public FilterInvocation(String contextPath, String servletPath, String pathInfo,String query, String method) 




