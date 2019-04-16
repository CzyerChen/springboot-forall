```text
                        Filter
                          |
                          |
                     AbstractFilter
                          |
                          |
                     NameableFilter    //组装拦截器链时会根据这个名字找到相应的拦截器实例
                          |
                          |
                   OncePerRequestFilter  //说一次请求只会走一次拦截器链
                          | 
           |-------------------------------|
           |                               |
      AdviceFilter //AOP风格的支持  AbstractShiroFilter
           |                               |
     PathMatchingFilter               ShiroFilter  //整个Shiro的入口点
 基于Ant风格的请求路径匹配功能
   及拦截器参数解析的功能
           |
     AccessControlFilter
     访问控制的基础功能
```


### 拦截器链
- Shiro 对Servlet 容器的FilterChain 进行了代理，即ShiroFilter 在继续Servlet 容器的Filter链的执行之前
- 通过ProxiedFilterChain 对Servlet 容器的FilterChain 进行了代理，
- 先走Shiro 自己的Filter 体系，然后才会委托给Servlet 容器的FilterChain 进行Servlet 容器级别的Filter链执行；
```text
Shiro的ProxiedFilterChain执行流程：
1、先执行Shiro自己的Filter链；
2、再执行Servlet容器的Filter链（即原始的Filter）。
```
- 声明的拦截器
```text
public enum DefaultFilter {
//拦截器类：AnonymousFilter，匿名拦截器，即不需要登录即可访问；一般用于静态资源过滤； 示例“/static/**=anon”
anon(AnonymousFilter.class),

//拦截器类：FormAuthenticationFilter，基于表单的拦截器；如“/**=authc”
authc(FormAuthenticationFilter.class),

//拦截器类：BasicHttpAuthenticationFilter，Basic HTTP身份验证拦截器，
authcBasic(BasicHttpAuthenticationFilter.class),

//拦截器类：LogoutFilter，退出拦截器，主要属性：redirectUrl
logout(LogoutFilter.class),

//拦截器类：NoSessionCreationFilter，不创建会话拦截器
noSessionCreation(NoSessionCreationFilter.class),

//拦截器类：PermissionsAuthorizationFilter，权限授权拦截器，验证用户是否拥有所有权限； 属性和roles 一样； 示例“/user/**=perms["user:create"]”
perms(PermissionsAuthorizationFilter.class),

//拦截器类：PortFilter，端口拦截器，主要属性：port（80）：可以通过的端口；示例“/test= port[80]”，
port(PortFilter.class),

//拦截器类：HttpMethodPermissionFilter，rest 风格拦截器，自动根据请求方法构建权限字符串（ GET=read,POST=create,PUT=update,DELETE=delete,HEAD=read,TRACE=read,OPTIONS=read, MKCOL=create）构建权限字符串；示例“/users=rest[user]”，
rest(HttpMethodPermissionFilter.class),

//拦截器类：RolesAuthorizationFilter，角色授权拦截器，验证用户是否拥有所有角色；主要属性： loginUrl
roles(RolesAuthorizationFilter.class),

//拦截器类:SslFilter，SSL 拦截器，只有请求协议是https 才能通过
ssl(SslFilter.class),

//拦截器类：UserFilter，用户拦截器，用户已经身份验证/记住我
  user(UserFilter.class);
}

org.apache.shiro.web.filter.authz.HostFilter，即主机拦截器，其提供了属性：authorizedIps：已授权的ip 地址，deniedIps：表示拒绝的ip 地址

```
- 动态url拦截器注册
```text
//1、创建FilterChainResolver
PathMatchingFilterChainResolver filterChainResolver =
new PathMatchingFilterChainResolver();
//2、创建FilterChainManager
DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
//3、注册Filter
for(DefaultFilter filter : DefaultFilter.values()) {
filterChainManager.addFilter(
filter.name(), (Filter) ClassUtils.newInstance(filter.getFilterClass()));
}
//4、注册URL-Filter的映射关系
filterChainManager.addToChain("/login.jsp", "authc");
filterChainManager.addToChain("/unauthorized.jsp", "anon");
filterChainManager.addToChain("/**", "authc");
filterChainManager.addToChain("/**", "roles", "admin");
//5、设置Filter的属性
FormAuthenticationFilter authcFilter =
(FormAuthenticationFilter)filterChainManager.getFilter("authc");
authcFilter.setLoginUrl("/login.jsp");
RolesAuthorizationFilter rolesFilter =
(RolesAuthorizationFilter)filterChainManager.getFilter("roles");
rolesFilter.setUnauthorizedUrl("/unauthorized.jsp");
filterChainResolver.setFilterChainManager(filterChainManager);
return filterChainResolver;
```

