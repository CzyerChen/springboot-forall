### 三、核心配置解读&高级配置
#### SecurityConfig中的configure
- 身份认证很重要地一部分就是根据角色和权限，把控好所有页面需要或者不需要认证，是否能够查看
- 根据SecurityConfig中的configure方法进行研究
```text
 protected void configure(HttpSecurity http) throws Exception {
      http
          .authorizeRequests()
              .antMatchers("/", "/home").permitAll()
              .anyRequest().authenticated()
              .and()
          .formLogin()
              .loginPage("/login")
              .permitAll()
              .and()
          .logout()
              .permitAll();
  }
```
- 对于/ /home /login logout允许所有人访问，无需认证，/hello页面没有描述，标识需要通过身份认证
```text
防止CSRF攻击
    Session Fixation protection(防止别人篡改sessionId)
    
Security Header(添加一系列和Header相关的控制)
    HTTP Strict Transport Security for secure requests
    集成X-Content-Type-Options
    缓存控制
    集成X-XSS-Protection.aspx)
    X-Frame-Options integration to help prevent Clickjacking(iframe被默认禁止使用)

为Servlet API集成了如下的几个方法
    HttpServletRequest#getRemoteUser())
    HttpServletRequest.html#getUserPrincipal())
    HttpServletRequest.html#isUserInRole(java.lang.String))
    HttpServletRequest.html#login(java.lang.String, java.lang.String))
    HttpServletRequest.html#logout())
```
#### @EnableWebSecurity
- 安全认证主要就是通过这个注解来开启的
- 源码
```text
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ WebSecurityConfiguration.class,
		SpringWebMvcImportSelector.class })
@EnableGlobalAuthentication
@Configuration
public @interface EnableWebSecurity {

	/**
	 * Controls debugging support for Spring Security. Default is false.
	 * @return if true, enables debug support with Spring Security
	 */
	boolean debug() default false;
}

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import(AuthenticationConfiguration.class)
@Configuration
public @interface EnableGlobalAuthentication {
}

```
- 这个注解还需要引入WebSecurityConfiguration --web安全的配置
- SpringWebMvcImportSelector 判断当前的环境是否包含springmvc，做出对应的初始化，避免重复配置
- @EnableGlobalAuthentication 用于激活AuthenticationConfiguration配置
- 通过@EnableWebSecurity 可以加载WebSecurityConfiguration，AuthenticationConfiguration两个配置类，用于配置认证和配置web安全

1.WebSecurityConfiguration
通过外部引用这个web安全的配置，我们可以看到，依靠上这个类可以给Spring注入一个Security的核心过滤器
```text
/**
	 * Creates the Spring Security Filter Chain
	 * @return
	 * @throws Exception
	 */
	@Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
	public Filter springSecurityFilterChain() throws Exception {
		boolean hasConfigurers = webSecurityConfigurers != null
				&& !webSecurityConfigurers.isEmpty();
		if (!hasConfigurers) {
			WebSecurityConfigurerAdapter adapter = objectObjectPostProcessor
					.postProcess(new WebSecurityConfigurerAdapter() {
					});
			webSecurity.apply(adapter);
		}
		return webSecurity.build();
	}
```
- SpringSecurityFilterChain最终将请求移交给DelegatingFilterProxy代理类进行代理，Spring通过代理模式，将spring security 中的安全过滤，交给web包下的代理类进行请求拦截，很好地进行了解耦

2.AuthenticationConfiguration
- 通过@EnableGlobalAuthentication外部引用注入了AuthenticationConfiguration，AuthenticationConfiguration内部主要是为了构建AuthenticationManager，也是后期起到关键作用身份认证管理器
```text
@Configuration
@Import(ObjectPostProcessorConfiguration.class)
public class AuthenticationConfiguration {

	private AtomicBoolean buildingAuthenticationManager = new AtomicBoolean();

	private ApplicationContext applicationContext;

	private AuthenticationManager authenticationManager;

	private boolean authenticationManagerInitialized;

	private List<GlobalAuthenticationConfigurerAdapter> globalAuthConfigurers = Collections
			.emptyList();

	private ObjectPostProcessor<Object> objectPostProcessor;

	@Bean
	public AuthenticationManagerBuilder authenticationManagerBuilder(
			ObjectPostProcessor<Object> objectPostProcessor, ApplicationContext context) {
		LazyPasswordEncoder defaultPasswordEncoder = new LazyPasswordEncoder(context);
		AuthenticationEventPublisher authenticationEventPublisher = getBeanOrNull(context, AuthenticationEventPublisher.class);

		DefaultPasswordEncoderAuthenticationManagerBuilder result = new DefaultPasswordEncoderAuthenticationManagerBuilder(objectPostProcessor, defaultPasswordEncoder);
		if (authenticationEventPublisher != null) {
			result.authenticationEventPublisher(authenticationEventPublisher);
		}
		return result;
	}
```

3.WebSecurityConfigurerAdapter
- 我们在创建SecurityConfig的时候就是继承了一个抽象类WebSecurityConfigurerAdapter，采用了适配器的模式，能够将变与不变的属性分离，能够允许自己传入参数修改原有的配置
- WebSecurityConfigurerAdapter中提供的几个configure方法就是后续我们重写的
```text
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		this.disableLocalConfigureAuthenticationBldr = true;
	}
	
	
	protected void configure(HttpSecurity http) throws Exception {
    		logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
    
    		http
    			.authorizeRequests()
    				.anyRequest().authenticated()
    				.and()
    			.formLogin().and()
    			.httpBasic();
    	}
    	}
    	
    public void configure(WebSecurity web) throws Exception {
   	}
```
- 一个典型的configure(HttpSecurity http)配置
```text
protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()//配置路径拦截
                .antMatchers("/resources/**", "/signup", "/about").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated()
                .and()
            .formLogin()//表单提交相关
                .usernameParameter("username")
                .passwordParameter("password")
                .failureForwardUrl("/login?error")
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()//注销
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
                .permitAll()
                .and()
            .httpBasic()//可以配置basic登录
                .disable();
    }
```

4.WebSecurityBuilder
- 一个configure(WebSecurity web)的基础配置
```text
public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/resources/**");
    }
```

5.AuthenticationManagerBuilder
- 一个configure(AuthenticationManagerBuilder auth)的基本配置，这边可以内置安全认证用户，或者自定义UserDetailService，来提供和数据库交互的用户身份认证信息查询
- 新版本中密码需要注明加密方式
```text
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("admin").password("{noop}admin").roles("USER");
    }
}
```
- 对于身份认证的配置有两种，简单介绍以下它们的区别：一个全局，一个当前
```text
//重写当前WebSecurityConfigurerAdapter的方法，作用在当前@EnableWebSecurity标识的类的作用域下
 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("admin").password("{noop}admin").roles("USER");
    }
    
 
 //获取到全局的AM，作用域可以跨多个WebSecurityConfigurerAdapter
 @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{noop}pass")
                .roles("USER");
    }    
```
