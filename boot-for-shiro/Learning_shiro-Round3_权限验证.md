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
- “user:view”等价于“ user:view:* ”；而“organization”等价于“ organization:* ”或者“ organization:*:* ”。可以这么理解，这种方式实现了前缀匹配
- “user”可以匹配“ user:view ”或“ user:view:1 ”,但是如“ *:view ”不能匹配“ system:user:view ”，需要使用“ *:*:view ”，即后缀匹配必须指定前缀

### 授权匹配流程
```text
      1. 【subject】-> isPermittes /hasRole  :调用后（可能是if/else，或者注解和标签），
                        会委托给 SecurityManager
                                     |
                                     |
                                     |
                                     |
      2. 【SecurityManager】 -> 抵达SecurityManager,接着会委托给 Authorizer
                                     |
                                     |
                                     |
             3.【Authrizer】 ->  调用如 isPermitted(“user:view”)，
      其首先会通过 PermissionResolver 把字符串转换成相应的 Permission 实例
                                     |
                                     |
                                     |
    4.【Realm】 -> 授权前，会调用相应的 Realm 获取 Subject 相应的角色/权限用于匹配传入的角色/权限
                                     |
                                     |
                                     |
      5.【Authorizer】 -> Authorizer 会判断 Realm 的角色/权限是否和传入的匹配，
             如果有多个 Realm，会委托给 ModularRealmAuthorizer 进行循环判断
                                     |
                                     |
                         认证通过返回true,认证不通过返回false                              
```
### Authorizer、PermissionResolver及RolePermissionResolver
- Authorizer 的职责是进行授权（访问控制），是 Shiro API 中授权核心的入口点
- PermissionResolver 用于解析权限字符串到 Permission 实例   
- 而 RolePermissionResolver 用于根据角色解析相应的权限集合。                                      
- 设置 securityManager 的 realms 一定要放到最后，因为在调用 SecurityManager.setRealms 时会将 realm使用 IniSecurityManagerFactory 创建的 IniRealm，因为其初始化顺序的问题可能造成后续的初始化 Permission 造成影响

#### 自定义解析和实现

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


### 自定义Permisson PermissonResolver
- permission是描述权限的顶层接口，可以通过实现Permission来定义一些权限的拦截和授权
- public boolean implies(Permission p) {} 主要实现这个方法，来判断权限的规范
- 有时候可能会出现“user:*:*”这种权限的描述不能满足自定义的需求，那么你就需要自定义一种permission,比如现在想通过“+user+10”,user标识角色，数字标识权限，就需要自己实现
```text
public class BitPermission implements Permission {
    private String resourceIdentify;
    private int permissionBit;
    private String instanceId;

    public  BitPermission(String input){
        String[] array = input.split("\\+");
        if(array.length > 1) {
            resourceIdentify = array[1];
        }
        if(StringUtils.isEmpty(resourceIdentify)) {
            resourceIdentify = "*";
        }
        if(array.length > 2) {
            permissionBit = Integer.valueOf(array[2]);
        }
        if(array.length > 3) {
            instanceId = array[3];
        }
        if(StringUtils.isEmpty(instanceId)) {
            instanceId = "*";
        }
    }

    /**
     * 判断权限匹配，看是否适合自己处理
     * @param p
     * @return
     */
    @Override
    public boolean implies(Permission p) {
        if(!(p instanceof  BitPermission)){
           return  false;
        }
        BitPermission other = (BitPermission)p;
        if(!("*".equals(this.resourceIdentify) || this.resourceIdentify.equals(other.resourceIdentify))){//不是书写为* 并且用户标识不相等的，不能验证
            return  false;
        }
        if(!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) !=0)){
            return false;
        }

        if(!("*".equals(this.instanceId)|| this.instanceId.equals(other))){//权限位不相等并且不为*的，不能验证
            return  false;
        }
        return true;
    }

    public String getResourceIdentify() {
        return resourceIdentify;
    }

    public void setResourceIdentify(String resourceIdentify) {
        this.resourceIdentify = resourceIdentify;
    }

    public int getPermissionBit() {
        return permissionBit;
    }

    public void setPermissionBit(int permissionBit) {
        this.permissionBit = permissionBit;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
```
- implies主要过滤出这个权限的标识是不是在自己的管辖范围之内，需要满足“+xx+数字”这样的情况下才能自己处理
- 除了一个权限的定义，最主要需要一个处理器，如何去进行过滤或者拦截
```text
public class BitPermissionResolver implements PermissionResolver {
    @Override
    public Permission resolvePermission(String permissionString) {
        if(permissionString.startsWith("+")) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
```





