### 一、mybatis的日常操作
- 第一步：定义config.xml文件,最普通的写法，如果用到Spring的Bean管理，配置就可能不同
```text
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC" />
            <!-- 配置数据库连接信息 -->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver" />
                <property name="url" value="jdbc:mysql://localhost:3306/testdb" />
                <property name="username" value="test" />
                <property name="password" value="test" />
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="mapper/PersonMapper.xml"/>
    </mappers>

</configuration>
```
- 第二步：书写相应的数据库实体类Person1 Person1Mapper
- 第三步：书写对应的Mapper文件 mapper/PersonMapper.xml
```text
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mybatis.dao.mapper1.Person1Mapper">
    <select id="getUser" parameterType="int"
            resultType="com.mybatis.domain.Person1">
        select * from t_people where pid=#{id}
    </select>
</mapper>
```
- 第四步：通过SqlSessionFactoryBuilder 创建SqlSessionFactory,再通过SqlSessionFactory 打开一个SqlSession,最后进行Mapper的调用
```text

    @Test
    public void testSqlSession(){
        String file = "config.xml";
        InputStream asStream = this.getClass().getClassLoader().getResourceAsStream(file);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(asStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        Person1Mapper mapper = sqlSession.getMapper(Person1Mapper.class);
        Person1 user = mapper.getUser(1);
        Assert.assertNotNull(user);
    }
```

### 二、mybatis多数据源
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
- 动态数据源，可能需要应对一些临时的需求，能在应用层做到读写分离，即在程序代码中控制不同的查询方法去连接不同的库
- 除了这种方法以外，数据库中间件也是个不错的选择，它的优点是数据库集群对应用来说只暴露为单库，不需要切换数据源的代码逻辑

- 第一步：同样配置两种数据源（已省略mapper的config和xml配置的基本实现）
```text
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

```
- 第二步：实现一个动态数据源(这边使用了spring jdbc原生的，如果使用别的数据源，可以自行实现)
```text
public class DynamicDataSource extends AbstractRoutingDataSource {
    protected Object determineCurrentLookupKey() {
        return DatabaseContextHolder.getDbs();
    }
}
```
- 第三步：配置动态数据源，绑定提供的配置数据源
```text
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
```
- 第四步：创建原有的sqlSessionFactory 和事务管理器
```text
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
```
- 第五步：一个全局的Context ，记录当前数据源的情况
```text
public class DatabaseContextHolder {
    public static final DataSourceTypeEnum DEFAULT_DB= DataSourceTypeEnum.DB1;

    private static final ThreadLocal<DataSourceTypeEnum> dbs = new ThreadLocal<DataSourceTypeEnum>();

    public static void setDbs(DataSourceTypeEnum dataSourceTypeEnum){
        dbs.set(dataSourceTypeEnum);
    }

    public static DataSourceTypeEnum getDbs(){
        return dbs.get();
    }

    public static void clearDB() {
        dbs.remove();
    }
}
```
- 第六步：基于AOP来实现获取动态获取不同的数据源，先实现数据源注解
```text

public enum DataSourceTypeEnum {
    DB1,DB2
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DSType {

    public DataSourceTypeEnum value() default DataSourceTypeEnum.DB1;
}
```
- 第七步：AOP的具体实现，主要就是获取到注解的内容，判断数据源，然后指定为当前的数据源来使用
```text
@Aspect
@Component
public class DynamicDataSourceAspect {

    @Pointcut(value = "@annotation(com.mybatis.constant.DSType)")
    public void point(){

    }

    @Before(value = "point()")
    public void switchDB(JoinPoint joinPoint){
        //获取当前访问的class
        Class<?> aClass = joinPoint.getTarget().getClass();
        //获取访问的方法名
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        DataSourceTypeEnum db = DatabaseContextHolder.DEFAULT_DB;
        try{
            Method method = aClass.getMethod(methodName, parameterTypes);
            if(method.isAnnotationPresent(DSType.class)){
                //获取这个注解中的属性
                DSType annotation = method.getAnnotation(DSType.class);
                db = annotation.value();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //切换数据源
        DatabaseContextHolder.setDbs(db);

    }

    @After(value = "point()")
    public void afterDBChange(JoinPoint joinPoint){
        DatabaseContextHolder.clearDB();
    }
}
```

