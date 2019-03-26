- SwaggerUI 是一个规范和完整的框架
- 用于生成、描述、调用和可视化 RESTful 风格的 Web 服务
- 总体目标是使客户端和文件系统作为服务器以同样的速度来更新

### 重点
- @EnableSwagger2
- @Api  @ApiOperation @ApiParam等注解 
- 以上是简单的利用swagger呈现API接口文档，还可以结合自身的身份认证对API接口页面实现认证
- 以后慢慢丰富慢慢成长


### 其他
#### 一、spring boot Api返回封装
1.如果不封装可以怎么做

不封装的话，可能你会说我像返回什么直接return不行吗，这对于规范的API来说确实不行，但是你不想封装多余的对象，可以怎么做？

2.可以用这个ResponseEntity，这是spring-web提供的一个封装的API返回对象

3.拿成功返回的ok来研究
- 第一个无参的方法，调用了status(HttpStatus.OK)
```text
public static ResponseEntity.BodyBuilder ok() {
        return status(HttpStatus.OK);
    }
```
- httpStatus.OK是什么,(int value, String reasonPhrase),下面这些就是HttpStatus给我们写好的返回值，我们是自己写的话，这都要写下来，很好别人帮我们写好了
```text
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),
    CHECKPOINT(103, "Checkpoint"),
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MULTI_STATUS(207, "Multi-Status"),
    ALREADY_REPORTED(208, "Already Reported"),
    IM_USED(226, "IM Used"),
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    /** @deprecated */
    @Deprecated
    MOVED_TEMPORARILY(302, "Moved Temporarily"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    /** @deprecated */
    @Deprecated
    USE_PROXY(305, "Use Proxy"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    /** @deprecated */
    @Deprecated
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    URI_TOO_LONG(414, "URI Too Long"),
    /** @deprecated */
    @Deprecated
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    I_AM_A_TEAPOT(418, "I'm a teapot"),
    /** @deprecated */
    @Deprecated
    INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),
    /** @deprecated */
    @Deprecated
    METHOD_FAILURE(420, "Method Failure"),
    /** @deprecated */
    @Deprecated
    DESTINATION_LOCKED(421, "Destination Locked"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    LOCKED(423, "Locked"),
    FAILED_DEPENDENCY(424, "Failed Dependency"),
    UPGRADE_REQUIRED(426, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, "Precondition Required"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    LOOP_DETECTED(508, "Loop Detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");
```
- 调用的status方法呢？调用了静态内部类的构造方法，传递了参数
```text
  public static ResponseEntity.BodyBuilder status(HttpStatus status) {
        Assert.notNull(status, "HttpStatus must not be null");
        return new ResponseEntity.DefaultBuilder(status);
    }
```
- 带参数的ok呢？调用了静态内部类的body方法，实际是实现了BodyBuilder的接口
```text
 public static <T> ResponseEntity<T> ok(T body) {
        ResponseEntity.BodyBuilder builder = ok();
        return builder.body(body);
    }
```
```text
public interface BodyBuilder extends ResponseEntity.HeadersBuilder<ResponseEntity.BodyBuilder> {
        ResponseEntity.BodyBuilder contentLength(long var1);

        ResponseEntity.BodyBuilder contentType(MediaType var1);

        <T> ResponseEntity<T> body(@Nullable T var1);
    }
```
   
4.如果封装可以怎么封装?我们从HttpStatus，ResponseEntity里面似乎得到了启发，需要有code ，需要responseMessage,需要可以设置一个返回值body
- 比如我设计一个ApiResponse,舍去ResponseEntity复杂的内部静态类和静态接口，设置以上所述的三个值
- 经过设计可以得到：
```text
public class ApiResponse {
    public int code;
    public String responseMessage;
    public Object data;

    public static class Builder {
        private int code;
        private String responseMessage;
        private Object data;

        public ApiResponse.Builder code(int code){
            this.code = code;
            return this;
        }
        public ApiResponse.Builder responseMessage(String responseMessage){
            this.responseMessage = responseMessage;
            return  this;
        }
        public ApiResponse.Builder data(Object data){
            this.data = data;
            return  this;
        }

        public ApiResponse build() {
            return new ApiResponse(this.code, this.responseMessage, this.data);
        }

    }

    public ApiResponse(int code, String responseMessage, Object data) {
        this.code = code;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public static ApiResponse.Builder builder() {
        return new ApiResponse.Builder();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Object getData() {
        return data;
    }
}
```
- 返回的数据变得比较友好
```text
 return ApiResponse.builder().code(200).responseMessage("SUCCESS").data(userService.getAllUsers()).build();
```
- 展示结果如下
```text
{
"code":200,
"responseMessage":"SUCCESS",
"data":[{"id":1,"name":"czy","sex":1,"phone":"1370000- 0000","address":"hangzhou","email":"632300000@xxx.com"}]
}
```
- 以上是结合ResponseEntity和lombok的一些思考，将返回值封装了一下


