package com.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
/*@MapperScans(value = {
        @MapperScan(basePackages = "com.mybatis.dao.mapper1",
        sqlSessionFactoryRef = "sqlSessionFactory1",sqlSessionTemplateRef = "sqlSessionTemplate1"),
        @MapperScan(basePackages = "com.mybatis.dao.mapper2",
        sqlSessionFactoryRef = "sqlSessionFactory2",sqlSessionTemplateRef = "sqlSessionTemplate2")
})*/
public class MybatisTestApplication {

    public static void main(String[] args){
        SpringApplication.run(MybatisTestApplication.class,args);
    }
}
