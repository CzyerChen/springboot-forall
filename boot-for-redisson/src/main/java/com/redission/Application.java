package com.redission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 19 17:38
 */
@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
