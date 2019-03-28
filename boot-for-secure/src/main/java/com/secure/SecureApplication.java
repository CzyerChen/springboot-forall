package com.secure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 9:20
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.secure.repository"})
public class SecureApplication {

    public static void main(String[] args){
        SpringApplication.run(SecureApplication.class,args);
    }
}