#### 二、spring boot国际化设置
- 国际化是一个比较让人忽视的点，特别是在API交互层，我们通常交互都需要规定编码UTF-8，一般来说都是默认的，但是碰上有的对接家它是国外的返回值用中文人家也不懂，为了更大化的适应，因而引入国际化
- 今天尝试的是spring boot项目，那么真的是非常的方便，为什么?springboot默认就支持国际化的，而且不需要你过多的配置，只需要在resources/下创建国际化配置文件即可，注意名称必须以messages开始
- messages.properties（默认的语言配置文件，当找不到其他语言的配置的时候，使用该文件进行展示）messages_zh_CN.properties（中文）messages_en_US.properties（英文）
- 这里的改造就是将手写的返回值放到配置文件中，利用国际化，解决返回的问题

1.国际化配置1
- 首先了解国际化配置文件为resource目录下的message.properties文件，如果要修改这个路径，可以在application.properties中设置spring.messages.basename=i18n/message
```text
spring.messages.basename=i18n/message
spring.messages.encoding=UTF-8
```
- 为什么是这样改，我们可以了解一下源码
```text
@Configuration
@ConditionalOnMissingBean(
    value = {MessageSource.class},
    search = SearchStrategy.CURRENT
)
@AutoConfigureOrder(-2147483648)
@Conditional({MessageSourceAutoConfiguration.ResourceBundleCondition.class})
@EnableConfigurationProperties
public class MessageSourceAutoConfiguration {
    private static final Resource[] NO_RESOURCES = new Resource[0];

    public MessageSourceAutoConfiguration() {
    }

    @Bean
    @ConfigurationProperties(
        prefix = "spring.messages"
    )
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Bean
    public MessageSource messageSource() {
        MessageSourceProperties properties = this.messageSourceProperties();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (StringUtils.hasText(properties.getBasename())) {
            messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
        }

        if (properties.getEncoding() != null) {
            messageSource.setDefaultEncoding(properties.getEncoding().name());
        }

        messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
        Duration cacheDuration = properties.getCacheDuration();
        if (cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }

        messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
        return messageSource;
    }

    protected static class ResourceBundleCondition extends SpringBootCondition {
        private static ConcurrentReferenceHashMap<String, ConditionOutcome> cache = new ConcurrentReferenceHashMap();

        protected ResourceBundleCondition() {
        }

        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String basename = context.getEnvironment().getProperty("spring.messages.basename", "messages");
            ConditionOutcome outcome = (ConditionOutcome)cache.get(basename);
            if (outcome == null) {
                outcome = this.getMatchOutcomeForBasename(context, basename);
                cache.put(basename, outcome);
            }

            return outcome;
        }

        private ConditionOutcome getMatchOutcomeForBasename(ConditionContext context, String basename) {
            Builder message = ConditionMessage.forCondition("ResourceBundle", new Object[0]);
            String[] var4 = StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(basename));
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String name = var4[var6];
                Resource[] var8 = this.getResources(context.getClassLoader(), name);
                int var9 = var8.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    Resource resource = var8[var10];
                    if (resource.exists()) {
                        return ConditionOutcome.match(message.found("bundle").items(new Object[]{resource}));
                    }
                }
            }

            return ConditionOutcome.noMatch(message.didNotFind("bundle with basename " + basename).atAll());
        }

        private Resource[] getResources(ClassLoader classLoader, String name) {
            String target = name.replace('.', '/');

            try {
                return (new PathMatchingResourcePatternResolver(classLoader)).getResources("classpath*:" + target + ".properties");
            } catch (Exception var5) {
                return MessageSourceAutoConfiguration.NO_RESOURCES;
            }
        }
    }
}
```
现在这样看spring.messages.basename=i18n/message这样的配置就看上去不奇怪了把，自己配置就会根据这个前缀拼上classpath*:" + target + ".properties，最后就是classpath*:i18n/message.properties，这样就能准确找到配置文件啦

