- 高并发互联网消息中间件的要求：
```text
1. 多topic，成百上千的要求
2. 事务消息，在一些涉及金额、订单等消息上，要求事务性
3. 问题的快读定位和重试机制，要求问题的溯源，失败自动重试，失败降级，降级限流，死信监控与查询
4. 完善的监控和报警机制
5. 服务本身的高可用、高可靠
6. 完善的运维和管理，有自身支持或者三方平台能够提供控制台的查看
7. 秒级延迟和http协议

```



http://ifeve.com/《apache-rocketmq用户指南》官方文档/

https://blog.csdn.net/chenaima1314/article/details/79202315

http://rocketmq.apache.org/docs/rmq-arc/

https://www.jianshu.com/p/2838890f3284
rocketmq的分布式事务：https://www.jianshu.com/p/53324ea2df92

rocketmq & kafka的对比：
http://jm.taobao.org/2016/03/24/rmq-vs-kafka/

rocketmq的事务性特点:
https://www.jianshu.com/p/53324ea2df92
