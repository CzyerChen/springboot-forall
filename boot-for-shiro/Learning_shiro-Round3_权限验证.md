> 在上一部分身份验证的背景之下，我们知道如何通过过滤器的配置，对需要认证的页面进行配置和成功和失败的跳转，但是对于系统内的不同角色也需要有不同的权限，这个怎么实现呢？

- 需要有资源，比如哪些页面哪些API，需要有用户，角色和权限的对应表，角色描述了用户的身份，权限表述了每种角色对应的访问路径有哪些
- Shiro 支持粗粒度权限（如用户模块的所有权限）和细粒度权限（操作某个用户的权限，即实例级别的）

### 授权的方式
- 写if/else进行判断


- 通过注解方式，在方法上注明角色控制
```text
// 表示当前Subject已经通过login进行了身份验证；即Subject.isAuthenticated()返回true。
@RequiresAuthentication  
 
// 表示当前Subject已经身份验证或者通过记住我登录的。
@RequiresUser  

// 表示当前Subject没有身份验证或通过记住我登录过，即是游客身份。
@RequiresGuest  

// 表示当前Subject需要角色admin和user。  
@RequiresRoles(value={"admin", "user"}, logical= Logical.AND)  

// 表示当前Subject需要权限user:a或user:b。
@RequiresPermissions (value={"user:a", "user:b"}, logical= Logical.OR)
```

- 通过jsp标签进行控制

### 授权
- 基于角色的访问控制，粒度相对来说粗一些，一个用户的角色定了，那它看到的东西是固定的

- 基于资源的访问控制，粒度相对来说细一些，用户有哪些资源可以访问都是可以控制的

### 字符串通配符权限
- "system:user:create,dalete.view"

### 如何进行表设计
#### 1.基于角色的访问控制
```text
  t_user                t_role               t_permission
    |---t_user_role-----| |--t_role_permission---|

```
- 数据库数据：init.sql,建立密码为123456的用户
- 对于授权的方式，可是使用最传统的if/else方式，可是会让方法体很臃肿，这边采用注解的方式
- 开启权限控制
```text
 @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

```
- 了解一些控制的注解
```text
// 表示当前Subject已经通过login进行了身份验证；即Subject.isAuthenticated()返回true。
@RequiresAuthentication  
 
// 表示当前Subject已经身份验证或者通过记住我登录的。
@RequiresUser  

// 表示当前Subject没有身份验证或通过记住我登录过，即是游客身份。
@RequiresGuest  

// 表示当前Subject需要角色admin和user。  
@RequiresRoles(value={"admin", "normal"}, logical= Logical.AND)  

// 表示当前Subject需要权限user:query或user:delete。
@RequiresPermissions (value={"user:query", "user:delete"}, logical= Logical.OR)
```
- 控制层的请求
```text
@Controller
@RequestMapping("/user")
public class UserController {

    @RequiresPermissions(value = {"user:query"})
    @RequestMapping("/all")
    public String getAllUsers(ModelMap map){
        map.addAttribute("value","获取全部用户信息");
        return "user";
    }

    @RequiresPermissions("user:add")
    @RequestMapping("/add")
    public String userAdd(ModelMap model) {
        model.addAttribute("value", "新增用户");
        return "user";
    }

    @RequiresRoles(value = {"admin"})
    //@RequiresPermissions("user:delete")
    @RequestMapping("/delete")
    public String userDelete(ModelMap model) {
        model.addAttribute("value", "删除用户");
        return "user";
    }

}
```
- 一个可以提供前端支持的UI user.html

- 问题记录：如果没有配置在filterChain中的认证失败，并不会自动跳转到403页面，需要手动捕获，并跳转到403页面
    - 此处使用全局异常处理，对于抛出的全局异常进行捕获并跳转
    - 比如在filterChain中设置了filterChainDefinitionMap.put("/user/delete", "perms[user:delete]");，如果用户没有user:delete权限，那么当其访问/user/delete的时候，页面会被重定向到/403
```text
@ControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * 解决认证失败500问题
     * @return
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public String handleAuthorizationException() {
        return "403";
    }

}
```

#### 2.基于资源的访问控制--暂未实验
```text
   t_user                t_role              r_resources
     |----t_user_role-----| |-----t_role_resources---|
```




