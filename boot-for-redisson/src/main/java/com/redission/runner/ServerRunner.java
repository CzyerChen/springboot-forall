package com.redission.runner;

import com.redission.domain.User;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.PatternMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 24 10:17
 */
@Component
public class ServerRunner implements CommandLineRunner {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void run(String... args) throws Exception {
        /**
         * PART1 :异步操作
         */
       /* RAtomicLong testLong = redissonClient.getAtomicLong("testLong");
        testLong.compareAndSet(0,10);
        RFuture<Boolean> booleanRFuture = testLong.compareAndSetAsync(10, 30);
        booleanRFuture.whenComplete((res,exp) ->{
            System.out.println("处理结果 ："+res);
        });

        booleanRFuture.thenAccept( res ->{
            System.out.println(res);
        }).exceptionally(exp ->{
            exp.printStackTrace();
            System.out.println("出现异常");
            return  null;
        });

        //已经过时了
        booleanRFuture.addListener(new FutureListener<Boolean>() {
            @Override
            public void operationComplete(Future<Boolean> booleanFuture) throws Exception {

            }
        });

        long testLong1 = redissonClient.getAtomicLong("testLong").get();
        System.out.println(testLong1);*/


        /**
         * PART2:RMAP
         */
        /*RMap map = redissonClient.getMap("testMap");
        System.out.println(map.getName());
        Object mapkey1 = map.get("mapkey1");
        map.put("mapkey1","1111");
        Object ifAbsent = map.putIfAbsent("mapkey1", "222");
        Map insertMap = new HashMap<>();
        insertMap.put("insert1","11111");
        insertMap.put("insert2","22222");
        map.putAll(insertMap);
        //fastput和put差距在于有没有返回值，fastput比较快哦
        map.fastPut("mapkey2","zzxxxx");*/

        /**
         * RKEYS  这个命令感觉用处不太大，flushDB??
         */
       /* RKeys keys = redissonClient.getKeys();
        System.out.println(keys.count());
        Iterable<String> foundedKeys = keys.getKeysByPattern("test*");
        long numOfDeletedKeys = keys.delete("obj1", "obj2", "obj3");
        long deletedKeysAmount = keys.deleteByPattern("test?");
        String randomKey = keys.randomKey();*/


        /**
         * 通用对象桶
         */
        /*RBucket<User> bucket = redissonClient.getBucket("testObject");
        bucket.set(new User(1,"hello"));
        User user = bucket.get();
        //if empty,现在不是empty ,无效
        bucket.trySet(new User(2,"world"));
        //不满足预期值，无效
        bucket.compareAndSet(new User(2,"world"),new User(3,"bucket"));
        //返回旧值，成功
        User bucketAndSet = bucket.getAndSet(new User(4, "flow"));
        System.out.println(bucketAndSet.toString());*/

        /**
         * 二进制流（Binary Stream）
         * Redisson的分布式RBinaryStream Java对象同时提供了InputStream接口和OutputStream接口的实现
         */

        /**
         * 地理空间对象桶（Geospatial Bucket）
         * 这个其实结合动态热点的地理位置，并作实时位点成图是比较有意义的
         * 实时数据成图
         * 离你最近的xxx，离你最近的车站
         * 离你一公里以内的出租车。。。
         * 共享地理位置，你们两个相距多远，最短路径。。。
         * 。。。。
         */
       /* RGeo<String>  geo = redissonClient.getGeo("infoGeo");
        //加入一些位点信息
        geo.add(new GeoEntry(13.361389, 38.115556, "Palermo"),
                new GeoEntry(15.087269, 37.502669, "Catania"));
        geo.addAsync(37.618423, 55.751244, "Moscow");

        *//**
         * 获取两点之间的距离
         *//*
        Double dist = geo.dist("Palermo", "Catania", GeoUnit.METERS);
        //返回他们的geoHash值
        geo.hashAsync("Palermo", "Catania");
        //返回他们的地址位置
        Map<String, GeoPosition> positions = geo.pos("test2", "Palermo", "test3", "Catania", "test1");
        //距离某一个点距离200公里内的set，返回object
        List<String> cities = geo.radius(15, 37, 200, GeoUnit.KILOMETERS);
        //返回所有和目标点之间的距离，返回distance
        //radiusWithDistance
        //返回geo position mapped by object
        Map<String, GeoPosition> citiesWithPositions = geo.radiusWithPosition(15, 37, 200, GeoUnit.KILOMETERS);

        //传入对象信息，可以看到更多丰富的内容
        RGeo<User> userRGeo = redissonClient.getGeo("userGeo");
        userRGeo.add(new GeoEntry(13.361389, 38.115556, new User(1,"user1")),
                new GeoEntry(15.087269, 37.502669, new User(2,"user2")));
        Double dist1 = userRGeo.dist(new User(1, "user1"), new User(2, "user2"), GeoUnit.METERS);
        System.out.println(dist1);*/

