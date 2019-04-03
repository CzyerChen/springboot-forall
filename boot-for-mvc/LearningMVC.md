- Thymeleaf是⼀个XML/XHTML/HTML5模板引擎，可⽤于Web与⾮Web环境中的应⽤开发。它是⼀个开源的Java库
- Thymeleaf提供了⼀个⽤于整合Spring MVC的可选模块，在应⽤开发中，你可以使⽤Thymeleaf来完全代替JSP或其他模板引擎，如Velocity、FreeMarker等
- Thymeleaf的主要⽬标在于提供⼀种可被浏览器正确显示的、格式良好的模板创建⽅式，因此也可以⽤作静态建模
- 你可以使⽤它创建经过验证的XML与HTML模板。相对于编写逻辑或代码，开发者只需将标签属性添加到模板中即可

#### 实现MVC
- 通过controller层传入属性，并在前端页面hello.html使用

#### XML请求格式
- HTTP请求的Content-Type有各种不同格式定义，如果要⽀持Xml格式的消息转换，就必须要使⽤对应的转换器。Spring MVC中默认已经有⼀套采⽤Jackson实现的转换器 MappingJackson2XmlHttpMessageConverter
- 传统手动转换方式
```text
@Configuration
public class MessageConverterConfig {extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    public  void configureMessageConverters(List<MappingJackson2HttpMessageConverter> converters){
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
        builder.indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
}
```
- Springboot 世界 ,做一个xml的适配是多么简单
```text
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "User")
public class UserVO {
    @JacksonXmlProperty(localName = "name")
    private String username;
    @JacksonXmlProperty(localName = "age")
    private int age;
}
```
- 测试：localhost:8090/user
- 入参
```text
<User>
	<name>czy</name>
	<age>1</age>
</User>
```
- 返回值
```text
<User>
    <name>insideUser</name>
    <age>22</age>
</User>
```

