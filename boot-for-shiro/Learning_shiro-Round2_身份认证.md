## 身份认证
- 用户的身份认证需要两个东西
- principals：身份，即主体的标识属性，可以是任何东西，如用户名、邮箱等，唯一即可。一个主体可以有多个principals，但只有一个Primary principals，一般是用户名/密码/手机号。
- credentials：证明/凭证，即只有主体知道的安全值，如密码/数字证书等

### 认证实践
```text
  @Test
    public void testA(){
        //1.读取配置，初始化SecurityManager工厂，获取SecurityManager实例
        Factory<SecurityManager> factory  = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager instance = factory.getInstance();
        //2.SecurityUtils绑定SecurityManager实例
        SecurityUtils.setSecurityManager(instance);
        //3.得到Subject的身份认证的subject主体
        Subject subject = SecurityUtils.getSubject();
        //封装用户名密码的对象
        UsernamePasswordToken token  = new UsernamePasswordToken("ziyan", "123456");

        try{
            //4. 认证
            subject.login(token);
        }catch (Exception e){
            //5.认证失败
        }

        Assert.assertEquals(true,subject.isAuthenticated());

        subject.logout();

    }
```

- 通过test方法，得出如下流程
```text
1.读取配置，初始化SecurityManager工厂，获取SecurityManager实例
2. 获取SecurityManager并绑定到SecurityUtils
3.通过SecurityUtils得到Subject，其会自动绑定到当前线，获取身份验证的Token
4.调用subject.login 方法进行登录，其会自动委托给SecurityManager.login方法进行登录
5.如果身份验证失败请捕获AuthenticationException 或其子类
6.调用subject.logout退出，其会自动委托给SecurityManager.logout方法退出
```
- 登录内部的详细流程
```text

       subject.login()
             |
            \|/
       Authenticator
             |
            \|/
如果有多Realm的多验证策略，可以通过Authentication Strategy实现
             |
            \|/
最终由不同Realm实现，根据token信息，进行身份认证
 
  
1调用Subject.login(token)进行登录，其会自动委托给Security Manager

2.SecurityManager负责真正的身份验证逻辑；它会委托给Authenticator进行身份验证

3.Authenticator才是真正的身份验证者，Shiro API中核心的身份认证入口点，此处可以自定义插入自己的实现

4. Authenticator可能会委托给相应的AuthenticationStrategy进行多Realm身份验证，默认ModularRealmAuthenticator会调用AuthenticationStrategy进行多Realm身份验证

5.Authenticator 会把相应的token 传入Realm，从Realm 获取身份验证信息，可以配置多个Realm，将按照相应的顺序及策略进行访问

```


### Realm --- 身份验证的主要作用类
#### 1.自定义Realm
- 实现implements Realm
- 实现需要实现的三个方法
```text
public class SysRealm implements Realm {
    public String getName() {
        return "selfDefineRealm";
    }

    /**
     * 仅支持UsernamePasswordToken类
     * @param authenticationToken
     * @return
     */
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String)authenticationToken.getPrincipal();
        String password = (String)authenticationToken.getCredentials();
        if(!"ziyan".equals(username)){
            throw  new UnknownAccountException();
        }

        if(!"123456".equals(password)){
            throw  new IncorrectCredentialsException();
        }

        return new SimpleAuthenticationInfo(username,password,getName());
    }
}
```
#### 2.配置多Realm
- 同样自定义Realm
- shiro-realm.ini文件中
```text
#声明一个realm
myRealm1=com.github.zhangkaitao.shiro.chapter2.realm.MyRealm1
myRealm2=com.github.zhangkaitao.shiro.chapter2.realm.MyRealm2
#指定securityManager的realms实现
securityManager.realms=$myRealm1,$myRealm2
```
- realm会按照指定的顺序进行身份验证

#### 3.Realm的继承关系
```text
             Realm  //上面的测试继承了根
               | 
           CachingRealm //带缓存的Realm
               |
         AuthenticatingRealm 
               |
         AuthorizingRealm //一般继承这个即可
               |
    |-----------------------------------------------------------------------------|
    |                               |                       |                     |
SimpleAccountRealm              JdbcRealm                JndiLdapRealm        AbstreactLdapRealm
    |                                                                             |
TextConfigurationRealm                                                       ActiveDirectoryRealm
    |
    |
    |---------------|
PropertiesRealm  IniRealm    
配置文件读取        ini文件读取

```

- iniRealm 就是刚才演示的一些配置，其余可以百度
```text
[users]
ziyan=123456
claire=111111

sysRealm=com.stest.SysRealm
securityManager=$sysRealm

```
- PropertiesRealm
```text
user.username=password,role1,role2指定用户名/密码及其角色；
role.role1=permission1,permission2指定角色及权限信息
```

- JdbcRealm
```text 
通过sql查询相应的信息，
如“select password from users where username = ?”获取用户密码，
“select password, password_salt from users whereusername = ?”获取用户密码及盐；
“select role_name from user_roles where username = ?”获取用户角色；
“select permission from roles_permissions where role_name = ?”获取角色对应的权限信息；也可以调用相应的api进行自定义sql；
```

