> shiro其实是当下非常火的身份认证框架，简便的身份认证，灵活的注解，很好地框架集成性，无疑让它成为宠儿

> 也是比较意外的先学习了security,再学习shiro，想看看它是怎样的轻量级

- 学习[张开涛 跟我学shiro]---全面基础
- 学习[wuyouzhuguli](https://github.com/wuyouzhuguli/SpringAll)--这位大哥写的循序渐进，可以跟着上手

### shiro简介
- Apache Shiro 是Java 的一个安全框架。
- 可能没有Spring Security做的功能强大，但是在实际工作时可能并不需要那么复杂的东西，所以使用小而简单的Shiro 就足够了
- 其不仅可以用在JavaSE 环境，也可以用在JavaEE 环境，这意味着什么，更加的灵活，SpringSecurity紧紧和web security的圈子绑定在一起，这是一个问题
- 可以完成：：认证、授权、加密、会话管理、与Web 集成、缓存等


### 特性
- Authentication：身份认证/登录，验证用户是不是拥有相应的身份；
- Authorization：授权，即权限验证，验证某个已认证的用户是否拥有某个权限，；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；
- Session Manager：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通JavaSE环境的，也可以是如Web环境的；
- Cryptography：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；
- Web Support：Web 支持，可以非常容易的集成到Web 环境；
- Caching：缓存，比如用户登录后，其用户信息、拥有的角色/权限不必每次去查，这样可以
- Concurrency：shiro 支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去；
- Testing：提供测试支持；
- Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；
- Remember Me：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了。

### 内部架构
```text
Application Code
       |
      \|/
   Subject -->current user 
       |
      \|/
Shiro Security Manager 管理所有的用户
       |
      \|/
     Realm ---> 访问DB数据进行认证认证的核心   
```
- Subject：主体，代表了当前“用户”
- 所有Subject 都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager；可以把Subject认为是一个门面；SecurityManager才是实际的执行者；
- SecurityManager：安全管理器；即所有与安全有关的操作都会与SecurityManager 交互；且它管理着所有Subject；可以看出它是Shiro 的核心
- Realm：域，Shiro从从Realm获取安全数据（如用户、角色、权限），就是说SecurityManager要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法

#### 认证的简单流程
- 应用代码通过Subject来进行认证和授权
- 而Subject又委托给SecurityManager
- 给Shiro 的SecurityManager 注入Realm
- 从而让SecurityManager 能得到合法的用户及其权限进行判断
- 用户和权限需要开发人员再Realm中自行处理


#### 架构组件
- Subject：主体，可以看到主体可以是任何可以与应用交互的“用户”；
- SecurityManager ：相当于SpringMVC 中的DispatcherServlet 或者Struts2 中的
- FilterDispatcher；是Shiro的心脏；所有具体的交互都通过SecurityManager进行控制；它管理着所有Subject、且负责进行认证和授权、及会话、缓存的管理。
- Authenticator：认证器，负责主体认证的，这是一个扩展点，如果用户觉得Shiro 默认的不好，可以自定义实现；其需要认证策略（Authentication Strategy），即什么情况下算用户认证通过了；
- Authrizer：授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作；即控制着用户能访问应用中的哪些功能；
- Realm：可以有1个或多个Realm，可以认为是安全实体数据源，即用于获取安全实体的；可以是JDBC 实现，也可以是LDAP 实现，或者内存实现等等；由用户提供；注意：Shiro不知道你的用户/权限存储在哪及以何种格式存储；所以我们一般在应用中都需要实现自己的Realm；
- SessionManager：如果写过Servlet就应该知道Session的概念，Session呢需要有人去管理它的生命周期，这个组件就是SessionManager；而Shiro 并不仅仅可以用在Web 环境，也可以用在如普通的JavaSE 环境、EJB 等环境；所有呢，Shiro 就抽象了一个自己的Session来管理主体与应用之间交互的数据；这样的话，比如我们在Web 环境用，刚开始是一台Web 服务器；接着又上了台EJB 服务器；这时想把两台服务器的会话数据放到一个地方，这个时候就可以实现自己的分布式会话（如把数据放到Memcached服务器）；
- SessionDAO：DAO 大家都用过，数据访问对象，用于会话的CRUD，比如我们想把Session保存到数据库，那么可以实现自己的SessionDAO，通过如JDBC 写到数据库；比如想把Session 放到Memcached 中，可以实现自己的Memcached SessionDAO；另外SessionDAO中可以使用Cache进行缓存，以提高性能；
- CacheManager：缓存控制器，来管理如用户、角色、权限等的缓存的；因为这些数据基本上很少去改变，放到缓存中后可以提高访问的性能
 Cryptography：密码模块，Shiro 提高了一些常见的加密组件用于如密码加密/解密的。
 
 