2.国际化数据的获取
- 看到了国际化配置的源码，相比你看到了一个MessageSource，这个就是我们可以获取国际化配置的类，我们可以通过@Autowird MessageSource messageSource注入使用
- 获取方式
```text
第一步：注入MessageSource
@Autowird 
MessageSource messageSource

第二步：获取Locale对象
方式一：Locale locale = LocaleContextHolder.getLocale（）

方式二：Locale locale1= RequestContextUtils.getLocale(request);

第三步：获取对应字段
String msg = messageSource.getMessage("api.success,code", null,locale);

```
- 以上三步，我们就可以根据网页页面的语言回复对应数据进行展示了

3.国际化实现的原理
- 刚才也有说到，可以根据网页页面的语言来给出对应版本的国际化标识，那么这个是怎么做到的呢？
- 因为有一个区域解析器在进行处理，  Spring采用的默认区域解析器是AcceptHeaderLocaleResolver。它通过检验HTTP请求的accept-language头部来解析区域。这个头部是浏览器结合操作系统的语言系统来设置的，后台是无法从修改的
- 但是有一个疑问，难道电脑操作系统是英文的，我就不能展示一些中文相关的东西了吗？
- 有会话区域解析器之SessionLocaleResolver，设置完只针对当前的会话有效，session失效，还原为默认状态
```text
在启动类下，注入一个Bean
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
         //设置默认区域,
         slr.setDefaultLocale(Locale.CHINA);
         return slr;
    }
```
- 我们浏览一些英文网站的时候常常会发现，有提示说切换成英文，切换成中文，我们点击一个“英文”，我们展示的页面就变成了英文，这是怎么实现的呢？
- 我们进行页面语言的切换其实就是请求一个接口，将数据转换成英文，然后刷新页面展示
````text
@RequestMapping("/change/sessionLanauage/{lang}")
    public String changeSessionLanauage(HttpServletRequest request,@PathVariable String lang){
       if("zh".equals(lang)){
           request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("zh", "CN")); 
       }elseif("en".equals(lang)){
           request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("en", "US")); 
       }
       return "redirect:/index";
    }
````
- 这样也会有一个问题，就是你当前页面关了，过一会再进来，又要重新设置页面为英文，重新请求，这样不太友好，用户肯定希望自己登陆情况下，我的设置能够有一定时间的保持，那就提到了一个cookie的概念
```text
//获取区域解析器
LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

localeResolver.setLocale(request, response, new Locale("en", "US"));

```
- Cookie区域解析器之CookieLocaleResolver
```text
    @Bean
    public LocaleResolver localeResolver() {
       CookieLocaleResolver slr = new CookieLocaleResolver();
        //设置默认区域,
        slr.setDefaultLocale(Locale.CHINA);
        slr.setCookieMaxAge(3600);//设置cookie有效期.
        returnslr;
    }
```
- 固定的区域解析器之FixedLocaleResolver 
```text
    @Bean
    public LocaleResolver localeResolver() {
       FixedLocaleResolver slr = new FixedLocaleResolver ();
        //设置默认区域,
        slr.setDefaultLocale(Locale.US);
        returnslr;
    }
```
- 还有一种奇葩的需求，就是http请求头里面接收某一参数，协商认定为是locale的配置，就可以实现用户自定义，那么后端可以通过拦截器，获取对应字段的设置，修改不论是session还是cookie的locale配置


#### 三、API 全局优雅异常设计
- 异常有分受检异常和非受检异常，受检异常希望用户进行捕获并进行后续操作，非受检的异常可以抛出给JVM处理
- 在做API设计的时候，往往需要根据一些用户输入、请求次数、请求合法性做业务层面的异常设计，而不是仅限于默认的一些RuntimeException
- 在业务层和API层，怎么设计异常，并进行捕获或者抛出呢？