- 自定义拦截器
1.扩展OncePerRequestFilter
```text
public class MyOncePerRequestFilter extends OncePerRequestFilter {
@Override
protected void doFilterInternal(ServletRequest request, ServletResponse response,
FilterChain chain) throws ServletException, IOException {
System.out.println("=========once per request filter");
chain.doFilter(request, response);
}
}
```
2.AdviceFilter 类似于AOP编程
```text
public class MyAdviceFilter extends AdviceFilter {
@Override
protected boolean preHandle(ServletRequest request, ServletResponse response) throws
Exception {
System.out.println("====预处理/前置处理");
return true;//返回false将中断后续拦截器链的执行
}
@Override
protected void postHandle(ServletRequest request, ServletResponse response) throws
Exception {
System.out.println("====后处理/后置返回处理");
}
@Override
public void afterCompletion(ServletRequest request, ServletResponse response, Exception
exception) throws Exception {
System.out.println("====完成处理/后置最终处理");
}
}
```
3.PathMatchingFilter
```text
public class MyPathMatchingFilter extends PathMatchingFilter {
@Override
protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object
mappedValue) throws Exception {
System.out.println("url matches,config is " + Arrays.toString((String[])mappedValue));
return true;
}
}
```
4.AccessControlFilter
```text
public boolean onPreHandle(ServletRequest request, ServletResponse response, Object
mappedValue) throws Exception {
return isAccessAllowed(request, response, mappedValue)
|| onAccessDenied(request, response, mappedValue);
}
```
- 基于表单的拦截
```text
 public class FormLoginFilter extends PathMatchingFilter {
        private String loginUrl = "/login.jsp";
        private String successUrl = "/";
        @Override
        protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object
                mappedValue) throws Exception {
            if(SecurityUtils.getSubject().isAuthenticated()) {
                return true;//已经登录过
            }
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            if(isLoginRequest(req)) {
                if("post".equalsIgnoreCase(req.getMethod())) {//form表单提交
                    boolean loginSuccess = login(req); //登录
                    if(loginSuccess) {
                        return redirectToSuccessUrl(req, resp);
                    }
                }
                return true;//继续过滤器链
            } else {//保存当前地址并重定向到登录界面
                saveRequestAndRedirectToLogin(req, resp);
                return false;
            }
        }
        private boolean redirectToSuccessUrl(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
            WebUtils.redirectToSavedRequest(req, resp, successUrl);
            return false;
        }
        private void saveRequestAndRedirectToLogin(HttpServletRequest req,
                                                   HttpServletResponse resp) throws IOException {
            WebUtils.saveRequest(req);
            WebUtils.issueRedirect(req, resp, loginUrl);
        }
        private boolean login(HttpServletRequest req) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            try {
                SecurityUtils.getSubject().login(new UsernamePasswordToken(username,
                        password));
            } catch (Exception e) {
                req.setAttribute("shiroLoginFailure", e.getClass());
                return false;
            }
            return true;
        }
        private boolean isLoginRequest(HttpServletRequest req) {
            return pathsMatch(loginUrl, WebUtils.getPathWithinApplication(req));
        }
    }


```

-  任意角色授权拦截器
```text
  public class AnyRolesFilter extends AccessControlFilter {
        private String unauthorizedUrl = "/unauthorized.jsp";
        private String loginUrl = "/login.jsp";
        protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                          Object mappedValue) throws Exception {
            String[] roles = (String[])mappedValue;
            if(roles == null) {
                return true;//如果没有设置角色参数，默认成功
            }
            for(String role : roles) {
                if(getSubject(request, response).hasRole(role)) {
                    return true;
                }
            }
            return false;//跳到onAccessDenied处理
        }
        @Override
        protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
                throws Exception {
            Subject subject = getSubject(request, response);
            if (subject.getPrincipal() == null) {//表示没有登录，重定向到登录页面
                saveRequest(request);
                WebUtils.issueRedirect(request, response, loginUrl);
            } else {
                if (StringUtils.hasText(unauthorizedUrl)) {//如果有未授权页面跳转过去
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {//否则返回401未授权状态码
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
            return false;
        }
    }
```

