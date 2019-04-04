二、搭建Security认证
- 需要基础的登陆页面 --- thymeleaf
- 需要基本的接口请求 --- web
- 需要基本的身份认证 --- security

1.引入依赖
```text
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
</dependencies>
```

2.最核心的是一个Security的配置类
- 内置一个默认用户，可以启动放入内存供认证使用
- 也支持从数据库读取用户名密码加载到内存使用，这边的customeUserService就是通过实现UserDetailService实现的
- 测试过程中，由于SpringSecurit的版本升级，需要在password处指定一个加密的方案，才可以通过认证
```text
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    UserDetailsService customeUserService() {
        return new ReaderSerivce();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customeUserService()).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .anyRequest().authenticated()//所有请求需要登陆后访问
                    .antMatchers("/","/home")
                    .permitAll()
                    .antMatchers("/hello")
                    .hasRole("READER")
                    .and()
                .formLogin()
                .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .permitAll() //登陆页面任何人都可以访问
                    .and()
                .logout()
                    .permitAll();

    }

    /**
     * 内置一个默认用户，Spring Security5 需要在password的地方指定加密方式
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{noop}pass")
                .roles("USER");
    }
}
```

3.其他的就是需要一个首页页面，可以有所有权限，登陆页面需要有所有权限，hello页面是通过认证的跳转页面，简单地模拟登陆场景


```text
http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                //设置默认登录成功跳转页面
                .defaultSuccessUrl("/index").failureUrl("/login?error").permitAll()
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                //设置cookie的私钥
                .key("")
                .and()
                .logout()
                //默认注销行为为logout，可以通过下面的方式来修改
                .logoutUrl("/logout")
                //设置注销成功后跳转页面，默认是跳转到登录页面
                .logoutSuccessUrl("")
                .permitAll();
```
