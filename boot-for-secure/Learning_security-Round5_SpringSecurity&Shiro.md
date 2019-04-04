> 这边并不是专题介绍Shiro,只是在SpringSecurity中拿出来做一下对比，shiro轻型简易，越来越受宠爱，SpringSecurity依靠它完备的安全机制，细粒度的控制，和Spring的天生结合，他们之间一定可以比一比
## 五、SpringSecurity & Shiro

### SpringSecurity & Shiro 相同点
1、认证功能
2、授权功能
3、加密功能
4、会话管理
5、缓存支持
6、rememberMe功能
....

Shiro 有的功能 ，SpringSecurity都有

### SpringSecurity & Shiro 区别
#### Shiro
- 在实现安全认证所需功能的前提下，坚守简单性和灵活性，能够非常灵活的处理认证、授权、管理会话以及密码加密

1.特点
- 易于理解的 Java Security API；
- 简单的身份认证（登录），支持多种数据源（LDAP，JDBC，Kerberos，ActiveDirectory 等）；
- 对角色的简单的签权（访问控制），支持细粒度的签权；
- 支持一级缓存，以提升应用程序的性能；
- 内置的基于 POJO 企业会话管理，适用于 Web 以及非 Web 的环境；
- 异构客户端会话访问；
- 非常简单的加密 API；
- 不跟任何的框架或者容器捆绑，可以独立运行

2.概念
- Authentication：身份认证/登录，验证用户是不是拥有相应的身份
- Authorization：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情
- Session Manager：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中
- Cryptography：加密，保护数据的安全性
- Web Support：Web支持，可以非常容易的集成到Web环境
- Caching：缓存
- Concurrency：shiro支持多线程应用的并发验证
- Testing：提供测试支持
- Run As：允许一个用户假装为另一个用户的身份进行访问
- Remember Me：记住我

3.Shiro四大核心功能:Authentication,Authorization,Cryptography,Session Management

4.Shiro三个核心组件：Subject, SecurityManager 和 Realms.
- Subject：主体，代表了当前“用户”，所有Subject都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager
- SecurityManager：安全管理器；即所有与安全有关的操作都会与SecurityManager交互；且它管理着所有Subject
- Realm：域，Shiro从从Realm获取安全数据（如用户、角色、权限），如果用户名密码是存放在DB里面的，就可以通过Realm中逻辑进行判断

#### SpringSecurity
- 除了基本的shiro有的安全认证功能，它能与Spring紧密结合，对Oauth、OpenID也有支持，无需自己实现，权限控制粒度更细
```text
OAuth在"客户端"与"服务提供商"之间，设置了一个授权层（authorization layer）。"客户端"不能直接登录"服务提供商"，只能登录授权层，以此将用户与客户端区分开来。"客户端"登录授权层所用的令牌（token），与用户的密码不同。用户可以在登录的时候，指定授权层令牌的权限范围和有效期

OpenID 系统的第一部分是身份验证，即如何通过 URI 来认证用户身份
与OpenID同属性的身份识别服务商还有ⅥeID,ClaimID,CardSpace,Rapleaf,Trufina ID Card等
```
- 是一个能够为基于Spring的企业应用系统提供声明式的安全访问控制解决方案的安全框架
- 相对逻辑和实现会比Shiro麻烦一些，但是基本的组件会大致相同，SpringSecurity依赖Spring的IOC和AOP做了比较好的对象管理和依赖注入

1.模块有：
- Web/Http 安全 : 利用上面分析过过滤链路，一系列的Filter做好了session的管理，安全上下文的创建，身份认证，权限控制和异常处理等
- 业务对象或者方法的安全：控制方法访问权限
- AuthenticationManager：AM是核心的一个认证器
- AccessDecisionManager：为 Web 或方法的安全提供访问决策
- AuthenticationProvider：ProviderManager是最终决定通过哪些方案去认证用户的地方
- UserDetailsService：一个接口，提供一个loadByUsername的方法，用于获取来自DB层的用户身份信息