        /**
         * 原子数据类型
         *
         */
       /* RAtomicLong atomicLong = redissonClient.getAtomicLong("atomicLong");
        long l = atomicLong.incrementAndGet();
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble("atomicDouble");
        double andIncrement = atomicDouble.getAndIncrement();*/


        /**
         * 话题订阅
         * 这个在redis client的时候，利用原生的命令也尝试过，就是一个简单的消息发送和订阅，订阅可以多方，可以模糊
         */
        /*RTopic<String> testToic = redissonClient.getTopic("testToic");
        long publish = testToic.publish("\"infomation\"");

        testToic.addListener((channel, msg) -> System.out.println("信道："+channel.toString() +"收到消息:"+msg));

        RPatternTopic<String> patternTopic = redissonClient.getPatternTopic("test*");
        patternTopic.addListener(new PatternMessageListener<String>() {
            @Override
            public void onMessage(CharSequence pattern, CharSequence channel, String msg) {
                System.out.println("pattern :" + pattern.toString() +"信道："+channel.toString() +"收到消息:"+msg);
            }
        });*/


        /**
         * 布隆过滤器：这个想必大家都有听说过缓存穿透，这是redis的两大问题之一，提到的解决办法就有实现布隆过滤器，过滤不合法的值，从而避免这种风险
         * Redisson利用Redis实现了Java分布式布隆过滤器（Bloom Filter）
         */
       /* RBloomFilter<User> testBloom = redissonClient.getBloomFilter("testBloom");
        testBloom.tryInit(100000L, 0.03);
        testBloom.add(new User(1, "user1"));
        testBloom.add(new User(2, "user2"));
        boolean user3 = testBloom.contains(new User(3, "user3"));
        boolean user2 = testBloom.contains(new User(2, "user2"));*/
        //集群版本的布隆过滤器：getClusteredBloomFilter，操作是一样的


        /**
         * 累加器：Long 和Double 操作是一样的
         * 采用了与java.util.concurrent.atomic.DoubleAdder类似的接口
         * 通过利用客户端内置的DoubleAdder对象，为分布式环境下递增和递减操作提供了很高得性能
         * 据统计其性能最高比分布式AtomicDouble对象快 12000 倍
         * 完美适用于分布式统计计量场景。
         * 需要手动销毁
         *
         * 场景嘛：递增主键的获取？数值的累加？
         * */
        /*RLongAdder longAdder = redissonClient.getLongAdder("longAdder");
        longAdder.add(1);
        longAdder.increment();
        longAdder.increment();
        longAdder.add(1000);
        longAdder.decrement();
        longAdder.add(23);
        long sum = longAdder.sum();
        longAdder.reset();
        longAdder.destroy();*/

        /**
         * 限流器：
         * 基于Redis的分布式限流器可以用来在分布式环境下现在请求方的调用频率
         * 不保证公平
         */
       /* RRateLimiter limiter = redissonClient.getRateLimiter("limiter");
        //一秒允许十个令牌访问
        limiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);
        //申请4个令牌
        limiter.tryAcquire(4);
        //申请4个令牌，允许等待两秒
        limiter.tryAcquire(4, 2, TimeUnit.SECONDS);
        //申请2个令牌，允许等待两秒
        limiter.tryAcquireAsync(2, 2, TimeUnit.SECONDS);

        //默认获取一个，一直等待
        limiter.acquire();

        //异步获取
        RFuture<Void> voidRFuture = limiter.acquireAsync();*/


