> 页面级别权限控制


### 导入标签库
- <%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

1.guest
- 用户没有身份验证时显示相应信息，即游客访问信息 
```text
<shiro:guest>
欢迎游客访问，<a href="${pageContext.request.contextPath}/login.jsp">登录</a>
</shiro:guest>
```
2.user 
- 用户已经身份验证/记住我登录后显示相应的信息
```text
<shiro:user>
欢迎[<shiro:principal/>]登录，<a href="${pageContext.request.contextPath}/logout">退出</a>
</shiro:user>
```
3.authenticated 
- 用户已经身份验证通过，即Subject.login登录成功，不是记住我登录的
```text
<shiro:authenticated>
用户[<shiro:principal/>]已身份验证通过
</shiro:authenticated>
```
4.notAuthenticate
- 用户已经身份验证通过，即没有调用Subject.login进行登录，包括记住我自动登录的也属于未进行身份验证
```text
<shiro:notAuthenticated>
未身份验证（包括记住我）
</shiro:notAuthenticated>
```
5.principal
- 显示用户身份信息，默认调用Subject.getPrincipal()获取，即Primary Principal。
```text
<shiro: principal/>

相当于Subject.getPrincipals().oneByType(String.class)。
<shiro:principal type="java.lang.String"/>

相当于((User)Subject.getPrincipals()).getUsername()
<shiro:principal property="username"/>

```

6.hasRole
- 如果当前Subject有角色将显示body体内容。
```text
<shiro:hasRole name="admin">
用户[<shiro:principal/>]拥有角色admin<br/>
</shiro:hasRole>
```

7.hasAnyRole
- 如果当前Subject有任意一个角色（或的关系）将显示body体内容
```text
<shiro:hasAnyRoles name="admin,user">
用户[<shiro:principal/>]拥有角色admin 或user<br/>
</shiro:hasAnyRoles>
```

8.lacksRole
- 如果当前Subject没有角色将显示body体内容
```text
<shiro:lacksRole name="abc">
用户[<shiro:principal/>]没有角色abc<br/>
</shiro:lacksRole>
``` 

9.hasPermission
- 如果当前Subject有权限将显示body体内容
```text
<shiro:hasPermission name="user:create">
用户[<shiro:principal/>]拥有权限user:create<br/>
</shiro:hasPermission>
```

10.lacksPermission
- 如果当前Subject没有权限将显示body体内容
```text
<shiro:lacksPermission name="org:create">
用户[<shiro:principal/>]没有权限org:create<br/>
</shiro:lacksPermission>
```