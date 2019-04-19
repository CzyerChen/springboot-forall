> redis大家早有耳闻，是一个分布式内存数据库，功能很多，jedis客户端可以执行原生redis命令

> 什么是redisson,仅仅是个简单的redis客户端封装吗，想必没有这么简单吧，不然它怎么能拿到这么多star?

### 什么是redisson
- Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）
- 提供了一系列的分布式的Java常用对象，还提供了许多分布式服务,包括(BitSet, Set, Multimap, SortedSet, Map, List, Queue, BlockingQueue, Deque, BlockingDeque, Semaphore, Lock, AtomicLong, CountDownLatch, Publish / Subscribe, Bloom filter, Remote service, Spring cache, Executor service, Live Object service, Scheduler service
- 确实提供了很多redis的操作封装，可是源于一个很重要的思想“关注点分离”，它和jedis的很大不同，他不用关心redis到底怎么去执行命令，解析命令，它只关注有哪些数据结构，可以怎么用的特点
- Redisson底层采用的是Netty 框架。支持Redis 2.8以上版本，支持Java1.6+以上版本