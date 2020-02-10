/**
 * Author:   claire
 * Date:    2020-02-10 - 08:53
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-10 - 08:53          V1.3.1
 */
package com.mybatis.multidb.config.selfdefined;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.fibodt.cloud.falcon.pool.FBDataSource;
import com.mybatis.multidb.config.DynamicDataSource;
import com.mybatis.multidb.config.selfdefined.properties.FbDataSourceProperties;
import com.mybatis.multidb.enumeration.DBTypeEnum;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能简述 <br/>
 * 〈自定义的数据库包装类注入〉
 *
 * @author claire
 * @date 2020-02-10 - 08:53
 */
@EnableTransactionManagement
@Configuration
//enable一下需要的配置文件，以便可以在内部注入使用，enable的是能够允许通过ConfigurationProperties注入的配置类
@EnableConfigurationProperties({FbDatasourceStatProperties.class, DataSourceProperties.class, FbDataSourceProperties.class})
@MapperScan("com.mybatis.multidb.mapper.db*")
public class SelfMybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 功能描述: <br/>
     * 〈注入参数，并生成获取FBDatasource的MultiFbDatasource对象〉
     *
     * @return {@link com.mybatis.multidb.config.selfdefined.MultiFbDatasource}
     * @author claire
     * @date 2020-02-10 - 09:00
     */
    @Bean(name = "db1parent")
    @ConfigurationProperties(prefix = "spring.datasource.fibo.db1")
    public MultiFbDatasource multiFbDatasource1() {
        return FbDataSourceBuilder.create().build();
    }

    /**
     * 功能描述: <br/>
     *
     * @return {@link com.mybatis.multidb.config.selfdefined.MultiFbDatasource}
     * @author claire
     * @date 2020-02-10 - 09:00
     */
    @Bean(name = "db2parent")
    @ConfigurationProperties(prefix = "spring.datasource.fibo.db2")
    public MultiFbDatasource multiFbDatasource2() {
        return FbDataSourceBuilder.create().build();
    }

    /**
     * 功能描述: <br/>
     *
     * @return {@link com.mybatis.multidb.config.selfdefined.MultiFbDatasource}
     * @author claire
     * @date 2020-02-10 - 09:00
     */
    @Bean(name = "db3parent")
    @ConfigurationProperties(prefix = "spring.datasource.fibo.db3")
    public MultiFbDatasource multiFbDatasource3() {
        return FbDataSourceBuilder.create().build();
    }


    /**
     * 功能描述: <br/>
     * 〈由于参数注入的postpropossor,因而将带有参数的MultiFbDatasource 优先注入〉
     *
     * @param db1parent
     * @return {@link com.fibodt.cloud.falcon.pool.FBDataSource}
     * @author claire
     * @date 2020-02-10 - 08:55
     */
    @Bean(name = "db1")
    @ConditionalOnBean(name = "db1parent")
    public FBDataSource db1(@Qualifier("db1parent") MultiFbDatasource db1parent) {
        return db1parent.getInstance();
    }

    /**
     * 功能描述: <br/>
     *
     * @param db2parent
     * @return {@link com.fibodt.cloud.falcon.pool.FBDataSource}
     * @author claire
     * @date 2020-02-10 - 08:55
     */
    @Bean(name = "db2")
    @ConditionalOnBean(name = "db2parent")
    public FBDataSource db2(@Qualifier("db2parent") MultiFbDatasource db2parent) {
        return db2parent.getInstance();
    }


    /**
     * 功能描述: <br/>
     *
     * @param db3parent
     * @return {@link com.fibodt.cloud.falcon.pool.FBDataSource}
     * @author claire
     * @date 2020-02-10 - 08:55
     */
    @Bean(name = "db3")
    @ConditionalOnBean(name = "db3parent")
    public FBDataSource db3(@Qualifier("db3parent") MultiFbDatasource db3parent) {
        return db3parent.getInstance();
    }

    @Bean("multipleDataSource")
    @ConditionalOnBean(name = {"db1", "db2", "db3"})
    @Primary
    public DataSource multipleDataSource(@Qualifier("db1") FBDataSource db1,
                                         @Qualifier("db2") FBDataSource db2,
                                         @Qualifier("db3") FBDataSource db3) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.db1.getValue(), db1);
        targetDataSources.put(DBTypeEnum.db2.getValue(), db2);
        targetDataSources.put(DBTypeEnum.db3.getValue(), db3);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(db1);
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    @ConditionalOnBean(name = "multipleDataSource")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(db1(multiFbDatasource1()), db2(multiFbDatasource2()),db3(multiFbDatasource3())));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor()
        });
        return sqlSessionFactory.getObject();
    }
}