#### JdbcRealm
- 初次尝试遇到了一些问题，没有找到很好的例子，导致有些例子过于复杂不利于由浅入深，接下来记录一下尝试全过程
- 利用Springboot2.x版本尝试shiro-spring 1.4.0 利用原生支持的thymeleaf，做简单的页面跳转，jsp用起来出了点问题
- MAVEN
```text
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springboot-forall</artifactId>
        <groupId>com.learning</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>boot-for-shiro</artifactId>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <!-- shiro-spring -->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.4.0</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <dependency>
            <groupId>net.sourceforge.nekohtml</groupId>
            <artifactId>nekohtml</artifactId>
        </dependency>

        <dependency>
            <groupId>org.unbescape</groupId>
            <artifactId>unbescape</artifactId>
            <version>1.1.6.RELEASE</version>
        </dependency>


    </dependencies>

    <build>

        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
- 实体类
```text
@Entity
@Table(name = "t_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private List<TRole> roleList;


    @Transient
    public Set<String> getRolesName() {
        List<TRole> roles = getRoleList();
        Set<String> set = new HashSet<String>();
        for (TRole role : roles) {
            set.add(role.getRolename());
        }
        return set;
    }
}
```
- repo
```text
public interface TUserRepository extends JpaRepository<TUser,Integer> {

    @Query(value = "select u.password from t_user u where u.username =:username",nativeQuery = true)
    String findPasswordByUsername(@Param("username") String username);

    TUser findByUsername(String username);
}

```
- shiro核心配置：很简单，没有缓存，只有一个自定义的认证方式，一个核心的SecurityManager,以及过滤器的配置
```text
@Configuration
@Slf4j
public class ShiroConfig {


    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

   @Bean(name = "selfRealm")
   @DependsOn("lifecycleBeanPostProcessor")
    public SelfRealm selfRealm(){
        return new SelfRealm();
    }


    @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(selfRealm());
        return manager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shirFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }
}

```
- 简单的接口测试
```text
@Controller
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseBo login(@RequestParam String username, @RequestParam String password){
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
            return ResponseBo.ok();
        }catch (UnknownAccountException e){
            return ResponseBo.error(e.getMessage());
        }catch (IncorrectCredentialsException e){
            return ResponseBo.error(e.getMessage());        
        }catch (LockedAccountException e){
            return ResponseBo.error(e.getMessage());
        }catch (ExcessiveAttemptsException e){
            return ResponseBo.error(e.getMessage());        
        }catch (AuthenticationException e){
            return ResponseBo.error(e.getMessage());
        }
    }

    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        String name = (String) SecurityUtils.getSubject().getPrincipal();
        TUser user = new TUser();
        user.setUsername(name);
        model.addAttribute("user", user);
        return "index";
    }
}
```
- 这是第一步的测试，简单的进行自定义的身份认证，读取来自数据库的加密密码

#### 多Realm
- Shiro 可以通过ini文件注入SecurityManager,通过文件配置多个Realm
- shiro继承多个Realm的策略
```text
SecurityManager接口继承了Authenticator，另外还有一个ModularRealmAuthenticator实现，
其委托给多个Realm 进行验证，验证规则通过AuthenticationStrategy 接口指定，默认提供


FirstSuccessfulStrategy：只要有一个Realm验证成功即可，只返回第一个Realm身份验证
成功的认证信息，其他的忽略；

AtLeastOneSuccessfulStrategy：只要有一个Realm验证成功即可，和FirstSuccessfulStrategy
不同，返回所有Realm身份验证成功的认证信息；

AllSuccessfulStrategy：所有Realm验证成功才算成功，且返回所有Realm身份验证成功的
认证信息，如果有一个失败就失败了

```
- 配置文件版本
```text
#指定securityManager的authenticator实现
authenticator=org.apache.shiro.authc.pam.ModularRealmAuthenticator
securityManager.authenticator=$authenticator
#指定securityManager.authenticator的authenticationStrategy
allSuccessfulStrategy=org.apache.shiro.authc.pam.AllSuccessfulStrategy
securityManager.authenticator.authenticationStrategy=$allSuccessfulStrategy

myRealm1=com.github.zhangkaitao.shiro.chapter2.realm.MyRealm1
myRealm2=com.github.zhangkaitao.shiro.chapter2.realm.MyRealm2
myRealm3=com.github.zhangkaitao.shiro.chapter2.realm.MyRealm3
securityManager.realms=$myRealm1,$myRealm3
```
- config方式
```text
    @Bean(name = "securityManager")
       public org.apache.shiro.mgt.SecurityManager securityManager(){
           DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
           ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
           authenticator.setRealms(....);
           authenticator.setAuthenticationStrategy(....);
           manager.setAuthenticator(authenticator);
          
           return manager;
       }
```
- 根据以上策略可以进行身份的多重认证，比如需要手机验证，邮箱验证等，一般登陆页面的话都是采用某一种方式，只有安全校验性极高的表单才会进行多重验证
- 对于认证策略也可以自己定义
```text
 AllSuccessfulStrategy allSuccessfulStrategy = new AllSuccessfulStrategy();
        //每次Realm之前
        allSuccessfulStrategy.beforeAttempt();
        //每次Realm之后
        allSuccessfulStrategy.afterAttempt();
        //所有Realm之前
        allSuccessfulStrategy.beforeAllAttempts();
        //所有Realm之后
        allSuccessfulStrategy.afterAllAttempts();
        
#自定义认证策略，继承AbstractAuthenticationStrategy
public class AllSuccessfulStrategy extends AbstractAuthenticationStrategy
可以重写 beforeAttempt  afterAttempt  beforeAllAttempts  afterAllAttempts四个方法
```



#### 身份认证这一章 主要是通过一个身份认证（仅仅是身份认证）的例子，看到了SecurityUtils Subject SecurityManager Realm ShiroFilter的身影，简单地从数据库中读取加密的密码做身份认证，做简单的登陆拦截和跳转
#### 多Realm的定义，自定义认证策略等方法有待进一步实验

