#### 监控的指标有以下四种
- springboot actuator进行应用的监控,包括：/env、/info、/metrics、/health等;
- /health主要描述应用是否存活，磁盘和数据库连接是否存活
- /info主要访问应用的基础信息，也需要进行配置
```$xslt
info.build.artifact=@project.artifactId@
info.build.name=@project.name@
info.build.description=@project.description@
info.build.version=@project.version@

完成以上配置还需要在Pom文件中添加响应的依赖
<resources>
   <resource>
      <directory>src/main/resources</directory>
      <filtering>true</filtering>
   </resource>
</resources>

<plugin>
   <groupId>org.apache.maven.plugins</groupId>
   <artifactId>maven-resources-plugin</artifactId>
   <version>2.6</version>
   <configuration>
      <delimiters>
         <delimiter>@</delimiter>
      </delimiters>
      <useDefaultDelimiters>false</useDefaultDelimiters>
   </configuration>
</plugin>
```
#### 监控的配置可以说非常简单了，还有结合与springboot的指标监控

1.springboot actuator metrics prometheus springboot actuator metrics prometheus grafana是非常成熟的
 对接普罗米修斯，监控springboot JMX的指标
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>

2.引用该依赖，能够暴露普罗米修斯所需的监控数据格式，安装并配置普罗米修斯，指定间隔pull这个接口的数据，就能够动态展示所需的数据了

#### mapping endpoint
- 能够使用该endpoint获取所有http接口的定义，能够自定义接口文档的页面
```json
{
    "contexts": {
        "application": {
            "mappings": {
                "dispatcherServlets": {
                    "dispatcherServlet": [
                        {
                            "handler": "public com.test.common.model.ApiResponse com.test.controller.action.TestController.getTags()",
                            "predicate": "{[/api/test/tags],methods=[GET]}",
                            "details": {
                                "handlerMethod": {
                                    "className": "com.test.controller.action.TestController",
                                    "name": "getTags",
                                    "descriptor": "()Lcom/test/common/model/ApiResponse;"
                                },
                                "requestMappingConditions": {
                                    "consumes": [],
                                    "headers": [],
                                    "methods": [
                                        "GET"
                                    ],
                                    "params": [],
                                    "patterns": [
                                        "/api/test/tags"
                                    ],
                                    "produces": []
                                }
                            }
                    }]
                }
            } 
        }
    }
}       
```

#### 自定义指标监控
- 继承自AbstractHealthIndicator，实现doHealthCheck方法即可
```java
@Component
public class OlapEngineHealthIndicator extends AbstractHealthIndicator {
    @Autowired
    private OlapSearchClient searchClient;

    /**
     * 功能描述: <br/>
     * 〈健康检测详细操作，通过认证操作测活〉
     *
     * @param  builder
     * @since 1.3.0
     * @author claire
     * @date 2020-01-09 - 18:24
     */
    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            searchClient.auth();
            builder.up()
                    .withDetail("olap","【Connection Active】Successful !");
        }catch (Exception e){
            builder.down()
                    .withDetail("olap","【Connection Error】 Please check the network !");
        }
    }
}

```
