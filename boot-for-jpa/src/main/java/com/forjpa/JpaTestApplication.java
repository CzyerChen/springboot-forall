package com.forjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 25 19:40
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.forjpa.repository"})
public class JpaTestApplication {

    public static void main(String[] args){
        SpringApplication.run(JpaTestApplication.class,args);
    }
}
