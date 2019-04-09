> Druid 本来是为数据库监控而生的，不过因为它的侵入性，结果把应用的监控也做了

> 有首页，数据源，SQL监控，SQL防火墙，Web应用，URI监控，Session监控，Spring监控，JSON API

### 添加POM依赖
```text

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>
```

### 添加YML配置
```text
spring:
  # 数据源
  datasource:
    url: jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=utf8
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    platform: mysql
    initialSize: 5
    minIdle: 10
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 2000
    minEvictableIdleTimeMillis: 600000
    maxEvictableIdleTimeMillis: 900000
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    maxOpenPreparedStatements: 20
    asyncInit: true
    filters: stat,wall,log4j
    logSlowSql: true
    hibernate:
      show-sql: true
```

### 手动注入BEAN DruidConfig
```text
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidConfig {
    /**
     * 数据库地址
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 初始化连接数量
     */
    private int initialSize;

    /**
     * 最小闲置连接
     */
    private int minIdle;

    /**
     * 最大存活连接
     */
    private int maxActive;

    /**
     * 配置获取连接等待超时的时间
     */
    private long maxWait;

    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    private long timeBetweenEvictionRunsMillis;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private long minEvictableIdleTimeMillis;

    /**
     * 配置一个连接在池中最大生存的时间，单位是毫秒
     */
    private long maxEvictableIdleTimeMillis;

    /**
     *
     */
    private boolean testWhileIdle;

    /**
     *
     */
    private boolean testOnBorrow;

    /**
     *
     */
    private boolean testOnReturn;

    /**
     *
     */
    private boolean poolPreparedStatements;

    /**
     *
     */
    private int maxOpenPreparedStatements;

    /**
     *
     */
    private boolean asyncInit;


    @Bean
    public DruidDataSource druidDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        druidDataSource.setAsyncInit(asyncInit);
        return druidDataSource;
    }

....Getter/Setter
```

### 添加WebStatFilter
```text
@WebFilter(filterName = "DruidFilter", urlPatterns = "/*", initParams = {
        @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
})
public class DruidFilter extends WebStatFilter {
}
```

### 添加StatViewServlet
```text
@WebServlet(urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "allow", value = "127.0.0.1"),// IP白名单 (没有配置或者为空，则允许所有访问)
                @WebInitParam(name = "deny", value = "xxx.xxx.xx.xxx"),// IP黑名单 (存在共同时，deny优先于allow)
                @WebInitParam(name = "loginUsername", value = "admin"),// 用户名
                @WebInitParam(name = "loginPassword", value = "admin"),// 密码
                @WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的“Reset All”功能
        })
public class DruidServlet extends StatViewServlet {

    private static final long serialVersionUID = -6085007333934055609L;
}
```

### 添加Shiro非拦截
```text
  filterChainDefinitionMap.put("/druid/**", "anon");
```

### 主类开启Servlet包扫描
```text
@ServletComponentScan
```
