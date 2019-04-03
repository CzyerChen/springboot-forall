package com.swaggerui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 05 16:17
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.swaggerui"})
public class SwaggerAppMain {
    public static void main(String[] args){
        SpringApplication.run(SwaggerAppMain.class,args);
    }
}