        /**
         * Map -- RMAP
         * 分布式映射结构的RMap Java对象实现了java.util.concurrent.ConcurrentMap接口和java.util.Map接口
         * 保持了元素的插入顺序
         * 最大元素数量是4 294 967 295个
         *
         */
        /*RMap<String, String > testMap = redissonClient.getMap("testMap");
        testMap.put("key1","value1");
        testMap.putIfAbsent("key1","value11");

        testMap.remove("key1");
        testMap.fastPut("key2","value2");
        testMap.fastRemove("key2");

        RFuture<String> stringRFuture = testMap.putAsync("key3", "value3");
        RFuture<Boolean> booleanRFuture = testMap.fastPutAsync("key4", "value4");

        testMap.fastPutAsync("key5","value5");
        testMap.fastRemoveAsync("key5");

        RMap<String , User> testUser = redissonClient.getMap("testUser");
        testUser.fastPut("111111",new User(111111,"user1"));
        testUser.fastPut("222222",new User(222222,"user2"));

        //字段锁，用于并发修改部分,锁住key
        RLock lock = testUser.getLock("222222");
        lock.lock(60,TimeUnit.SECONDS);

        //场景：修改缓存值，避免并发修改
        try{
            User user = testUser.get("222222");
            user.setName("updateUser");
            testUser.fastRemove("222222");
            testUser.fastPut("222222",user);
        }catch (Exception e){

        }finally {
            lock.unlock();
        }

        //读写锁 -- 这个是具体的，读和写分开，读可以并发，写不能，其实就是功能分开清晰一点，一般我们想加锁都是写的情况
        //读想加锁，也是能够并发读
        RReadWriteLock readWriteLock = testUser.getReadWriteLock("111111");
        RLock rLock = readWriteLock.readLock();
        try{
            User user = testUser.get("111111");
            System.out.println(user.toString());
        }catch (Exception e){

        }finally {
            rLock.unlock();
        }*/

        /**
         * 过期淘汰 RMapCache
         *
         */
       /* RMapCache<String, User> map = redissonClient.getMapCache("userMap");
        // 有效时间 ttl = 10分钟
        map.put("1", new User(1,"user1"), 10, TimeUnit.MINUTES);
        // 有效时间 ttl = 10分钟, 最长闲置时间 maxIdleTime = 10秒钟
        map.put("2", new User(2,"user2"), 10, TimeUnit.MINUTES, 10, TimeUnit.SECONDS);

        // 有效时间 = 3 秒钟
        map.putIfAbsent("3", new User(3,"user3"), 3, TimeUnit.SECONDS);
        // 有效时间 ttl = 40秒钟, 最长闲置时间 maxIdleTime = 10秒钟
        map.putIfAbsent("4", new User(4,"user4"), 40, TimeUnit.SECONDS, 10, TimeUnit.SECONDS);
*/

     /*   LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
                // 用于淘汰清除本地缓存内的元素
                // 共有以下几种选择:
                // LFU - 统计元素的使用频率，淘汰用得最少（最不常用）的。
                // LRU - 按元素使用时间排序比较，淘汰最早（最久远）的。
                // SOFT - 元素用Java的WeakReference来保存，缓存元素通过GC过程清除。
                // WEAK - 元素用Java的SoftReference来保存, 缓存元素通过GC过程清除。
                // NONE - 永不淘汰清除缓存元素。
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LFU)
                // 如果缓存容量值为0表示不限制本地缓存容量大小
                .cacheSize(100)
                // 以下选项适用于断线原因造成了未收到本地缓存更新消息的情况。
                // 断线重连的策略有以下几种：
                // CLEAR - 如果断线一段时间以后则在重新建立连接以后清空本地缓存
                // LOAD - 在服务端保存一份10分钟的作废日志
                //        如果10分钟内重新建立连接，则按照作废日志内的记录清空本地缓存的元素
                //        如果断线时间超过了这个时间，则将清空本地缓存中所有的内容
                // NONE - 默认值。断线重连时不做处理。
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE)
                // 以下选项适用于不同本地缓存之间相互保持同步的情况
                // 缓存同步策略有以下几种：
                // INVALIDATE - 默认值。当本地缓存映射的某条元素发生变动时，同时驱逐所有相同本地缓存映射内的该元素
                // UPDATE - 当本地缓存映射的某条元素发生变动时，同时更新所有相同本地缓存映射内的该元素
                // NONE - 不做任何同步处理
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
                // 每个Map本地缓存里元素的有效时间，默认毫秒为单位
                .timeToLive(10000)
                // 或者
                .timeToLive(10, TimeUnit.SECONDS)
                // 每个Map本地缓存里元素的最长闲置时间，默认毫秒为单位
                .maxIdle(10000)
                // 或者
                .maxIdle(10, TimeUnit.SECONDS);

        RLocalCachedMap<String,Object> cacheMap = redissonClient.getLocalCachedMap("cacheMap", options);
        //可返回旧值
        cacheMap.put("222",102);
        cacheMap.put("333","888888");
        cacheMap.put("111",true);

        cacheMap.remove("111");
        cacheMap.fastPut("3333","ttttt");

        cacheMap.destroy();
        *//**
         * fastPutIfAbsent
         * fastRemove
         * putAsync
         * fastPutAsync
         * fastRemoveAsync
         *
         */


