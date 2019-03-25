- liquibase第一次接触的时候，是作为一个数据库sql语句执行的异步执行器，可以进行语句的检测，可以进行数据变更的检测，可以降低数据操作风险,这个也不清楚是不是一种小众的用法
- 后来看到很多liquibase用作数据库迁移，还和flyway进行比较
- [liquibase官网](http://www.liquibase.org/documentation/)


### 一、什么是liquibase?
- LiquiBase是一个用于数据库重构和迁移的开源工具
- 通过日志文件的形式记录数据库的变更，然后执行日志文件中的修改，将数据库更新或回滚到一致的状态

### 二、liquibase的特点
- 支持主流数据库，有MySQL，postgreSQL，Oracle,SqlServer,DB2,还有看到尝试对接H2的
- 支持多开发者共同维护
- 支持多种操作格式，有xml，yaml，json,sql，尝试的有xml和sql，xml是最原始的，sql是最直接的，也是程序员最熟悉的，隔热还是倾向于直接使用sql
- 支持多种运行方式，命令行，Spring继承，Maven插件，Gradle插件，基本上能普遍使用

### 三、本次尝试是与Spring的继承
- 看到有几种方法，一种是通过创建Configuration类，注入一个Bean，一种是通过配置使用Spring默认的实现
- 本文尝试的是第二种

### 优点
- liquibase从应用层面记录sql的操作，并作数据锁，避免给数据库造成永久性的损害