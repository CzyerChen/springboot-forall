/**
 * Author:   claire
 * Date:    2020-02-08 - 13:55
 * Description: 数据库1连接
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 13:55          V1.3.1           数据库1连接
 */
package com.druiddb.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 功能简述 <br/> 
 * 〈数据库1连接〉
 *
 * @author claire
 * @date 2020-02-08 - 13:55
 * @since 1.3.1
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.db1")
@MapperScan(sqlSessionTemplateRef = DruidDb0Config.SQL_SESSION_TEMPLATE, basePackages = DruidDb0Config.BASE_PACKAGES)
@Slf4j
@Data
public class DruidDb0Config {
    private static final String DB_PREFIX = "spring.datasource";
    public static final String BASE_PACKAGES = "com.druiddb.mapper.db1";
    private static final String MAPPER_LOCATIONS = "classpath*:mappers/db1/*.xml";
    public static final String SQL_SESSION_TEMPLATE = "dB0SqlSessionTemplate";
    private static final String SQL_SESSION_FACTORY = "dB0SqlSessionFactory";
    private static final String PLATFORM_TRANSACTION_MANAGER = "dB0PlatformTransactionManager";
    private static final String DATA_SOURCE = "dB0DataSource";

    private String url;
    private String username;
    private String password;

    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("init olddb Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // IP白名单
        servletRegistrationBean.addInitParameter("allow", "");
        // IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "");
        servletRegistrationBean.addInitParameter("loginPassword", "");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean(name = "SqlSessionTemplate")
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "SqlSessionFactory")
    @Autowired
    public SqlSessionFactory sqlSessionFactory(@Qualifier(value = "DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS));
        return bean.getObject();
    }

    @Bean(name = "PlatformTransactionManager")
    @Autowired
    public PlatformTransactionManager platformTransactionManager(@Qualifier("DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }



    // 解决 spring.datasource.filters=stat,wall,log4j 无法正常注册进去
    @ConfigurationProperties(prefix = DB_PREFIX)
    class IDataSourceProperties {
        private String url = getUrl();
        private String username = getUsername();
        private String password = getPassword();
        private String driverClassName;
        private int initialSize;
        private int minIdle;
        private int maxActive;
        private int maxWait;
        private int timeBetweenEvictionRunsMillis;
        private int minEvictableIdleTimeMillis;
        private String validationQuery;
        private boolean testWhileIdle;
        private boolean testOnBorrow;
        private boolean testOnReturn;
        private boolean poolPreparedStatements;
        private int maxPoolPreparedStatementPerConnectionSize;
        private String filters;
        private String connectionProperties;

        @Bean(name = "DataSource")     //声明其为Bean实例
//        @Primary  //在同样的DataSource中，首先使用被标注的DataSource(分库后多数据源不需要配置)
        public DataSource dataSource() {
            DruidDataSource datasource = new DruidDataSource();
            datasource.setUrl(url);
            datasource.setUsername(username);
            datasource.setPassword(password);
            datasource.setDriverClassName(driverClassName);

            //configuration
            datasource.setInitialSize(initialSize);
            datasource.setMinIdle(minIdle);
            datasource.setMaxActive(maxActive);
            datasource.setMaxWait(maxWait);
            datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            datasource.setValidationQuery(validationQuery);
            datasource.setTestWhileIdle(testWhileIdle);
            datasource.setTestOnBorrow(testOnBorrow);
            datasource.setTestOnReturn(testOnReturn);
            datasource.setPoolPreparedStatements(poolPreparedStatements);
            datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
            try {
                datasource.setFilters(filters);
            } catch (SQLException e) {
                log.error("druid configuration initialization filter: " + e);
            }
            datasource.setConnectionProperties(connectionProperties);
            return datasource;
        }
    }
}
