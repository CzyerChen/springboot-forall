Mybatis作为在东亚开发者市场上占有绝对的使用优势，在中国大陆上讨论Mybatis优化的项目也是挺活跃。

局限于原始Mybatis繁琐的流程，**自动代码生成、声明式SQL、动态SQL，以及诸多细节的内容：多租户、多数据源、数据脱敏、SQL审计、多表查询、多表join、自动化主键生成**等等，推动项目开发降本增效，降低项目开发门槛，远离付费，哈哈。

产生了多个增强版工具：
-  TkMybatis（2014）
- MybatisPlus（2016）
- FluentMybatis（2020）
- MybatisFlex（2023）
- 等

目前人气【MybatisPlus】还是最高的。

所以，现在的你在用什么框架呢？
# 今天看看阿里推进的FluentMybatis
2021年，【阿里官宣...新一代ORM框架...】吸引了注意，
目前看1.3K的star来看，受欢迎程度不如后起之秀。

## 那么，可以尝试吗？优化有哪些？
想必这是每一项新技术出来，普通大众都会问的问题。
每一项技术的出现都是为了解决某一方面的问题，或者处于某一角度的洞悉，看看这个产品的出发点和落脚点是什么？

### 出发点
Mybatis在东亚地区开发者市场近些年具有不可撼动的地位，并占中国JavaORM之首，短期内肯定占有较大的市场份额，有使用者那么就有市场！

### 落脚点
考虑到，当基于Mybatis的功能丰富到了一定的程度，会更加注重敏捷、高效、降低代码入侵性。
因此集各家所长，诞生了FluentMybatis

### 它有哪些特点？
- 只需Entity
- FluentAPI
- 嵌套查询 IN EXISTS
- 条件构造 NO IFELSE
- AND OR 灵活组合
- 连表查询
- 环境和租户自定义
- 等

### 如何体验下功能

springBoot2.X版本下跑起来，主要有以下几个步骤：

- 引入maven依赖
  - 需要包含fluent-mybatis的两个基础包
```xml
    <properties>
        <fluent-mybatis.version>1.8.7</fluent-mybatis.version>
    </properties>
 <!-- 引入fluent-mybatis 运行依赖包, scope为compile -->
        <dependency>
            <groupId>com.github.atool</groupId>
            <artifactId>fluent-mybatis</artifactId>
            <version>${fluent-mybatis.version}</version>
        </dependency>
        <!-- 引入fluent-mybatis-processor, scope设置为provider 编译需要，运行时不需要 -->
        <dependency>
            <groupId>com.github.atool</groupId>
            <artifactId>fluent-mybatis-processor</artifactId>
            <scope>provided</scope>
            <version>${fluent-mybatis.version}</version>
        </dependency>
```
- springboot-web包
- springboot-mybatis包
- lombok和springboot-test
- 此外数据库的驱动包，mysql-connector 或者 postgresql
```xml
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
          <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter-test</artifactId>
            <version>2.3.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <!-- Postgresql驱动包 -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
```
具体版本取决于自身springboot版本，我这边使用的是2.3.12.RELEASE，没有采用默认的Mysql，想看看其他非默认DB的对接情况。
- 书写代码自动生成配置类
  - 确定连接DB的类型、URL、用户名、密码、driver名、schema等
  - 注意不同的DBType部分参数需要指明，默认支持Mysql
  - 执行自动生成配置类后，在配置目录下获取到dao\entity的类
```java
@SpringBootTest(classes = FluentMybatisApplication.class)
public class SystemGeneratorDemo {
    // 数据源 url
    static final String url = "jdbc:postgresql://127.0.0.1:5432/dev";
    // 数据库用户名
    static final String username = "user";
    // 数据库密码
    static final String password = "pass";
    static final String dbDriver="org.postgresql.Driver";

    @Test
    public void generate() throws Exception {
        // 引用配置类，build方法允许有多个配置类
        FileGenerator.build(Empty.class);
    }

    @Tables(
            // 设置数据库连接信息
            url = url, username = username, password = password,
            dbType = DbType.POSTGRE_SQL, //指定PG，默认MySQL
            driver = dbDriver, //指定driver，默认MySQL
            // 设置entity类生成src目录, 相对于 user.dir
            srcDir = "src/main/java",
            // 设置entity类的package值
            basePack = "com.learning.fluentmybatis",
            // 设置dao接口和实现的src目录, 相对于 user.dir
            daoDir = "src/main/java",
            // 设置哪些表要生成Entity文件
            schema = "public", //指定schema, PG上有这个逻辑
            tables = {@Table(value = {"product_info"})}
    )
    static class Empty { //类名随便取, 只是配置定义的一个载体
    }
}
```
- 自动生成实体关系查询类
  - maven compile操作后，能在target下获取dao.base\helper\mapper\wrapper等类，是DAO操作的核心类
  - 在IDEA中，需要将target/generated-sources/annotations加入到source配置中，所谓源代码数据的一部分
  - IDEA配置中，Build,Execution,Deployment/Compilier/Annotation Processoers 打开Enable annotation processing，是编辑器自动识别构建的数据

经过以上配置，就已经完成根据配置扫描的表，自动生成查询所需的帮助类，支持动态的数据查询，不需要依赖繁琐的XML配置，灵活书写单表、多表的查询、更新、删除。

```java
//主类或者配置类上开启包扫描
@MapperScan({"com.learning.fluentmybatis.mapper"})

//==================================
@SpringBootTest
public class ProductInfoMapperTest {
    @Qualifier("fmProductInfoMapper")
    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private ProductInfoBaseDao productInfoBaseDao;

    @Test
    public void testInsert(){
        ProductInfoEntity entity = new ProductInfoEntity();
        entity.setName("test5");
        entity.setDisplayName("TEST5");
        productInfoMapper.insert(entity);
        System.out.println(entity.getId());
    }


    @Test
    public void testUpdate(){
        productInfoMapper.updateBy(productInfoMapper.updater().set.updateTime().is(new Date()).end()
                .where().id().eq(999L).end());
        ProductInfoEntity product = productInfoMapper.findOne(productInfoMapper.query().where().id().eq(999L).end());
        System.out.println(product.toString());
    }

    @Test
    public void testSelect(){
        ProductInfoEntity product = productInfoMapper.findOne(productInfoMapper.query().where().id().eq(999L).end());
        System.out.println(product.toString());
        
    }
}
```

其他指导文档、丰富的查询功能见官方，[Fluent-Mybatis官方直通车点这里](https://gitee.com/fluent-mybatis/fluent-mybatis/wikis/fluent%20mybatis%E7%89%B9%E6%80%A7%E6%80%BB%E8%A7%88)