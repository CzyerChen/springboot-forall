- 整个redisson和netty的协议进行了完全的整合，包括handler的介入，eventGroup的使用等，都使用了全套的netty体系

- 由于netty提供了promise功能，这里也大量使用了相应的异步模型来进行数据处理,异步发送，监听器监听返回消息并响应

-  redisson : java 5线程语义，Promise编程语义

- 相比jedis，其支持的特性并不是很高，但对于日常的使用还是没有问题的，完成了一个基本的redis网络实现，可以理解为redisson是一个完整的框架，而jedis即完成了语言层的适配

- redisson在使用来说，是隔绝底层实现，类似JPA一样的，面向领域设计，将信息转化为领域事件，开箱即用，代码规范，适合学习