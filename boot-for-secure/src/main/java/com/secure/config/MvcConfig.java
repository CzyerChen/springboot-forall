package com.secure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 04 13:58
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter{

    @Override
    public  void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/ip").setViewName("ipview");
        registry.addViewController("/iplogin").setViewName("iplogin");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/login1").setViewName("login1");
        registry.addViewController("/index").setViewName("index");
    }
}
