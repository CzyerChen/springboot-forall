```text
基于Redis的Java分布式远程服务，可以用来通过共享接口执行存在于另一个Redisson实例里的对象方法。

换句话说就是通过Redis实现了Java的远程过程调用（RPC）。

分布式远程服务基于可以用POJO对象，方法的参数和返回类不受限制，可以是任何类型
```

```text
这种思想会比较像微服务的情况，或者传统的RPC框架，motan或者Hessian
RPC的原理就是通过底层通信，redis维护一个函数调用映射表
通过发送消息，调用远程服务，传递入参，获取返回值，通过通信将返回值传送回来
一般好像还是rpc框架用的比较多，或者自己来实现调用，没有用过redisson这个分布式服务

1.服务注册  remoteService.register(SomeServiceInterface.class, someServiceImpl);

2.服务调用

3.获取返回值

服务端：
RRemoteService remoteService = redisson.getRemoteService();
SomeServiceImpl someServiceImpl = new SomeServiceImpl();

// 在调用远程方法以前，应该首先注册远程服务
// 只注册了一个服务端工作者实例，只能同时执行一个并发调用
remoteService.register(SomeServiceInterface.class, someServiceImpl);

// 注册了12个服务端工作者实例，可以同时执行12个并发调用
remoteService.register(SomeServiceInterface.class, someServiceImpl, 12);


客户端:
RRemoteService remoteService = redisson.getRemoteService();
SomeServiceInterface service = remoteService.get(SomeServiceInterface.class);

String result = service.doSomeStuff(1L, "secondParam", new AnyParam());

```

- 中间的一些注解：
```text
@REntity  --- 标识对象

@RId  --- 标识ID

@RObjectField  --- 标识对象字段




```