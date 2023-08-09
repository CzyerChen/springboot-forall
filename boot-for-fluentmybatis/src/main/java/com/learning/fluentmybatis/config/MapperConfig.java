/**
 * Author:   claire
 * Date:    2023/8/8 - 6:15 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/8 - 6:15 下午          V1.0.0
 */
package com.learning.fluentmybatis.config;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2023/8/8 - 6:15 下午
 * @since 1.0.0
 */
@Configuration
@MapperScan({"com.learning.fluentmybatis.mapper"})
public class MapperConfig {

    /**
     * 定义mybatis的SqlSessionFactoryBean
     *
     * @param dataSource
     * @return
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 获取所有mapper文件路径
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 多个
        bean.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));
        // 单个
//            bean.setMapperLocations(new Resource[]{new ClassPathResource("mapper/CustomMapper.xml")});
        return bean;
    }

    // 定义fluent mybatis的MapperFactory
    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }
}
