- [关于部署环境](#关于部署环境)
- [本机单机可执行包部署、Docker部署](#本机单机可执行包部署docker部署)
  - [Mac部署：下载源文件](#mac部署下载源文件)
  - [可执行包部署 NameServer](#可执行包部署-nameserver)
    - [问题1：资源不足](#问题1资源不足)
    - [补充: 关于日志的输出](#补充-关于日志的输出)
  - [可执行包部署 Broker](#可执行包部署-broker)
    - [对于Local模式](#对于local模式)
    - [对于Cluster模式](#对于cluster模式)
  - [对于 Proxy](#对于-proxy)
  - [Docker部署 NameServer](#docker部署-nameserver)
  - [Docker部署 Broker](#docker部署-broker)

跟着官方文档 `Quick start` 体验 RocketMQ5.X

## 关于部署环境

本地测试环境：

- MacOS 10.15.7
- JDK 17
- docker 20.10.17

官方要求：

- 64位操作系统，推荐 Linux/Unix/macOS
- 64位 JDK 推荐 1.8 以上

以上都是符合的

## 本机单机可执行包部署、Docker部署

### Mac部署：下载源文件

部署包分为源码包和可执行包，源码包下载后需要自己编译，可执行包下载后就是直接启动。为了避免本地环境打包问题，我是直接选用了可执行包进行体验。

- 源码包下载 [点这里下载5.1.4 source版本](https://dist.apache.org/repos/dist/release/rocketmq/5.1.4/rocketmq-all-5.1.4-source-release.zip)
- 可执行包下载 [点这里下载5.1.4 bin版本](https://dist.apache.org/repos/dist/release/rocketmq/5.1.4/rocketmq-all-5.1.4-bin-release.zip)
- 也可到这里精挑细选其他版本：https://dist.apache.org/repos/dist/release/rocketmq/，bin版本直接执行，source版本需要编译

如果想编译，可以参考以下：

```bash
$ unzip rocketmq-all-5.1.4-source-release.zip
$ cd rocketmq-all-5.1.4-source-release/
$ mvn -Prelease-all -DskipTests -Dspotbugs.skip=true clean install -U
$ cd distribution/target/rocketmq-5.1.4/rocketmq-5.1.4
```

对于已经下载好和编译好的执行目录结构大致如下：

```bash
:~/Downloads/rocketmq-all-5.1.4-bin-release$ tree -L 3
.
├── LICENSE
├── NOTICE
├── README.md
├── benchmark
│   ├── batchproducer.sh
│   ├── consumer.sh
│   ├── producer.sh
│   ├── runclass.sh
│   ├── shutdown.sh
│   └── tproducer.sh
├── bin
│   ├── README.md
│   ├── cachedog.sh
│   ├── cleancache.sh
│   ├── cleancache.v1.sh
│   ├── controller
│   │   ├── fast-try-independent-deployment.cmd
│   │   ├── fast-try-independent-deployment.sh
│   │   ├── fast-try-namesrv-plugin.cmd
│   │   ├── fast-try-namesrv-plugin.sh
│   │   ├── fast-try.cmd
│   │   └── fast-try.sh
│   ├── dledger
│   │   └── fast-try.sh
│   ├── export.sh
│   ├── mqadmin
│   ├── mqadmin.cmd
│   ├── mqbroker
│   ├── mqbroker.cmd
│   ├── mqbroker.numanode0
│   ├── mqbroker.numanode1
│   ├── mqbroker.numanode2
│   ├── mqbroker.numanode3
│   ├── mqbrokercontainer
│   ├── mqcontroller
│   ├── mqcontroller.cmd
│   ├── mqnamesrv
│   ├── mqnamesrv.cmd
│   ├── mqproxy
│   ├── mqproxy.cmd
│   ├── mqshutdown
│   ├── mqshutdown.cmd
│   ├── nohup.out
│   ├── os.sh
│   ├── play.cmd
│   ├── play.sh
│   ├── runbroker.cmd
│   ├── runbroker.sh
│   ├── runserver.cmd
│   ├── runserver.sh
│   ├── setcache.sh
│   ├── startfsrv.sh
│   ├── tools.cmd
│   └── tools.sh
├── conf
│   ├── 2m-2s-async
│   │   ├── broker-a-s.properties
│   │   ├── broker-a.properties
│   │   ├── broker-b-s.properties
│   │   └── broker-b.properties
│   ├── 2m-2s-sync
│   │   ├── broker-a-s.properties
│   │   ├── broker-a.properties
│   │   ├── broker-b-s.properties
│   │   └── broker-b.properties
│   ├── 2m-noslave
│   │   ├── broker-a.properties
│   │   ├── broker-b.properties
│   │   └── broker-trace.properties
│   ├── broker.conf
│   ├── container
│   │   └── 2container-2m-2s
│   ├── controller
│   │   ├── cluster-3n-independent
│   │   ├── cluster-3n-namesrv-plugin
│   │   ├── controller-standalone.conf
│   │   └── quick-start
│   ├── dledger
│   │   ├── broker-n0.conf
│   │   ├── broker-n1.conf
│   │   └── broker-n2.conf
│   ├── plain_acl.yml
│   ├── rmq-proxy.json
│   ├── rmq.broker.logback.xml
│   ├── rmq.client.logback.xml
│   ├── rmq.controller.logback.xml
│   ├── rmq.namesrv.logback.xml
│   ├── rmq.proxy.logback.xml
│   ├── rmq.tools.logback.xml
│   └── tools.yml
├── lib
│   ├── animal-sniffer-annotations-1.21.jar
│   ├── annotations-13.0.jar
│   ├── annotations-4.1.1.4.jar
│   ├── annotations-api-6.0.53.jar
│   ├── awaitility-4.1.0.jar
│   ├── bcpkix-jdk15on-1.69.jar
│   ├── bcprov-jdk15on-1.69.jar
│   ├── bcutil-jdk15on-1.69.jar
│   ├── caffeine-2.9.3.jar
│   ├── checker-qual-3.12.0.jar
│   ├── commons-beanutils-1.9.4.jar
│   ├── commons-cli-1.5.0.jar
│   ├── commons-codec-1.13.jar
│   ├── commons-collections-3.2.2.jar
│   ├── commons-digester-2.1.jar
│   ├── commons-io-2.7.jar
│   ├── commons-lang3-3.12.0.jar
│   ├── commons-logging-1.2.jar
│   ├── commons-validator-1.7.jar
│   ├── concurrentlinkedhashmap-lru-1.4.2.jar
│   ├── disruptor-1.2.10.jar
│   ├── dledger-0.3.1.2.jar
│   ├── error_prone_annotations-2.14.0.jar
│   ├── failureaccess-1.0.1.jar
│   ├── fastjson-1.2.83.jar
│   ├── grpc-api-1.50.0.jar
│   ├── grpc-context-1.50.0.jar
│   ├── grpc-core-1.50.0.jar
│   ├── grpc-netty-shaded-1.50.0.jar
│   ├── grpc-protobuf-1.50.0.jar
│   ├── grpc-protobuf-lite-1.50.0.jar
│   ├── grpc-services-1.50.0.jar
│   ├── grpc-stub-1.50.0.jar
│   ├── gson-2.9.0.jar
│   ├── guava-31.1-jre.jar
│   ├── hamcrest-2.1.jar
│   ├── j2objc-annotations-1.3.jar
│   ├── jackson-core-2.15.2.jar
│   ├── jaeger-thrift-1.6.0.jar
│   ├── jaeger-tracerresolver-1.6.0.jar
│   ├── javassist-3.20.0-GA.jar
│   ├── javax.annotation-api-1.3.2.jar
│   ├── jna-4.2.2.jar
│   ├── jsr305-3.0.2.jar
│   ├── jul-to-slf4j-2.0.6.jar
│   ├── kotlin-stdlib-1.6.20.jar
│   ├── kotlin-stdlib-common-1.6.20.jar
│   ├── kotlin-stdlib-jdk7-1.6.20.jar
│   ├── kotlin-stdlib-jdk8-1.6.20.jar
│   ├── libthrift-0.14.1.jar
│   ├── listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar
│   ├── lz4-java-1.8.0.jar
│   ├── netty-all-4.1.65.Final.jar
│   ├── netty-tcnative-boringssl-static-2.0.53.Final-linux-aarch_64.jar
│   ├── netty-tcnative-boringssl-static-2.0.53.Final-linux-x86_64.jar
│   ├── netty-tcnative-boringssl-static-2.0.53.Final-osx-aarch_64.jar
│   ├── netty-tcnative-boringssl-static-2.0.53.Final-osx-x86_64.jar
│   ├── netty-tcnative-boringssl-static-2.0.53.Final-windows-x86_64.jar
│   ├── netty-tcnative-boringssl-static-2.0.53.Final.jar
│   ├── netty-tcnative-classes-2.0.53.Final.jar
│   ├── okhttp-4.11.0.jar
│   ├── okio-3.2.0.jar
│   ├── okio-jvm-3.0.0.jar
│   ├── openmessaging-api-0.3.1-alpha.jar
│   ├── opentelemetry-api-1.29.0.jar
│   ├── opentelemetry-api-events-1.29.0-alpha.jar
│   ├── opentelemetry-context-1.29.0.jar
│   ├── opentelemetry-exporter-common-1.29.0.jar
│   ├── opentelemetry-exporter-logging-1.29.0.jar
│   ├── opentelemetry-exporter-logging-otlp-1.29.0.jar
│   ├── opentelemetry-exporter-otlp-1.29.0.jar
│   ├── opentelemetry-exporter-otlp-common-1.29.0.jar
│   ├── opentelemetry-exporter-prometheus-1.29.0-alpha.jar
│   ├── opentelemetry-exporter-sender-okhttp-1.29.0.jar
│   ├── opentelemetry-extension-incubator-1.29.0-alpha.jar
│   ├── opentelemetry-sdk-1.29.0.jar
│   ├── opentelemetry-sdk-common-1.29.0.jar
│   ├── opentelemetry-sdk-extension-autoconfigure-spi-1.29.0.jar
│   ├── opentelemetry-sdk-logs-1.29.0.jar
│   ├── opentelemetry-sdk-metrics-1.29.0.jar
│   ├── opentelemetry-sdk-trace-1.29.0.jar
│   ├── opentelemetry-semconv-1.29.0-alpha.jar
│   ├── opentracing-noop-0.33.0.jar
│   ├── opentracing-tracerresolver-0.1.8.jar
│   ├── opentracing-util-0.33.0.jar
│   ├── perfmark-api-0.25.0.jar
│   ├── proto-google-common-protos-2.9.0.jar
│   ├── protobuf-java-3.20.1.jar
│   ├── protobuf-java-util-3.20.1.jar
│   ├── rocketmq-acl-5.1.4.jar
│   ├── rocketmq-broker-5.1.4.jar
│   ├── rocketmq-client-5.1.4.jar
│   ├── rocketmq-common-5.1.4.jar
│   ├── rocketmq-container-5.1.4.jar
│   ├── rocketmq-controller-5.1.4.jar
│   ├── rocketmq-example-5.1.4.jar
│   ├── rocketmq-filter-5.1.4.jar
│   ├── rocketmq-grpc-netty-codec-haproxy-1.0.0.jar
│   ├── rocketmq-logback-classic-1.0.1.jar
│   ├── rocketmq-namesrv-5.1.4.jar
│   ├── rocketmq-openmessaging-5.1.4.jar
│   ├── rocketmq-proto-2.0.3.jar
│   ├── rocketmq-proxy-5.1.4.jar
│   ├── rocketmq-remoting-5.1.4.jar
│   ├── rocketmq-rocksdb-1.0.3.jar
│   ├── rocketmq-shaded-slf4j-api-bridge-1.0.0.jar
│   ├── rocketmq-slf4j-api-1.0.1.jar
│   ├── rocketmq-srvutil-5.1.4.jar
│   ├── rocketmq-store-5.1.4.jar
│   ├── rocketmq-tiered-store-5.1.4.jar
│   ├── rocketmq-tools-5.1.4.jar
│   ├── slf4j-api-2.0.3.jar
│   ├── snakeyaml-1.32.jar
│   ├── tomcat-annotations-api-8.5.46.jar
│   ├── tomcat-embed-core-8.5.46.jar
│   └── zstd-jni-1.5.2-2.jar
├── logs
└── nohup.out

17 directories, 192 files
```

### 可执行包部署 NameServer

负责管理消息队列和消费者组，默认启动在9876端口。

```bash
├── LICENSE
├── NOTICE
├── README.md
├── benchmark
├── bin
├── conf
├── lib
├── logs
└── nohup.out
```

其中，bin下是我们执行脚本命令的地方，conf是配置连接信息的地方，logs是日志打印的地方，nohup启动默认日志会输出到nohup.out下

跟着以下指令来启动

```bash
$ nohup sh bin/mqnamesrv &
[1] 111631
[~ rocketmq]# nohup: ignoring input and appending output to 'nohup.out'

[$ rocketmq]# tail -f logs/rocketmqlogs/namesrv.log 
2024-01-08 10:17:08 INFO NSScanScheduledThread - start scanNotActiveBroker
2024-01-08 10:17:13 INFO NSScanScheduledThread - start scanNotActiveBroker
```

正常服务资源充足的话，可以看到 `The Name Server boot success. serializeType=JSON, address 0.0.0.0:9876`

此外官方让在这个目录下查看日志`tail -f ~/logs/rocketmqlogs/namesrv.log`，或者我是在nohup.out 中看的。注意是用户主目录下的 `~/logs` 不是此应用目录的 logs 下。

但是作为测试，也有可能因为资源不足出现执行后却未启动成功的情况，这个时候就需要看日志了

#### 问题1：资源不足

```bash
OpenJDK 64-Bit Server VM warning: INFO: os::commit_memory(0x0000000700000000, 4294967296, 0) failed; error='Not enough space' (errno=12)
#
# There is insufficient memory for the Java Runtime Environment to continue.
```

表示没有足够的空间来启动RocketMQ，那么它默认需要多少呢，是否可以调整呢？是可以的。

通过 `/bin/runserver.sh` 脚本我们可以看到

```bash
 if [ -z "$JAVA_MAJOR_VERSION" ] || [ "$JAVA_MAJOR_VERSION" -lt "9" ] ; then
      JAVA_OPT="${JAVA_OPT} -server -Xms4g -Xmx4g -Xmn2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
      JAVA_OPT="${JAVA_OPT} -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8 -XX:-UseParNewGC"
      JAVA_OPT="${JAVA_OPT} -verbose:gc -Xloggc:${GC_LOG_DIR}/rmq_srv_gc_%p_%t.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
      JAVA_OPT="${JAVA_OPT} -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=30m"
    else
      JAVA_OPT="${JAVA_OPT} -server -Xms4g -Xmx4g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
      JAVA_OPT="${JAVA_OPT} -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:G1ReservePercent=25 -XX:InitiatingHeapOccupancyPercent=30 -XX:SoftRefLRUPolicyMSPerMB=0"
      JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=${GC_LOG_DIR}/rmq_srv_gc_%p_%t.log:time,tags:filecount=5,filesize=30M"
```

这里我们看到虽然JDK版本不同会有相应参数的调整，但是需要的基础资源是 `-Xms4g -Xmx4g` , 初始堆和最大堆的内存要求是4G，如果达不到自然会说资源不够。方法就是根据自身情况，降低配置。
我这边2G内存的测试服务器配置如下：`-server -Xms256m -Xmx256m -Xmn125m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m`

#### 补充: 关于日志的输出

简单看一下日志文件的配置 `~/conf/`

```bash
rmq.broker.logback.xml
rmq.client.logback.xml
rmq.controller.logback.xml
rmq.namesrv.logback.xml
rmq-proxy.json
rmq.proxy.logback.xml
rmq.tools.logback.xml
```

```bash
            <fileNamePattern>${user.home}${file.separator}logs${file.separator}rocketmqlogs${file.separator}otherdays${file.separator}namesrv_traffic.%i.log.gz</fileNamePattern>
```

如果没有输出到项目根目录 `~/logs` 的文件夹内，极有可能是这个参数无法获取或者获取为空的情况。

当前部署的用户是什么用户？并且需要理解 `${user.home}` 这个参数

- 如果是root用户，比如测试用的root用户，`${user.home}` 指的是 `/root`，那么日志会在 `/root/logs` 下
- 如果正常生产一般是一个用户级别目录 比如rmq用户， `${user.home}` 指的是 `/home/rmq`，那么日志会在 `/home/rmq/logs` 下

```bash
/root下
.
├── logs
│   └── rocketmqlogs
│       ├── broker_default.log
│       ├── broker.log
│       ├── commercial.log
│       ├── filter.log
│       ├── lock.log
│       ├── namesrv_default.log
│       ├── namesrv.log
│       ├── namesrv_traffic.log
│       ├── otherdays
│       ├── pop.log
│       ├── protection.log
│       ├── proxy.log
│       ├── proxy_metric.log
│       ├── proxy_watermark.log
│       ├── remoting.log
│       ├── stats.log
│       ├── storeerror.log
│       ├── store.log
│       ├── tools_default.log
│       ├── tools.log
│       ├── transaction.log
│       └── watermark.log
└── store
    ├── abort.bak
    ├── checkpoint
    ├── commitlog
    │   └── 00000000000000000000
    ├── compaction
    │   ├── position-checkpoint
    │   └── position-checkpoint.bak
    ├── config
    │   ├── consumerFilter.json
    │   ├── consumerFilter.json.bak
    │   ├── consumerOffset.json
    │   ├── consumerOffset.json.bak
    │   ├── consumerOrderInfo.json
    │   ├── consumerOrderInfo.json.bak
    │   ├── delayOffset.json
    │   ├── delayOffset.json.bak
    │   ├── subscriptionGroup.json
    │   ├── subscriptionGroup.json.bak
    │   ├── timercheck
    │   ├── timermetrics
    │   ├── timermetrics.bak
    │   ├── topics.json
    │   └── topics.json.bak
    ├── consumequeue
    │   └── dataTopic2
    ├── index
    │   └── 20240103133439115
    ├── lock
    └── timerwheel
```

如果想大调整log日志输出的地方，就可以针对 `.logback.xml` 里面 `<fileNamePattern>${user.home}` 手动调整，修改为期望的地方再启动即可。

### 可执行包部署 Broker

是 RocketMQ 的消息代理服务器，负责接收、处理和存储消息

此处测试使用Local模式启动，推荐使用Local模式，同时也支持Cluster模式

#### 对于Local模式

启动的同时，开始proxy

```bash
$ nohup sh bin/mqbroker -n localhost:9876 --enable-proxy &
[2] 111778
[~ rocketmq]# nohup: ignoring input and appending output to 'nohup.out'

rocketmq]# tail -f ~/logs/rocketmqlogs/proxy.log 
}
2024-01-08 10:13:21 INFO main - ServiceProvider loaded no AccessValidator, using default org.apache.rocketmq.acl.plain.PlainAccessValidator
2024-01-08 10:13:22 INFO main - grpc server has built. port: 8081, tlsKeyPath: 1, tlsCertPath: 4, threadPool: 136314880, queueCapacity: {}, boosLoop: {}, workerLoop: {}, maxInboundMessageSize: {}
2024-01-08 10:13:22 INFO main - Server is running in TLS permissive mode
2024-01-08 10:13:22 INFO main - Using OpenSSL provider
2024-01-08 10:13:22 INFO main - SSLContext created for server
2024-01-08 10:13:23 INFO main - The broker[broker-a, xx.xx.xx.xx:10911] boot success. serializeType=JSON and name server is xx.xx.xx.xx:9876
2024-01-08 10:13:23 INFO main - user specified name server address: xx.xx:xx:xx:9876
2024-01-08 10:13:23 INFO main - grpc server start successfully.
2024-01-08 10:13:23 INFO main - Mon Jan 08 10:13:23 CST 2024 rocketmq-proxy startup successfully
```

启动broker，如果连接的namesrv是有公网IP等，需要明确配置 `应用目录/conf/broker.conf`
如不配置，可能存在连接超时、无法连接的故障，都是因为这边没配对而连接不上

```bash
[root@iZ2ze72qvclbhzjoaz5c2jZ conf]# cat broker.conf 
# nameServer 地址多个用;隔开 默认值null
# 例：127.0.0.1:6666;127.0.0.1:8888 
namesrvAddr = 127.0.0.1:9876
# 集群名称 单机配置可以随意填写，如果是集群部署在同一个集群中集群名称必须一致类似Nacos的命名空间
brokerClusterName = DefaultCluster
# broker节点名称 单机配置可以随意填写，如果是集群部署在同一个集群中节点名称不要重复
brokerName = broker-a
# broker id节点ID， 0 表示 master, 其他的正整数表示 slave，不能小于0 
brokerId = 0
# Broker 对外服务的监听端口 默认值10911
# 端口（注意：broker启动后，会占用3个端口，分别在listenPort基础上-2，+1，供内部程序使用，所以集群一定要规划好端口，避免冲突）
listenPort=10911
# Broker服务地址	String	内部使用填内网ip，如果是需要给外部使用填公网ip
brokerIP1 = 127.0.0.1
# BrokerHAIP地址，供slave同步消息的地址 内部使用填内网ip，如果是需要给外部使用填公网ip
brokerIP2 = 127.0.0.1

# Broker角色 默认值ASYNC_MASTER
# ASYNC_MASTER 异步复制Master，只要主写成功就会响应客户端成功，如果主宕机可能会出现小部分数据丢失
# SYNC_MASTER 同步双写Master，主和从节点都要写成功才会响应客户端成功，主宕机也不会出现数据丢失
# SLAVE
brokerRole = ASYNC_MASTER
# 刷盘方式
# SYNC_FLUSH（同步刷新）相比于ASYNC_FLUSH（异步处理）会损失很多性能，但是也更可靠，所以需要根据实际的业务场景做好权衡，默认值ASYNC_FLUSH
flushDiskType = ASYNC_FLUSH
# 在每天的什么时间删除已经超过文件保留时间的 commit log，默认值04
deleteWhen = 04
# 以小时计算的文件保留时间 默认值72小时
fileReservedTime = 72

# 消息大小 单位字节 默认1024 * 1024 * 4
maxMessageSize=4194304

# 在发送消息时，自动创建服务器不存在的Topic，默认创建的队列数，默认值4
defaultTopicQueueNums=4
# 是否允许Broker 自动创建Topic，建议线下开启，线上关闭
autoCreateTopicEnable=true
# 是否允许Broker自动创建订阅组，建议线下开启，线上关闭
autoCreateSubscriptionGroup=true

# 失败重试时间，默认重试16次进入死信队列，第一次1s第二次5s以此类推。
# 延时队列时间等级默认18个，可以设置多个比如在后面添加一个1d(一天)，使用的时候直接用对应时间等级即可,从1开始到18，如果添加了第19个直接使用等级19即可
messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h

# 指定TM在20秒内应将最终确认状态发送给TC，否则引发消息回查。默认为60秒
transactionTimeout=20
# 指定最多回查5次，超过后将丢弃消息并记录错误日志。默认15次。
transactionCheckMax=5
# 指定设置的多次消息回查的时间间隔为10秒。默认为60秒。
transactionCheckInterval=10
```

#### 对于Cluster模式

```bash
### On machine A, start the first Master, for example, the IP of the NameServer is: 192.168.1.1
$ nohup sh bin/mqbroker -n 192.168.1.1:9876 -c $ROCKETMQ_HOME/conf/2m-noslave/broker-a.properties --enable-proxy &
 
### On machine A, start the second Master, for example, the IP of the NameServer is: 192.168.1.1
$ nohup sh bin/mqbroker -n 192.168.1.1:9876 -c $ROCKETMQ_HOME/conf/2m-noslave/broker-b.properties --enable-proxy &

...
```

-n 多个IP的话使用英文分号隔离，`192.168.1.1:9876;192.161.2:9876`

### 对于 Proxy

是 RocketMQ 的代理服务器，用于扩展消息代理服务器的性能和容量

- Local 模式下，Broker 和 Proxy 是同进程部署
- Cluster 模式下，Broker 和 Proxy 是分开部署

官方没有文档描述docker部署的细节，主要与可执行脚本的指令是一致的，下面做一下测试

### Docker部署 NameServer

原理是结合docker的指令，去调起一个容器，可以从外部挂载配置文件、日志、存储，并且可以通过命令行参数传入动态配置

前置就是 `docker search rocketmq` & `docker pull rocketmq` 想要具体的tag的话，`docker pull rocketmq:5.1.4`

```bash
docker run -d \  
--privileged=true \
#容器的名称
--name rmqnamesrv \ 
#容器映射的端口
-p 9876:9876 \
# 挂载日志文件
-v ~/rocketmq5/nameserver/logs:/home/rocketmq/logs \
# 挂载数据存储
-v ~/rocketmq5/nameserver/store:/home/rocketmq/store \
# 挂载可执行文件，主要为了方便修改参数
-v ~/rocketmq5/nameserver/bin/runserver.sh:/home/rocketmq/rocketmq-5.1.4/bin/runserver.sh \
# 动态指定所需的资源，这边是测试降低资源要求，否则默认4G
-e "MAX_HEAP_SIZE=256M" -e "HEAP_NEWSIZE=128M" \
# 熟悉的启动指令
apache/rocketmq:5.1.4 sh mqnamesrv
```

查看日志是否启动成功，这块由于日志挂载出来了，就可以在外部查看

```bash
The Name Server boot success. serializeType=JSON, address 0.0.0.0:9876
```

### Docker部署 Broker

和NameServer是一致的，将配置、日志、存储外置，便于控制。此外如果是测试环境条件拮据的话，还要适当调整参数。


```bash
docker run -d \
# 容器名称
--name rmqbroker \
# broker与namesrv的连接
--link rmqnamesrv:namesrv \
# 暴露的端口，针对5.0开启proxy的场景，需要映射出8081端口
-p 10911:10911 -p 10909:10909 -p 8081:8081 \
--privileged=true \
# 挂载日志文件
-v ~/rocketmq5/broker/logs:/home/rocketmq/logs  \
# 挂载存储
-v ~/rocketmq5/broker/store:/home/rocketmq/store  \
# 挂载配置
-v ~/rocketmq5/broker/conf/broker.conf:/home/rocketmq/broker.conf  \
# 启动指令，runbroker.sh含动态的资源参数可供下面传入
-v ~/rocketmq5/broker/bin/runbroker.sh:/home/rocketmq/rocketmq-5.1.4/bin/runbroker.sh \
# 限定资源，如果测试环境拮据这块是需要指定的，映射namesrv
-e "MAX_HEAP_SIZE=256M"  -e "HEAP_NEWSIZE=128M" -e "NAMESRV_ADDR=namesrv:9876" \
# 启动指令，与可执行文件启动类似
apache/rocketmq:5.1.4  sh mqbroker -c /home/rocketmq/broker.conf  --enable-proxy
```

broker.conf 里面就是配置连接的数据，与前面配置类似

确定启动成功

```bash
Mon Jan 08 07:32:32 UTC 2024 rocketmq-proxy startup successfully
```

以上为一个参考官方文档进行 RocketMQ5.0 本地测试的流程，搭建后用于体验新版本功能

更多高端、生产配置指导见[官方文档](https://rocketmq.apache.org/docs/quickStart/01quickstart/)