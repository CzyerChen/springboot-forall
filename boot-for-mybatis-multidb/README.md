## springboot 结合 druid mybatis 实现多数据源接入
### 版本一：传统版 基于druid-spring-boot-start 的注入和自动配置
1.配置文件
```yaml
spring:
  datasource:
    druid:
      db1:
        url: jdbc:mysql://connect1?characterEncoding=utf-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useSSL=false
        username: user1
        password: pass1
        driver-class-name: com.mysql.jdbc.Driver
        initialSize: 5
        minIdle: 5
        maxActive: 20
      db2:
        url: jdbc:mysql://connect2?characterEncoding=utf-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useSSL=false
        username: user2
        password: pass2
        driver-class-name: com.mysql.jdbc.Driver
        initialSize: 5
        minIdle: 5
        maxActive: 20
      d3:
        url: jdbc:mysql://connect3?characterEncoding=utf-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useSSL=false
        username: user3
        password: pass3
        driver-class-name: com.mysql.jdbc.Driver
        initialSize: 5
        minIdle: 5
        maxActive: 20
  aop:
    proxy-target-class: true
    auto: true
```
2.配置类
- 由于基于druid的自动配置，所以配置项非常简单
```java
@EnableTransactionManagement
@Configuration
@MapperScan("com.mybatis.multidb.mapper.db*")
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 功能描述: <br/>
     * 〈注入数据库1〉
     *
     * @return {@link javax.sql.DataSource}
     * @author claire
     * @date 2020-02-10 - 09:05
     */
    @Bean(name = "db1")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db1")
    public DataSource db1() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * 功能描述: <br/>
     * 〈注入数据库2〉
     *
     * @return {@link javax.sql.DataSource}
     * @author claire
     * @date 2020-02-10 - 09:05
     */
    @Bean(name = "db2")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db2")
    public DataSource db2() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * 功能描述: <br/>
     * 〈注入数据库3〉
     *
     * @return {@link javax.sql.DataSource}
     * @author claire
     * @date 2020-02-10 - 09:05
     */
    @Bean(name = "db3")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db3")
    public DataSource db3() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("db1") DataSource db1,
                                         @Qualifier("db2") DataSource db2,
                                         @Qualifier("db3") DataSource db3) {
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
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(db1(), db2(),db3()));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //PerformanceInterceptor(),OptimisticLockerInterceptor()
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor()
        });
//        sqlSessionFactory.setGlobalConfig(globalConfiguration());
        return sqlSessionFactory.getObject();
    }

 /*   @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("-1");
        conf.setLogicNotDeleteValue("1");
        conf.setIdType(0);
        conf.setMetaObjectHandler(new MyMetaObjectHandler());
        conf.setDbColumnUnderline(true);
        conf.setRefresh(true);
        return conf;
    }*/
}
```
### 版本二 自己有封装的数据库 兼容传统配置和管理方式
- 涉及DatasourceBuilder DatasourceWrapper Datasource的改造
```java
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
```