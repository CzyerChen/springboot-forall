> Thymeleaf官方并没有提供Shiro的标签，我们需要引入第三方实现，地址为https://github.com/theborakompanioni/thymeleaf-extras-shiro

> 依靠这个组件，我们就可以实现我们需要的，有权限就展示，没有权限就不展示

### 实现步骤
1.添加依赖
```text
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
    <version>2.0.0</version>
</dependency>
```
2.注入Bean
```text
@Bean
public ShiroDialect shiroDialect() {
    return new ShiroDialect();
}
```
3.修改页面，加入shiro标签实现控制
```text
<body>
<p>你好！[[${user.username}]]</p>
<p shiro:hasRoe="admin"> 您好，超级管理员</p>
<p shiro:hasRole="test">你好，测试用户</p>
<h3>权限测试链接</h3>
<div>
	<a shiro:hasPermission="user:query" th:href="@{/user/all}">获取用户信息</a>
	<a shiro:hasPermission="user:insert" th:href="@{/user/add}">新增用户</a>
	<a shiro:hasRole="admin" th:href="@{/user/delete}">删除用户</a>
</div>
<a th:href="@{/logout}">注销</a>
</body>
```

### 更多标签
```text
Attribute
<p shiro:anyTag>
  Goodbye cruel World!
</p>


Element
<shiro:anyTag>
  <p>Hello World!</p>
</shiro:anyTag>


The guest tag
<p shiro:guest="">
  Please <a href="login.html">Login</a>
</p>


The user tag
<p shiro:user="">
  Welcome back John! Not John? Click <a href="login.html">here<a> to login.
</p>


The authenticated tag
<a shiro:authenticated="" href="updateAccount.html">Update your contact information</a>


The notAuthenticated tag
<p shiro:notAuthenticated="">
  Please <a href="login.html">login</a> in order to update your credit card information.
</p>


The principal tag
<p>Hello, <span shiro:principal=""></span>, how are you today?</p>
or
<p>Hello, <shiro:principal/>, how are you today?</p>
Typed principal and principal property are also supported.


The hasRole tag
<a shiro:hasRole="administrator" href="admin.html">Administer the system</a>


The lacksRole tag
<p shiro:lacksRole="administrator">
  Sorry, you are not allowed to administer the system.
</p>


The hasAllRoles tag
<p shiro:hasAllRoles="developer, project manager">
  You are a developer and a project manager.
</p>


The hasAnyRoles tag
<p shiro:hasAnyRoles="developer, project manager, administrator">
  You are a developer, project manager, or administrator.
</p>


The hasPermission tag
<a shiro:hasPermission="user:create" href="createUser.html">Create a new User</a>


The lacksPermission tag
<p shiro:lacksPermission="user:delete">
  Sorry, you are not allowed to delete user accounts.
</p>


The hasAllPermissions tag
<p shiro:hasAllPermissions="user:create, user:delete">
  You can create and delete users.
</p>


The hasAnyPermissions tag
<p shiro:hasAnyPermissions="user:create, user:delete">
  You can create or delete users.
</p>
```