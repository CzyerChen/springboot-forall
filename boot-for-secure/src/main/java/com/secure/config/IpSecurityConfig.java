package com.secure.config;

import com.secure.domain.IpAuthenticationProcessingFilter;
import com.secure.domain.IpAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 04 13:41
 */
/*@Configuration
@EnableWebSecurity*/
public class IpSecurityConfig extends WebSecurityConfigurerAdapter {
/*
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
    }*/
}
