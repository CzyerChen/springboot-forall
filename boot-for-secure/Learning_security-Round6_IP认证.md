> 跟随上面的学习，已经清晰地了解到了Spring Security的几个组成部分和认证流程，接下来跟随Xu jinfeng，也动手搭建以下手写的IP登录

### 六、实现IP限制过滤

### IpAuthenticationProcessingFilter
- 定义一个认证的入口
```text
public class IpAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
     public IpAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/ipVerify"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
         String host = request.getRemoteHost();
        System.out.println("ip:"+host);
        return getAuthenticationManager().authenticate(new IpAuthenticationToken(host));
    }
}

```
### IpAuthenticationToken
- 定义一个需要被认证的对象，本次尝试中仅仅对IP进行认证，那就只需要IP一个字段即可
```text
public class IpAuthenticationToken extends AbstractAuthenticationToken {
    private String ip;

    public IpAuthenticationToken(String ip){
        super(null);
        this.ip = ip;
        super.setAuthenticated(false);
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public IpAuthenticationToken(String ip,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.ip = ip;
        super.setAuthenticated(true);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.ip;
    }
}

```

### IpAuthenticationProvider
- 对IpAuthenticationToken进行认证，实现判断的核心部分
```text
public class IpAuthenticationProvider implements AuthenticationProvider {

    final static Map<String, SimpleGrantedAuthority> ipAuthorityMap = new ConcurrentHashMap<>();
    //维护一个ip白名单列表，每个ip对应一定的权限
    static {
        ipAuthorityMap.put("127.0.0.1", new SimpleGrantedAuthority("ADMIN"));
        ipAuthorityMap.put("172.30.59.75", new SimpleGrantedAuthority("ADMIN"));
        ipAuthorityMap.put("0:0:0:0:0:0:0:1",new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        IpAuthenticationToken ipAuthenticationToken = (IpAuthenticationToken)authentication;
        String ip = ipAuthenticationToken.getIp();
        SimpleGrantedAuthority simpleGrantedAuthority = ipAuthorityMap.get(ip);
        if(simpleGrantedAuthority != null){
            return new IpAuthenticationToken(ip, Collections.singletonList(simpleGrantedAuthority));
        }else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (IpAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

```
- 对于这个认证和匹配都可以自定义实现


### IpSecurityConfig
- 最后配置一个核心类，将IpAuthenticationProvider，LoginUrlAuthenticationEntryPoint注入
- IpAuthenticationProcessingFilter注入AM
- 配置configure

```text
@Configuration
@EnableWebSecurity
public class IpSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    IpAuthenticationProvider ipAuthenticationProvider(){
        return new IpAuthenticationProvider();
    }

    IpAuthenticationProcessingFilter ipAuthenticationProcessingFilter(AuthenticationManager authenticationManager){
        IpAuthenticationProcessingFilter ipAuthenticationProcessingFilter = new IpAuthenticationProcessingFilter();
        ipAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        ipAuthenticationProcessingFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/iplogin?error"));
        return ipAuthenticationProcessingFilter;
    }

    @Bean
    LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint(){
        LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/iplogin");
        return loginUrlAuthenticationEntryPoint;
    }

    @Override
    protected  void configure(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
                 .authorizeRequests()
                 .antMatchers("/","/home").permitAll()
                 .antMatchers("/iplogin").permitAll()
                 .anyRequest().authenticated()
                 .and()
                 .logout()
                 .permitAll()
                 .and()
                 .exceptionHandling()
                 .accessDeniedPage("/iplogin")
                 .authenticationEntryPoint(loginUrlAuthenticationEntryPoint());

          httpSecurity.addFilterBefore(ipAuthenticationProcessingFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ipAuthenticationProvider());
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{noop}pass")
                .roles("USER");
    }
```
