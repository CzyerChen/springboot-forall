接上篇，已经完成 RocketMQ5.0 环境的部署，就需要对这个环境进行测试，查看集群、写入消息、读取消息等

- [Docker部署 Dashboard](#docker部署-dashboard)
  - [获取镜像并下载](#获取镜像并下载)
  - [部署服务](#部署服务)
- [客户端连接](#客户端连接)
  - [pom文件](#pom文件)
  - [生产者代码](#生产者代码)
  - [消费者代码](#消费者代码)
  - [接口测试](#接口测试)
  - [问题: broker资源不足无法提供服务](#问题-broker资源不足无法提供服务)

## Docker部署 Dashboard

以上通过可执行文件部署或者容器部署的形式，都需要有一个可以查看的集群的地方，对于官方自己配备的有 `rocketmq-dashboard`, 可以使用docker快速部署，便于测试

### 获取镜像并下载

 `docker search rocketmq-dashboard` & `docker pull apacherocketmq/rocketmq-dashboard`

### 部署服务

```bash
docker run -d --name rmqdashboard -e "JAVA_OPTS=-Xmx256M -Xms256M -Xmn128M -Drocketmq.namesrv.addr=192.168.2.92:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8088:8080 apacherocketmq/rocketmq-dashboard
```

## 客户端连接

手动创建 topic: `sh bin/mqadmin updatetopic -n 192.168.2.92:9876 -t dataTopic2 -c DefaultCluster`

### pom文件

```xml
 <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-boot.version>3.0.2</spring-boot.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.apache.rocketmq</groupId>
            <artifactId>rocketmq-client-java</artifactId>
            <version>5.0.5</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
                <configuration>
                    <mainClass>com.learning.springbootrmq5.SpringbootRmq5Application</mainClass>
                    <skip>true</skip>
                </configuration>
                <executions>
                    <execution>
                        <id>repackage</id>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

### 生产者代码

```java
 @GetMapping("/sendSync")
    public String sendSync() throws ClientException, IOException {
        String endpoint = "192.168.2.92:8081";
        String topic = "dataTopic2";
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoint);
        ClientConfiguration configuration = builder.enableSsl(true).build();
        Producer producer = provider.newProducerBuilder()
                                    .setTopics(topic)
                                    .setClientConfiguration(configuration)
                                    .build();
        Message message = provider.newMessageBuilder()
                                  .setTopic(topic)
                                  .setKeys("messageKey")
                                  .setTag("messageTag")
                                  .setBody("messageBodySync".getBytes())
                                  .build();
        try {
            SendReceipt sendReceipt = producer.send(message);
            log.info("Send sync message successfully, messageId={}", sendReceipt.getMessageId());
        } catch (ClientException e) {
            log.error("Failed to send message", e);
        }
        producer.close();
        return "success";
    }

    @GetMapping("/sendAsync")
    public String sendAsync() throws ClientException, InterruptedException, IOException {
        String endpoint = "192.168.2.92:8081";
        String topic = "dataTopic2";
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().enableSsl(true).setEndpoints(endpoint);
        ClientConfiguration configuration = builder.build();
        Producer producer = provider.newProducerBuilder()
                                    .setTopics(topic)
                                    .setClientConfiguration(configuration)
                                    .build();
        Message message = provider.newMessageBuilder()
                                  .setTopic(topic)
                                  .setKeys("messageKey")
                                  .setTag("messageTag")
                                  .setBody("messageBodyASync".getBytes())
                                  .build();
        producer.sendAsync(message);
        log.info("Send async message successfully, messageId");
        return "success";
    }
```

### 消费者代码

```java
@Slf4j
@Component
public class MessageConsumerRunner implements CommandLineRunner {
    @Override
    public void run(final String... args) throws Exception {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        String endpoints = "192.168.2.92:8081";
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                                                                     .setEndpoints(endpoints)
                                                                     .build();
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        String consumerGroup = "YourConsumerGroup";
        String topic = "dataTopic2";
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                                            .setClientConfiguration(clientConfiguration)
                                            .setConsumerGroup(consumerGroup)
                                            .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                                            .setMessageListener(messageView -> {
                                                log.info("Consume message successfully, messageId={}", messageView.getMessageId());
                                                return ConsumeResult.SUCCESS;
                                            })
                                            .build();
        Thread.sleep(Long.MAX_VALUE);
    }
}
```

### 接口测试

请求接口 `/msg/sendAsync`

```log
2024-01-08T15:36:39.343+08:00  INFO 70572 --- [nio-8090-exec-1] c.l.s.d.web.sender.MessageController     : Send async message successfully, messageId
2024-01-08T15:36:39.355+08:00  INFO 70572 --- [onsumption-0-34] c.l.s.d.w.c.MessageConsumerRunner2       : Consume message successfully, messageId=01ACDE4800112213AC05AD400700000000
```

能够正常收发

### 问题: broker资源不足无法提供服务

可能出现的客户端报错为：

```log
org.apache.rocketmq.client.java.exception.InternalErrorException: [request-id=e3f9dxxxx1aa872, response-code=50001] org.apache.rocketmq.proxy.common.ProxyException: service not available now. It may be caused by one of the following reasons: the broker's disk is full [CL:  0.96 CQ:  0.96 INDEX: -1.00], messages are put to the slave, message store has been shut down, etc.

java.util.concurrent.RejectedExecutionException: Task org.apache.rocketmq.shaded.io.grpc.internal.DelayedStream$4@72ba34c2 rejected from java.util.concurrent.ThreadPoolExecutor@7deb0119[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 13]
	at java.base/java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2065) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:833) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1365) ~[na:na]
```


以上大体就是描述资源不足无法进行接入、服务不可达等，通常就是因为环境的资源不足，可能是内存、可能是硬盘

从 `.../broker/logs/rocketmqlogs/store.log` 中可以看出端倪，是磁盘存储不够了

```log
2024-01-08 13:34:24 ERROR StoreScheduledThread1 - physic disk maybe full soon 0.95, so mark disk full, storePathPhysic=/home/rocketmq/store/commitlog
```

可以通过清除以下数据暂时缓解 `.../broker/store/commitlog`，可以发现没怎么用也有好多G。不过确实需要使用的话尽早考虑扩容啊

扩大存储增加可用磁盘空间，就能够正常使用连接了
