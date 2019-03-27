> 来自《SpringBoot实战（第四版）》

> 相对基础偏知识点一些
 
### 什么是Spring Springboot
#### Spring的出世
- Spring诞生时是Java企业版（Java Enterprise Edition，JEE，也称J2EE）的轻量级代替品
- 通过依赖注入和面向切面编程，用简单的Java对象（Plain Old Java Object，POJO）实现了EJB的功能
- 虽然Spring的组件代码是轻量级的，但它的配置却是重量级,需要大量的XML进行Bean的配置，属性注入等
- 开启事务、Spring MVC，还是需要用XML或Java进行显式配置

#### Spring项目
- 一个项目结构，一个web.xml，一个启用SpringMVC的Spring配置，一个控制器响应HTTP请求，一个WEB部署容器，例如TOMCAT

#### Springboot的出世
- Spring Boot的命令行界面（Command Line Interface，CLI）：spring run HelloController.groovy
- Spring Boot的出现就是为了减轻Spring的配置，关于实现原理还是一致的

### Springboot 特性
- 自动配置---会根据配置里面包含的Bean来建立对应的Java Bean
```text
1.Spring Boot在应用程序的Classpath里发现H2数据库的库，那么它就自动配置一个嵌入式H2数据库。如果在Classpath里发现JdbcTemplate，那么它还会为你配置一个JdbcTemplate的Bean

2.自动配置涉及Java持久化API（Java Persistence API，JPA）、Thymeleaf模板、安全和Spring MVC

```

- 起步依赖---如果maven里面添加了很多jar包，版本的管理和冲突时一个很麻烦的事情
```text
1.Spring Boot通过起步依赖为项目的依赖管理提供帮助。起步依赖其实就是特殊的Maven依赖和Gradle依赖，利用了传递依赖解析，把常用库聚合在一起，组成了几个为特定功能而定制的依赖

2.例如你需要web相关的功能，可以添加web模块，需要持久化相关的功能，添加jpa模块，需要security相关的功能，添加security模块
```

- 命令行界面
```text
1.Spring Boot CLI利用了起步依赖和自动配置，让你专注于代码本身

2.编写者只需要重视代码的编写，不需要关心依赖，CLI能检测到你使用了哪些类，它知道要向Classpath中添加哪些起步依赖才能让它运转起来。一旦那些依赖出现在Classpath中，一系列自动配置就会接踵而来，确保启用DispatcherServlet和Spring MVC，这样控制器就能响应HTTP请求了。
```

- actuator---做应用层面的监控
```text
1.提供在运行时检视应用程序内部情况的能力。安装了Actuator就能窥探应用程序的内部情况

2.可以监控Spring应用程序上下文里配置的Bean,
Spring Boot的自动配置做的决策,
应用程序取到的环境变量、系统属性、配置属性和命令行参数,
应用程序里线程的当前状态,
应用程序最近处理过的HTTP请求的追踪情况,
各种和内存用量、垃圾回收、Web请求以及数据源用量相关的指标
```


### 安装 Spring Boot CLI
- 用下载的分发包进行安装。
- 用Groovy Environment Manager进行安装。
- 通过OS X Homebrew进行安装。
- 使用MacPorts进行安装。


### Spring Initializer 初始化Springboot 项目
- 1）通过Web界面使用。
- 2）通过Spring Tool Suite使用。
- 3）通过IntelliJ IDEA使用。
- 4）使用Spring Boot CLI使用。

那最常用的是两种 （1）（3）
- 网页就是通过当问http://start.spring.io。根据需求设置版本、设置需要加载的模板，下载压缩包即可
- IDEA是直接新建spring的项目，输入initializr Service URL :http://start.spring.io 然后也是选择模块等就能生成


### 构建一个简单的spring boot项目
- 建立一个使用web、Thymeleaf、JPA、Mysql模块的spring项目
```text
pom.xml
src -----main
 |        |-------java
                     |---com
 |        |               |---example
 |        |                      |------ExampleApplication.java  ----执行主类
 |        |-------resources
 |                   |---application.properties ---主配置文件
 |                   |---static
 |                   |---templates
 | 
 |-------test
          |---java
                |---com
                     |---example
                           |-------ExampleApplicationTests.java  ---主测试类

```
- @SpringBootApplication ---- 就是开启组建扫描和自动配置
- @Configuration ---- 标明该类使用Spring基于Java的配置
- @ComponentScan ---- 启用组件扫描，这样你写的Web控制器类和其他组件才能被自动发现并注册为Spring应用程序上下文里的Bean
- @Controller  ----  Spring MVC 控制器
### Springboot 构建过程解析

### Springboot 中的依赖关系

### Springboot 中的自动配置

### Springboot test测试

### Springboot + Actuator


### Springboot部署应用

### Springboot 开发者工具