> 一直存在一个疑惑，学校里面较的Mybatis，起初是手写，觉得很枯燥，有一些注意点，但是并不高级

> 后来接触到了mybatis generator ，通过反向代理生成mapper类，而且也能满足绝大多数的检索需求，可是这样的编程也太容易了吧

> mybatis很讨厌的分页也有别人写的jar可以支持，pagehelper,问题又迎刃而解

> 最后遇到了hibernate/jpa，只要遵循规范去书写，单表查询就变成了零门槛的事情，而且分页也是易如反掌，不用第三方的jar

> 那就意味着hibernate/jpa就是大势所趋吗，也并不是，我在后期看到了很多复杂的项目，很多还是采用了spring+mybatis的方式组织架构，并且正式工作中竟还是需要自己写mapper

> 明白了它们的能力，明白了它们的操作，来看看大佬们怎么谈其中的取舍和选型？

有很多文章谈及这种选择，鄙人就借此总结一下：

### 争议在JPA / MYBATIS
#### 什么是jpa，什么是hibernate
- JPA Java Persistence API，是Java EE 5的标准ORM接口，也是ejb3规范的一部分
- Hibernate，ORM框架，是JPA的一个实现
- JPA是标准接口，Hibernate是实现
```text
Hibernate主要是通过三个组件来实现的，hibernate-annotation、hibernate-entitymanager和hibernate-core

hibernate-annotation是Hibernate支持annotation方式配置的基础，它包括了标准的JPA annotation以及Hibernate自身特殊功能的annotation

hibernate-core是Hibernate的核心实现，提供了Hibernate所有的核心功能

hibernate-entitymanager实现了标准的JPA，可以把它看成hibernate-core和JPA之间的适配器，它并不直接提供ORM的功能，而是对hibernate-core进行封装，使得Hibernate符合JPA的规范
```
- Hibernate是完成数据库表和持久化类之间的映射，简单的查询书写会转换为一种数据库操作语句HQL，并不是普通的sql语句，在配置中开启show-sql=true，可以在控制窗口查看到打印的sql语句

#### 谈谈JPA的设计
1.聚合根和值对象
- 领域驱动设计中有，Entity 实体 和 Value Object 值对象 ，entity具有生命周期，有标识，但是值对象不可变，无标识
- JPA中实体用@Entity标识，标识与数据库表一一对应的意思，值对象只是值得映射，不会有表和它映射

2.仓储
- Repository也是领域设计中一个经典模式，表示基于此种实现的类秉承一个数据仓库的思想，不涉及具体的数据执行操作，只有规范和数据，真正面向领域，并不是面向数据库的开发
- mybatis的实现类似于传统的DAO操作，是sql直接作用到数据库上面，虽然JPA也有@Query用于执行一个手写的SQL，可是这就破坏了设计理念，这是为了让使用者从mybatis到JPA的过渡，因为mybatis以前可以说占领了ORM的大军

3.Specification模式
- 使用JPA有几个场景，手动sql查询@Query
- 使用基于规范简单的单表查询，findByUsernameAndAgeGreaterThan(String username,int age);
- 使用组装Specification，组织复杂的and or 复合查询 join操作，聚集操作，都是依赖于 [JpaSpecificationExecutor 构建的 Specification](https://blog.csdn.net/a184838158/article/details/82658757)

4.乐观锁--- 版本控制
@Version,进行检索的时候会自动进行版本的递增，并带上版本并进行判断
```text
@Entity
public class MyEntity implements Serializable {    
 
    @Id
    @GeneratedValue
    private Long id;
 
    private String name;
 
    @Version
    private Long version;
}
```
5.多表查询
- jpa的确没有对多表查询有很好的支持但并不是不能做，可以用Specification模式的join实现，[例子](http://www.spring4all.com/article/164)
- 但是一般也避免JPA去实现复杂的多表查询，可以通过elasticSearch做视图查询，作用到具体的参数后再查询数据库


6.原生SQL支持
- 注解：@Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)

7.支持自定义查询
- 我之前也总结了官当的文档，可以书写UserRepositoryCustom(Interface) --> UserRepositoryImpl (Class),内部可以直接使用JdbcTemplate




#### 什么是mybatis
- MyBatis是针对的SQL-Maping，我们所书写的sql mapper，通过语法解析，最终拼接成可以直接执行的sql

###  区别
```text
hibernate 基于HQL ，因而Hibernate优化起来相对MyBatis较难

MyBatis入门较快，而Hibernate掌握起来相对较难

Mybatis需要手动编写SQL语句，以及ResultMap。而Hibernate有良好的映射机制，开发者无需关心SQL的生成与结果映射，可以更专注于业务流程

Hibernate数据库移植性很好，MyBatis的数据库移植性不好，不同的数据库需要写不同SQL，mybatis只是具体的sql无法转换，然后HQL能够很好地适应不同的数据库做匹配

```
- 泥瓦匠总结一：使用方面

```text
传统公司、个人开发（可能只是我）喜欢用jpa，互联网公司更青睐于 mybatis

传统公司需求迭代速度慢，项目改动小，hibernate可以帮他们做到一劳永逸；而互联网公司追求快速迭代，需求快速变更，灵活的 mybatis 修改起来更加方便，而且一般每一次的改动不会带来性能上的下降，hibernate经常因为添加关联关系或者开发者不了解优化导致项目越来越糟糕

mybatis官方文档就说了他是一个半自动化的持久层框架，相对于全自动化的 hibernate 他更加的灵活、可控

mybatis 的学习成本低于 hibernate。hibernate 使用需要对他有深入的理解，尤其是缓存方面，作为一个持久层框架，性能依然是第一位的

hibernate 它有着三级缓存，一级缓存是默认开启的，二级缓存需要手动开启以及配置优化，三级缓存可以整合业界流行的缓存技术 redis，ecache 等等一起去实现

```
- 泥瓦匠总结二：业务方面
```text
数据分析型的OLAP应用适合用MyBatis，事务处理型OLTP应用适合用JPA

越是复杂的业务，越需要领域建模，建模用JPA实现最方便灵活

复杂的查询应该是通过CQRS模式，通过异步队列建立合适查询的视图，通过视图避免复杂的Join，而不是直接查询领域模型

从目前的趋势来看OLAP交给NoSQL数据库可能更合适

从国内开源的应用框架来看，国内使用jpa做orm的人还是比较少，如果换成hibernate还会多一些

jpa更灵活，包括基本的增删改查、数据关系以及数据库的切换上都比mybatis灵活，但是jpa门槛较高，另外就是更新数据需要先将数据查出来才能进行更新，数据量大的时候，jpa效率会低一些

``` 