- java中的那些异常
```text
         Throwable
             |
             |
    |----------------------------------|
    |                                  |
  Error                            Exception
                                        |
                             |----------------------|
                    受检  IOException         RuntimeException 非受检（java.lang.NullPointerException／java.lang.IllegalArgumentException）
                         
```
1.什么时候抛异常
- 关于一些远程调用、读取文件等请求的异常，涉及到资源的，需要及时catch并进行异常处理，做好资源的回收，对于service层的其他异常建议向上抛，不要再service层做处理
- 到了controller 层还需不需要抛出去呢？如果是需要异常处理或者转换的就需要catch处理，如果不需要就可以直接向外抛出

2.异常设计
- 通过以上认识，我们一般设计一个自定义异常类，然后继承RuntimeException 非受检的异常类
- 介绍两种判断参数合理性的方式Guava中的Preconditions类实现了很多入参方法的判断，sr 303的validation规范，这样而已减少我们代码中很多!=null ==null StringUtils.isBlank()的很多检查，使代码变得整洁
```text
@Null	被注释的元素必须为 null
@NotNull	被注释的元素必须不为 null
@AssertTrue	被注释的元素必须为 true
@AssertFalse	被注释的元素必须为 false
@Min(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max, min)	被注释的元素的大小必须在指定的范围内
@Digits (integer, fraction)	被注释的元素必须是一个数字，其值必须在可接受的范围内
@Past	被注释的元素必须是一个过去的日期
@Future	被注释的元素必须是一个将来的日期
@Pattern(value)	被注释的元素必须符合指定的正则表达式
@Email	被注释的元素必须是电子邮箱地址
@Length	被注释的字符串的大小必须在指定的范围内
@NotEmpty	被注释的字符串的必须非空
@Range	被注释的元素必须在合适的范围内
```
- 设计自定义异常，和上面API返回值其实类似，code就是一个errorcode,responseMessage就是一个错误信息，data这边自然没有了
- 异常可以只有状态码，比如404，500，但是比如是自定义的异常“用户未登陆”、“用户认证过期”等，就需要自己设计错误码，给出错误信息返回，那么可以统一设计成错误码、错误信息提示的异常
```text
public class ApiException  extends RuntimeException {
    private Long code;
    private Object data;

    public ApiException(Long code,String message,Object data,Throwable e){
        super(message,e);//RuntimeException会指出对应的提示信息和异常栈
        this.code = code;
        this.data = data;
    }
    public ApiException(Long code,String message,Object data){
        this(code,message,data,null);
    }

    public ApiException(Long code,String message){
        this(code,message,null,null);
    }

    public ApiException(String message,Throwable e){
        this(null,message,null,e);
    }
    public ApiException(Throwable e){
        super(e);
    }

    public ApiException(){

    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}


public class UserNotFoundException extends ApiException {
    
    public UserNotFoundException(String message){
        super(UserErrorCode.UserNotFoundErrorCode.longValue(),message,null);
    }
}

```
3.异常码的设计
- 上面接口返回值中通过国际化操作可以设置了一种统一配置返回码和返回提示的地方，这是一种设计的逻辑
- 如果不适用上面的方式，就可以定义一个抽象类，内部有静态常量，用来定义错误码，这有一个问题，就是错误信息需要自己定义，那就会在controller层有太多随意的输入，相对比较简陋一些
```text
public abstract class UserErrorCode {
    public static final Long UserNotFoundErrorCode = 10000L;
}
```

4.全局异常设计，统一处理用户的自定义异常
- @ControllerAdvice  --- 标识这是一个全局异常处理类
```text
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {
........
}
```
可以设置basePackages、Annotation，对指定包下和指定注解下的异常进行处理

- @RestControllerAdvice  --- 针对RestController内的异常进行处理，包含@ControllerAdvice @ResponseBody，其实非常常用
```text
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
......
}

```
- @ExceptionHandler(value = {ApiException.class}) ----对于指定的异常类型进行处理，比如自定义的异常怎么处理，系统的其他runtime异常怎么处理
```text
@ExceptionHandler(value = {ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse bussinessExeption(ApiException e, HttpServletResponse httpServletResponse) {
        ApiResponse.Builder response = new ApiResponse.Builder();
        if (e instanceof ApiException) {
            response.code(((ApiException) e).getCode()).responseMessage(e.getMessage()).data(((ApiException) e).getData());
        }
        return response.build();
    }
```
- @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)---设置返回码

5.通过全局异常的设计，可以把ApiResponse ApiException GlobalExceptionHandler swaagerui的知识全部连贯起来




