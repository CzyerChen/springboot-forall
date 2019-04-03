package com.secure.config;

import com.secure.repository.ReaderRepository;
import com.secure.repository.UserRepository;
import com.secure.service.ReaderSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 9:20
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*@Autowired
    private ReaderRepository readerRepository;
*/
    @Bean
    UserDetailsService customeUserService() {
        return new ReaderSerivce();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customeUserService()).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
  /*  @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .roles("USER");
    }
*/

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

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
               auth
                .userDetailsService(new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                       return  readerRepository.findByUsername(name).get();
                    }
                });

    }*/

}
