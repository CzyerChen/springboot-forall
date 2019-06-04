package com.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan
public class MybatisTestApplication {

    public static void main(String[] args){
        SpringApplication.run(MybatisTestApplication.class,args);
    }
}
