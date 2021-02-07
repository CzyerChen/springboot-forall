package com.learning.config;

import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus的配置类：对mybatis只有增强，没有修改，保留mybatis的所有特性
 * @author: claire  on 2019-08-28 - 10:42
 **/
@Configuration
public class MybatisPlusConfig {

    /**
     * 这里可以扩展，比如使用配置文件来配置扫描Mapper的路径
     */
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
//        scannerConfigurer.setBasePackage("com.learning.dao");
//        return scannerConfigurer;
//    }
}
