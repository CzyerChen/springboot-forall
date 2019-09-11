package com.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mybatis.constant.DataSourceTypeEnum;
import com.mybatis.model.MapWrapperFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.mybatis.dao")
public class DynamicDataSourceConfig {

    @Bean(name = "dataSource1")
    @ConfigurationProperties(prefix = "datasource.primary")
    public DataSource dataSource1(){
       return  new DruidDataSource();
    }


    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "datasource.secondary")
    public DataSource dataSource2(){
        return  new DruidDataSource();
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(
            @Qualifier("dataSource1")DataSource dataSource1,
            @Qualifier("dataSource2")DataSource dataSource2){
        Map<Object,Object> map = new HashMap<Object, Object>();
        map.put(DataSourceTypeEnum.DB1,dataSource1);
        map.put(DataSourceTypeEnum.DB2,dataSource2);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.setDefaultTargetDataSource(dataSource1);

        return dynamicDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);

        return sqlSessionFactoryBean.getObject();
    }


    @Bean(name = "dataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dynamicDataSource") DynamicDataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(){
        return configuration -> configuration.setObjectWrapperFactory(new MapWrapperFactory());
    }
}