### shiro的拦截流程
```text
AbstractShiroFilter //如ShiroFilter/ SpringShiroFilter都继承该Filter
    doFilter //Filter的doFilter
        doFilterInternal //转调doFilterInternal
            executeChain(request, response, chain) //执行拦截器链
                FilterChain chain = getExecutionChain(request, response, origChain) //使用原始拦截器链获取新的拦截器链
                    chain.doFilter(request, response) //执行新组装的拦截器链
                    
getExecutionChain(request, response, origChain) //获取拦截器链流程
    FilterChainResolver resolver = getFilterChainResolver(); //获取相应的FilterChainResolver
    FilterChain resolved = resolver.getChain(request, response, origChain); //通过FilterChainResolver根据当前请求解析到新的FilterChain拦截器链



默认情况下如使用ShiroFilterFactoryBean创建shiroFilter时，

默认使用PathMatchingFilterChainResolver进行解析，

而它默认是根据当前请求的URL获取相应的拦截器链，使用Ant模式进行URL匹配；

默认使用DefaultFilterChainManager进行拦截器链的管理。
```
- 源码
ShiroFilter中我们经常手动地可以将自定义地filter通过map地方式设置进来：Map<String, Filter> filters = new LinkedHashMap()
我们通过LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();加入过滤链地时候，这个会被最终转化为Map<String, NamedFilterList> filterChains
```text
public class DefaultFilterChainManager implements FilterChainManager {
    private static final transient Logger log = LoggerFactory.getLogger(DefaultFilterChainManager.class);
    private FilterConfig filterConfig;
    private Map<String, Filter> filters = new LinkedHashMap();
    private Map<String, NamedFilterList> filterChains = new LinkedHashMap();

    public DefaultFilterChainManager() {
        this.addDefaultFilters(false);
    }

    public DefaultFilterChainManager(FilterConfig filterConfig) {
        this.setFilterConfig(filterConfig);
        this.addDefaultFilters(true);
    }
    ......
    
}
```
- shiroFilter基于Path地调用链流程
```text
PathMatchingFilterChainResolver默认流程

public class PathMatchingFilterChainResolver implements FilterChainResolver {
    private static final transient Logger log = LoggerFactory.getLogger(PathMatchingFilterChainResolver.class);
    private FilterChainManager filterChainManager;
    private PatternMatcher pathMatcher = new AntPathMatcher();

    public PathMatchingFilterChainResolver() {
    //默认的调用链管理器DefaultFilterChainManager
        this.filterChainManager = new DefaultFilterChainManager();
    }

    public PathMatchingFilterChainResolver(FilterConfig filterConfig) {
        this.filterChainManager = new DefaultFilterChainManager(filterConfig);
    }

    //获取调用链的核心方法
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChainoriginalChain) {
     //1、首先获取拦截器链管理器
     FilterChainManager filterChainManager = getFilterChainManager();
     //没有过滤器
        if (!filterChainManager.hasChains()) {
            return null;
        }else {
          String requestURI = this.getPathWithinApplication(request);
   
      //存在过滤器
        //2、接着获取当前请求的URL（不带上下文）
        Iterator var6 = filterChainManager.getChainNames().iterator();
        //迭代器读取每一个过滤器
        String pathPattern;
         do {
            if (!var6.hasNext()) {
                return null;
            }
            pathPattern = (String)var6.next();
          } while(!this.pathMatches(pathPattern, requestURI));//当不匹配到过滤器的循环，拦截器链的名字就是URL模式
       
            if (log.isTraceEnabled()) {
               log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " + "Utilizing corresponding filter chain...");
           }
            //最终返回URL模式定义的拦截器链
             return filterChainManager.proxy(originalChain, pathPattern);
         }
    }
    
    ......
}
```
- 如果多个拦截器链都匹配了当前请求URL，那么只返回第一个找到的拦截器链；后续我们可以修改此处的代码，将多个匹配的拦截器链合并返回。
- DefaultFilterChainManager内部使用Map 来管理URL模式-拦截器链的关系；也就是说相同的URL模式只能定义一个拦截器链，不能重复定义，并且map的无序会造成获取的那个拦截器也不能确定是哪一个


