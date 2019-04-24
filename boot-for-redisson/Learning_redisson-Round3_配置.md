> [额外配置:Redis 客户端之Lettuce配置使用（基于Spring Boot 2.x）](https://blog.csdn.net/lms1719/article/details/83653298)

### 1.程序化配置
- 就是在程序中直接配置Config对象,比如你的参数都是在application的配置文件中书写的，注入进来，自己手动生成config
```text
Config config = new Config();
config.setTransportMode(TransportMode.EPOLL);
config.useClusterServers()
```

### 2.从文件读取配置
- 可以从YAML或者JSON的文件中读取配置
- Config.fromJSON ， Config.fromYAML ，接收一个File的对象
```text
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonConfig() throws IOException {
        Config config = Config.fromYAML(new File("redisson-config.yaml"));
        return Redisson.create(config);
    }
}
```
- 上面的程序配置和json yaml文件的方式还是挺常用的，还有可以和spring结合的方式，没有尝试过，来看看
- 通过Spring XML命名空间配置
```text
<redisson:client>
    <redisson:single-server ... />
    <!-- 或者 -->
    <redisson:master-slave-servers ... />
.....
</redisson:client>
```

### 常用配置
- 编码
```text
编码类名称	说明
org.redisson.codec.JsonJacksonCodec	             Jackson JSON 编码 默认编码
org.redisson.codec.AvroJacksonCodec	            Avro 一个二进制的JSON编码
org.redisson.codec.SmileJacksonCodec	        Smile 另一个二进制的JSON编码
org.redisson.codec.CborJacksonCodec	            CBOR 又一个二进制的JSON编码
org.redisson.codec.MsgPackJacksonCodec	        MsgPack 再来一个二进制的JSON编码
org.redisson.codec.IonJacksonCodec	            Amazon Ion 亚马逊的Ion编码，格式与JSON类似
org.redisson.codec.KryoCodec	                Kryo 二进制对象序列化编码
org.redisson.codec.SerializationCodec	        JDK序列化编码
org.redisson.codec.FstCodec	                    FST 10倍于JDK序列化性能而且100%兼容的编码
org.redisson.codec.LZ4Codec	                    LZ4 压缩型序列化对象编码
org.redisson.codec.SnappyCodec	                Snappy 另一个压缩型序列化对象编码
org.redisson.client.codec.JsonJacksonMapCodec	基于Jackson的映射类使用的编码。可用于避免序列化类的信息，以及用于解决使用byte[]遇到的问题。
org.redisson.client.codec.StringCodec	        纯字符串编码（无转换）
org.redisson.client.codec.LongCodec	            纯整长型数字编码（无转换）
org.redisson.client.codec.ByteArrayCodec	    字节数组编码
org.redisson.codec.CompositeCodec	            用来组合多种不同编码在一起
```
- 其他
```text
threads（线程池数量）                                 默认值: 当前处理核数量 * 2

nettyThreads （Netty线程池数量）                      默认值: 当前处理核数量 * 2

eventLoopGroup                                      只有io.netty.channel.epoll.EpollEventLoopGroup或io.netty.channel.nio.NioEventLoopGroup才是允许的类型

transportMode（传输模式）                             默认值：TransportMode.NIO
可选参数： 
TransportMode.NIO, 
TransportMode.EPOLL - 需要依赖里有netty-transport-native-epoll包（Linux）
TransportMode.KQUEUE - 需要依赖里有 netty-transport-native-kqueue包（macOS）

lockWatchdogTimeout（监控锁的看门狗超时，单位：毫秒）    默认值：30000

keepPubSubOrder（保持订阅发布顺序）                     默认值：true

performanceMode（高性能模式）                          默认值：HIGHER_THROUGHPUT
可选模式： 
HIGHER_THROUGHPUT - 将高性能引擎切换到 高通量 模式。 
LOWER_LATENCY - 将高性能引擎切换到 低延时 模式 。

```

### 配置文件详情
- 集群配置
```text
Config config = new Config();
config.useClusterServers()
    .setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
    //可以用"rediss://"来启用SSL连接
    .addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001")
    .addNodeAddress("redis://127.0.0.1:7002");

RedissonClient redisson = Redisson.create(config);

nodeAddresses（添加节点地址） 可以通过host:port的格式来添加Redis集群节点的地址

scanInterval（集群扫描间隔时间） 默认值： 1000   对Redis集群节点状态扫描的时间间隔。单位是毫秒

readMode（读取操作的负载均衡模式）默认值： SLAVE（只在从服务节点里读取）
设置读取操作选择节点的模式。
可用值为：
SLAVE - 只在从服务节点里读取。
MASTER - 只在主服务节点里读取。
MASTER_SLAVE - 在主从服务节点里都可以读取。

subscriptionMode（订阅操作的负载均衡模式） 默认值：SLAVE（只在从服务节点里订阅）
可用值为：
SLAVE - 只在从服务节点里订阅。 
MASTER - 只在主服务节点里订阅。

loadBalancer（负载均衡算法类的选择）
默认值： org.redisson.connection.balancer.RoundRobinLoadBalance
在多Redis服务节点的环境里，可以选用以下几种负载均衡方式选择一个节点：
org.redisson.connection.balancer.WeightedRoundRobinBalancer - 权重轮询调度算法
org.redisson.connection.balancer.RoundRobinLoadBalancer - 轮询调度算法
org.redisson.connection.balancer.RandomLoadBalancer - 随机调度算法
```
[更多参数](https://yq.aliyun.com/articles/551640)
- 集群-yaml
```text
clusterServersConfig:
  idleConnectionTimeout: 10000
  pingTimeout: 1000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  reconnectionTimeout: 3000
  failedAttempts: 3
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  slaveSubscriptionConnectionMinimumIdleSize: 1
  slaveSubscriptionConnectionPoolSize: 50
  slaveConnectionMinimumIdleSize: 32
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 32
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  nodeAddresses:
  - "redis://127.0.0.1:7004"
  - "redis://127.0.0.1:7001"
  - "redis://127.0.0.1:7000"
  scanInterval: 1000
threads: 0
nettyThreads: 0
codec: !<org.redisson.codec.JsonJacksonCodec> {}
"transportMode":"NIO"
```
- 云托管模式  略
- 单节点模式
程序内
```text
SingleServerConfig singleConfig = config.useSingleServer();
```
- 配置详情
```text
address（节点地址）
可以通过host:port的格式来指定节点地址。

subscriptionConnectionMinimumIdleSize（发布和订阅连接的最小空闲连接数）默认值：1
用于发布和订阅连接的最小保持连接数（长连接）

subscriptionConnectionPoolSize（发布和订阅连接池大小）默认值：50
用于发布和订阅连接的连接池最大容量


connectionMinimumIdleSize（最小空闲连接数）默认值：32
最小保持连接数（长连接）

connectionPoolSize（连接池大小）默认值：64
连接池最大容量

```
[更多参数](https://yq.aliyun.com/articles/551640)
- 单节点 -yaml模式
```text
singleServerConfig:
  idleConnectionTimeout: 10000
  pingTimeout: 1000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  reconnectionTimeout: 3000
  failedAttempts: 3
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  address: "redis://127.0.0.1:6379"
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  connectionMinimumIdleSize: 32
  connectionPoolSize: 64
  database: 0
  dnsMonitoring: false
  dnsMonitoringInterval: 5000
threads: 0
nettyThreads: 0
codec: !<org.redisson.codec.JsonJacksonCodec> {}
"transportMode":"NIO"
```
- 哨兵模式
```text
Config config = new Config();
config.useSentinelServers()
    .setMasterName("mymaster")
    //可以用"rediss://"来启用SSL连接
    .addSentinelAddress("redis://127.0.0.1:26389", "redis://127.0.0.1:26379")
    .addSentinelAddress("redis://127.0.0.1:26319");

RedissonClient redisson = Redisson.create(config);
```
- 哨兵配置详细参数
```text
dnsMonitoringInterval（DNS监控间隔，单位：毫秒）默认值：5000
用来指定检查节点DNS变化的时间间隔

masterName（主服务器的名称）
主服务器的名称是哨兵进程中用来监测主从服务切换情况的

addSentinelAddress（添加哨兵节点地址）
可以通过host:port的格式来指定哨兵节点的地址

readMode（读取操作的负载均衡模式）
默认值： SLAVE（只在从服务节点里读取）
可用值为：
SLAVE - 只在从服务节点里读取。
MASTER - 只在主服务节点里读取。
MASTER_SLAVE - 在主从服务节点里都可以读取。

```
参数有大部分都有重复，[更多参数](https://yq.aliyun.com/articles/551640)
- 哨兵 --yaml模式
```text
sentinelServersConfig:
  idleConnectionTimeout: 10000
  pingTimeout: 1000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  reconnectionTimeout: 3000
  failedAttempts: 3
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  slaveSubscriptionConnectionMinimumIdleSize: 1
  slaveSubscriptionConnectionPoolSize: 50
  slaveConnectionMinimumIdleSize: 32
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 32
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  sentinelAddresses:
  - "redis://127.0.0.1:26379"
  - "redis://127.0.0.1:26389"
  masterName: "mymaster"
  database: 0
threads: 0
nettyThreads: 0
codec: !<org.redisson.codec.JsonJacksonCodec> {}
"transportMode":"NIO"
```
- 主从模式
```text
Config config = new Config();
config.useMasterSlaveServers()
    //可以用"rediss://"来启用SSL连接
    .setMasterAddress("redis://127.0.0.1:6379")
    .addSlaveAddress("redis://127.0.0.1:6389", "redis://127.0.0.1:6332", "redis://127.0.0.1:6419")
    .addSlaveAddress("redis://127.0.0.1:6399");

RedissonClient redisson = Redisson.create(config);
```
- 主从配置参数
```text
dnsMonitoringInterval（DNS监控间隔，单位：毫秒）默认值：5000
用来指定检查节点DNS变化的时间间隔

masterAddress（主节点地址）
可以通过host:port的格式来指定主节点地址

addSlaveAddress（添加从主节点地址）
可以通过host:port的格式来指定从节点的地址

readMode（读取操作的负载均衡模式）
默认值： SLAVE（只在从服务节点里读取）
设置读取操作选择节点的模式。
可用值为：
SLAVE - 只在从服务节点里读取。
MASTER - 只在主服务节点里读取。
MASTER_SLAVE - 在主从服务节点里都可以读取。
```
[更多参数](https://yq.aliyun.com/articles/551640)
- 主从 -yaml模式
```text
masterSlaveServersConfig:
  idleConnectionTimeout: 10000
  pingTimeout: 1000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  reconnectionTimeout: 3000
  failedAttempts: 3
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  slaveSubscriptionConnectionMinimumIdleSize: 1
  slaveSubscriptionConnectionPoolSize: 50
  slaveConnectionMinimumIdleSize: 32
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 32
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  slaveAddresses:
  - "redis://127.0.0.1:6381"
  - "redis://127.0.0.1:6380"
  masterAddress: "redis://127.0.0.1:6379"
  database: 0
threads: 0
nettyThreads: 0
codec: !<org.redisson.codec.JsonJacksonCodec> {}
"transportMode":"NIO"
```