        /**
         * 多值映射
         * 用起来相对比较容易，但是当同一个key或者list中的值比较多的情况，获取全部，或者更新，都是需要获取key对应的全部值，非常消耗性能
         */
       /* RSetMultimap<String, User> setMulti = redissonClient.getSetMultimap("setMulti");
        setMulti.put("111",new User(1,"user1",1));
        setMulti.put("111",new User(2,"user2",2));
        setMulti.put("111",new User(3,"user3",3));
        setMulti.put("111",new User(1,"user1",1));
        setMulti.put("222",new User(1,"user1",1));

        RSet<User> users = setMulti.get("111");
        List<User> list = Arrays.asList(new User(4,"user4",4),new User(5,"user5",5));
        Set<User> oldValues = setMulti.replaceValues("111", list);
        setMulti.removeAll("111");*/
        //getListMultimap list的操作是和set一模一样的


        /**
         * 实现一个有序集合
         */
        /*RSortedSet<User> sortSet = redissonClient.getSortedSet("sortSet");
        sortSet.trySetComparator(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getAge() - o2.getAge();
            }
        });
        List<User> list = Arrays.asList(new User(4,"user4",22),new User(5,"user5",12),new User(6,"user6",60));
        sortSet.addAll(list);*/

        //这种排序操作带上权重的话，就可以到前面去啦
        //排名从0开始，poll就直接删除了，返回删除的值


        /**
         * 列表
         */
        //确保了元素插入时的顺序
       /* RList<Object> rlist = redissonClient.getList("rlist");
        rlist.add(0);
        rlist.remove(0);
        RQueue<Object> rqueue = redissonClient.getQueue("rqueue");
        rqueue.add(1);
        rqueue.poll();
        RDeque<Object> rdeque = redissonClient.getDeque("rdeque");
        rdeque.addFirst(2);
        rdeque.addLast(3);
        Object o = rdeque.removeFirst();
        Object o1 = rdeque.removeLast();

        //poll, pollFromAny, pollLastAndOfferFirstTo和take方法内部采用话题订阅发布实现
        RBlockingDeque<Object> rblockingqueue = redissonClient.getBlockingDeque("rblockingqueue");
        rblockingqueue.offer(4);
        Object peek = rblockingqueue.peek();
        Object poll = rblockingqueue.poll();
        //阻塞10秒
        Object poll1 = rblockingqueue.poll(10, TimeUnit.SECONDS);

        //poll, pollFromAny, pollLastAndOfferFirstTo和take方法内部采用话题订阅发布实现
        RBoundedBlockingQueue<Object> rboundblockqueue = redissonClient.getBoundedBlockingQueue("rboundblockqueue");

        //poll, pollFromAny, pollLastAndOfferFirstTo和take方法内部采用话题订阅发布实现
        RBlockingDeque<Object> rblockingdeque = redissonClient.getBlockingDeque("rblockingdeque");
*/
        //阻塞公平锁，没看到API ？？RBlockingFairQueue，RBlockingFairDeque 


        /**
         * 锁：
         * 可重入锁 ：Reentrant Lock
         */
        /*RLock lock = redissonClient.getLock("testMap");
        lock.lock(10, TimeUnit.SECONDS);
        //异步方式
        lock.lockAsync();
        *//**
         * 以上是最普通的锁，锁住一个map
         * 锁给我们分布式操作带来便利的同时，在节点宕机的时候，这个锁可能带来灾难，永远锁住这个map
         * redisson提供了看门狗 watchdog，它会将锁在宕机前尽量都释放，监控时间默认是30s，Config.lockWatchdogTimeout
         *//*
        //1000秒内尝试获取锁,锁的有效期10秒
        boolean b = lock.tryLock(100, 10, TimeUnit.SECONDS);
        //异步方式
        boolean b1 = lock.tryLock(100, 10, TimeUnit.SECONDS);
        lock.unlock();*/

       /* RLock fairLock = redissonClient.getFairLock("testMap");
        fairLock.lock();*/
        //通过公平锁也有异步方式


    }
}