### 调用链拦截器管理器-FilterChainManager
- 这是一个接口，我们上面看到了默认的调用连管理器存在一些问题，默认的PathMatchingFilterChainResolver和DefaultFilterChainManager不能满足我们的需求，那么我们是不是可以自定义来解决这种问题呢？
```text
public interface FilterChainManager {
    Map<String, Filter> getFilters();

    NamedFilterList getChain(String var1);

    boolean hasChains();

    Set<String> getChainNames();

    FilterChain proxy(FilterChain var1, String var2);

    void addFilter(String var1, Filter var2);

    void addFilter(String var1, Filter var2, boolean var3);

    void createChain(String var1, String var2);

    void addToChain(String var1, String var2);

    void addToChain(String var1, String var2, String var3) throws ConfigurationException;
}
```
```text
PathMatchingFilterChainResolver
              |
              |
          getChain
              |
              |
filterChainManager.proxy(originalChain, pathPattern)  问题一：原生只提供代理单个调用，而且是相同模式下的随机一个，而我们需要的是全部调用链
              |
              |
   源自 FilterChainManager的proxy方法
              |
              |
 唯一的实现类 DefaultFilterChainManager 
              |
             \|/
   public FilterChain proxy(FilterChain original, String chainName) {
         NamedFilterList configured = this.getChain(chainName);
         if (configured == null) {
             String msg = "There is no configured chain under the name/key [" + chainName + "].";
             throw new IllegalArgumentException(msg);
         } else {
             return configured.proxy(original);
         }
     }

```
- 因而我们需要做的有两个地方，对于默认的DefaultFilterChainManager，仅实现了接口，而并没有增加提供多调用链的实现，这点需要补充
- PathMatchingFilterChainResolver也仅仅调用filterChainManager接口的代理方法，并不能支持多过滤器
- CustomDefaultFilterChainManager
```text
public class CustomDefaultFilterChainManager extends DefaultFilterChainManager {
    private Map<String, String> filterChainDefinitionMap = null;

    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;

    public CustomDefaultFilterChainManager() {
        setFilters(new LinkedHashMap<String, Filter>());
        setFilterChains(new LinkedHashMap<String, NamedFilterList>());
        addDefaultFilters(false);
    }


    @PostConstruct
    public void init() {
        //Apply the acquired and/or configured filters:
        Map<String, Filter> filters = getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                applyGlobalPropertiesIfNecessary(filter);
                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }
            }
        }

        //build up the chains:
        Map<String, String> chains = getFilterChainDefinitionMap();
        //差别在于这里，需要给一个过滤器都加入调用链
        if (!CollectionUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                createChain(url, chainDefinition);
            }
        }
    }

    @Override
    protected void initFilter(Filter filter) {
        //ignore
    }

    public FilterChain proxy(FilterChain original, List<String> chainNames) {
        NamedFilterList configured = new SimpleNamedFilterList(chainNames.toString());
        for(String chainName : chainNames) {
            configured.addAll(getChain(chainName));
        }
        return configured.proxy(original);
    }

    private void applyGlobalPropertiesIfNecessary(Filter filter) {
        applyLoginUrlIfNecessary(filter);
        applySuccessUrlIfNecessary(filter);
        applyUnauthorizedUrlIfNecessary(filter);
    }


    private void applyLoginUrlIfNecessary(Filter filter) {
        String loginUrl = getLoginUrl();
        if (StringUtils.hasText(loginUrl) && (filter instanceof AccessControlFilter)) {
            AccessControlFilter acFilter = (AccessControlFilter) filter;
            //only apply the login url if they haven't explicitly configured one already:
            String existingLoginUrl = acFilter.getLoginUrl();
            if (AccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl)) {
                acFilter.setLoginUrl(loginUrl);
            }
        }
    }

    private void applySuccessUrlIfNecessary(Filter filter) {
        String successUrl = getSuccessUrl();
        if (StringUtils.hasText(successUrl) && (filter instanceof AuthenticationFilter)) {
            AuthenticationFilter authcFilter = (AuthenticationFilter) filter;
            //only apply the successUrl if they haven't explicitly configured one already:
            String existingSuccessUrl = authcFilter.getSuccessUrl();
            if (AuthenticationFilter.DEFAULT_SUCCESS_URL.equals(existingSuccessUrl)) {
                authcFilter.setSuccessUrl(successUrl);
            }
        }
    }

    private void applyUnauthorizedUrlIfNecessary(Filter filter) {
        String unauthorizedUrl = getUnauthorizedUrl();
        if (StringUtils.hasText(unauthorizedUrl) && (filter instanceof AuthorizationFilter)) {
            AuthorizationFilter authzFilter = (AuthorizationFilter) filter;
            //only apply the unauthorizedUrl if they haven't explicitly configured one already:
            String existingUnauthorizedUrl = authzFilter.getUnauthorizedUrl();
            if (existingUnauthorizedUrl == null) {
                authzFilter.setUnauthorizedUrl(unauthorizedUrl);
            }
        }
    }
    .....
    
 }
```
- CustomPathMatchingFilterChainResolver
```text
public class CustomPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    private CustomDefaultFilterChainManager customDefaultFilterChainManager;

    public void setCustomDefaultFilterChainManager(CustomDefaultFilterChainManager customDefaultFilterChainManager) {
        this.customDefaultFilterChainManager = customDefaultFilterChainManager;
        setFilterChainManager(customDefaultFilterChainManager);
    }

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String requestURI = getPathWithinApplication(request);

        List<String> chainNames = new ArrayList<String>();
        //the 'chain names' in this implementation are actually path patterns defined by the user.  We just use them
        //as the chain name for the FilterChainManager's requirements
        for (String pathPattern : filterChainManager.getChainNames()) {

            // If the path does match, then pass on to the subclass implementation for specific checks:
            if (pathMatches(pathPattern, requestURI)) {
                chainNames.add(pathPattern);
            }
        }

        if(chainNames.size() == 0) {
            return null;
        }

        return customDefaultFilterChainManager.proxy(originalChain, chainNames);
    }
}

```
