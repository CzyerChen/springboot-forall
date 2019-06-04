### mybatis多数据源
- 多数据源代表了两个及以上的数据库链接，包括了两个SqlSessionFactory,如果涉及事务还有两个不同的TransactionManager

#### 第一种 简单多数据源 实现基于配置的多数据源的操作
- 最初始的版本就是乖乖的配置好两个数据源，然后根据不同的链接操作不同的数据，mapper的书写保持不变，只是在注入和扫描的时候绑定不同的结构
- @Primary配置的是主数据源，优先加载
- 第一步：定义配置文件：
```text
datasource:
  primary:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&serverTimezone=GMT%2b8
    username: test
    password: test
  secondary:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/person?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&serverTimezone=GMT%2b8
    username: test
    password: test
```
- 第二步：定义数据源DataSource
```text
 @Bean(name = "dataSource1")
    @Primary
    @ConfigurationProperties(prefix = "datasource.primary")
    public DataSource dataSource(){
        return new DruidDataSource();
    }
```
- 第三步：配置事务管理器 DataSourceTransactionManager
```text
@Bean(name = "dataSourceTransactionManager1")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource1")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
```

- 第四步：配置SqlSessionFactory
```text
@Bean(name = "sqlSessionFactory1")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource1")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }
```

- 第五步：配置SqlSessionTemplate
```text

    @Bean("sqlSessionTemplate1")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory1")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
```
- 第六步：像配置数据源1一样，配置数据源2
```text
@Configuration
public class DataSouce1Config {

    @Bean(name = "dataSource1")
    @Primary
    @ConfigurationProperties(prefix = "datasource.primary")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "dataSourceTransactionManager1")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource1")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactory1")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource1")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("sqlSessionTemplate1")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory1")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}


@Configuration
public class DatSource2Config {
    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "datasource.secondary")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "dataSourceTransactionManager2")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource2")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource2")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("sqlSessionTemplate2")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory2")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
```

- 第七步：加入Mapper扫描，绑定不同的事务管理器和session
```text
//配置在主类上面
@MapperScans(value = {
        @MapperScan(basePackages = "com.mybatis.dao.mapper1",
        sqlSessionFactoryRef = "sqlSessionFactory1",sqlSessionTemplateRef = "sqlSessionTemplate1"),
        @MapperScan(basePackages = "com.mybatis.dao.mapper2",
        sqlSessionFactoryRef = "sqlSessionFactory2",sqlSessionTemplateRef = "sqlSessionTemplate2")
})

//也可以将@MapperScan的标志拆开来，放在各自的DataSourceConfig上面来实现

```

- 第八步：简单的实现，简单的测试
```text
Person1
Person2
Person1Mapper
Person2Mapper


@SpringBootTest(classes = MybatisTestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class MybatisTest {

    @Autowired
    private Person1Mapper person1Mapper;
    @Autowired
    private Person2Mapper person2Mapper;

    @Test
    public void test1(){
        Person1 person1 = person1Mapper.findbyId(1);
        Person2 person2 = person2Mapper.findbyId(1);
        Assert.assertNotNull(person1);
        Assert.assertNotNull(person2);
    }
}
//通过测试
```


#### 第二种 动态数据源 灵活切换 基于AOP实现
