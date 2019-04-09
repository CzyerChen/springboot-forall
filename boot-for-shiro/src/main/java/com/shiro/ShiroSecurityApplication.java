package com.shiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 14:50
 */
@SpringBootApplication
@ServletComponentScan
@EntityScan(basePackages = "com.shiro")
@EnableJpaRepositories(basePackages = "com.shiro")
public class ShiroSecurityApplication {

    public static void main(String[] args){
        SpringApplication.run(ShiroSecurityApplication.class,args);
    }
}
