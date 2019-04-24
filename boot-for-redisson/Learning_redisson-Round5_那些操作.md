> 测试过程利用单节点连接

> [命令对比操作redis vs redisson](https://yq.aliyun.com/articles/554750?spm=a2c4e.11153940.blogcont554765.124.56b82b19x5i2fe)

### 异步操作（异步流暂时不尝试）
```text
@Autowired
    private RedissonClient redissonClient;

    @Override
    public void run(String... args) throws Exception {
        RAtomicLong testLong = redissonClient.getAtomicLong("testLong");
        testLong.compareAndSet(0,10);
        RFuture<Boolean> booleanRFuture = testLong.compareAndSetAsync(10, 30);
        //适用于jdk1.8 
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
        
        //已经过时了，适用于jdk1.6
        booleanRFuture.addListener(new FutureListener<Boolean>() {
            @Override
            public void operationComplete(Future<Boolean> booleanFuture) throws Exception {
                
            }
        });

        long testLong1 = redissonClient.getAtomicLong("testLong").get();
        System.out.println(testLong1);
```

### 分布式的那些对象-- redisson的对象在这里,含有一些GEO或者累加器这种应用类型
#### RMap
```text
RMap map = redissonClient.getMap("testMap");
        System.out.println(map.getName());
        Object mapkey1 = map.get("mapkey1");
        map.put("mapkey1","1111");
        Object ifAbsent = map.putIfAbsent("mapkey1", "222");
        Map insertMap = new HashMap<>();
        insertMap.put("insert1","11111");
        insertMap.put("insert2","22222");
        map.putAll(insertMap);
        //fastput和put差距在于有没有返回值，fastput比较快哦
        map.fastPut("mapkey2","zzxxxx");
```
#### RKeys
```text
RKeys keys = redissonClient.getKeys();
        System.out.println(keys.count());
        Iterable<String> foundedKeys = keys.getKeysByPattern("test*");
        long numOfDeletedKeys = keys.delete("obj1", "obj2", "obj3");
        long deletedKeysAmount = keys.deleteByPattern("test?");
        String randomKey = keys.randomKey();
```
###  RBucket -- 通用对象桶
```text
 /**
         * 通用对象桶
         */
        RBucket<User> bucket = redissonClient.getBucket("testObject");
        bucket.set(new User(1,"hello"));
        User user = bucket.get();
        //if empty,现在不是empty ,无效
        bucket.trySet(new User(2,"world"));
        //不满足预期值，无效
        bucket.compareAndSet(new User(2,"world"),new User(3,"bucket"));
        //返回旧值，成功
        User bucketAndSet = bucket.getAndSet(new User(4, "flow"));
        System.out.println(bucketAndSet.toString());
```

### RGeo -- 地理空间桶
```text
 /**
         * 地理空间对象桶（Geospatial Bucket）
         * 这个其实结合动态热点的地理位置，并作实时位点成图是比较有意义的
         * 实时数据成图
         * 离你最近的xxx，离你最近的车站
         * 离你一公里以内的出租车。。。
         * 共享地理位置，你们两个相距多远，最短路径。。。
         * 。。。。
         */
        RGeo<String>  geo = redissonClient.getGeo("infoGeo");
        //加入一些位点信息
        geo.add(new GeoEntry(13.361389, 38.115556, "Palermo"),
                new GeoEntry(15.087269, 37.502669, "Catania"));
        geo.addAsync(37.618423, 55.751244, "Moscow");

        /**
         * 获取两点之间的距离
         */
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

        //可以传入对象信息，获得更多内容
        RGeo<User> userRGeo = redissonClient.getGeo("userGeo");
        userRGeo.add(new GeoEntry(13.361389, 38.115556, new User(1,"user1")),
                new GeoEntry(15.087269, 37.502669, new User(2,"user2")));
        Double dist1 = userRGeo.dist(new User(1, "user1"), new User(2, "user2"), GeoUnit.METERS);
        System.out.println(dist1);

```
### BitSet -- 数据分片
- 基于Redis的Redisson集群分布式BitSet通过RClusteredBitSet接口，为集群状态下的Redis环境提供了BitSet数据分片的功能
- 不知道应用场景是啥？

### AtomicLong AtomicDouble -- 原子数据类型
```text
        RAtomicLong atomicLong = redissonClient.getAtomicLong("atomicLong");
        long l = atomicLong.incrementAndGet();
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble("atomicDouble");
        double andIncrement = atomicDouble.getAndIncrement();
        System.out.println(l);
```

### RTopic -- 话题订阅
- 注意一点，发消息需要双引号的转义，不然会出现异常：JsonParseException: Unrecognized token 'xxxx': was expecting ('true', 'false' or 'null')
```text
 /**
         * 话题订阅
         * 这个在redis client的时候，利用原生的命令也尝试过，就是一个简单的消息发送和订阅，订阅可以多方，可以模糊
         */
        RTopic<String> testToic = redissonClient.getTopic("testToic");
        long publish = testToic.publish("\"infomation\"");

        testToic.addListener((channel, msg) -> System.out.println("信道："+channel.toString() +"收到消息:"+msg));

        RPatternTopic<String> patternTopic = redissonClient.getPatternTopic("test*");
        patternTopic.addListener(new PatternMessageListener<String>() {
            @Override
            public void onMessage(CharSequence pattern, CharSequence channel, String msg) {
                System.out.println("pattern :" + pattern.toString() +"信道："+channel.toString() +"收到消息:"+msg);
            }
        });

pattern :test*信道：test1channel收到消息:unbelivable info
pattern :test*信道：test1收到消息:hello world
```

### Bloom Filter -- 布隆过滤器
- 缓存所有正确的key，然后对来访数据进行过滤
```text
   /**
         * 布隆过滤器：这个想必大家都有听说过缓存穿透，这是redis的两大问题之一，提到的解决办法就有实现布隆过滤器，过滤不合法的值，从而避免这种风险
         * Redisson利用Redis实现了Java分布式布隆过滤器（Bloom Filter）
         */
        RBloomFilter<User> testBloom = redissonClient.getBloomFilter("testBloom");
        testBloom.tryInit(100000L, 0.03);
        testBloom.add(new User(1, "user1"));
        testBloom.add(new User(2, "user2"));
        boolean user3 = testBloom.contains(new User(3, "user3"));
        boolean user2 = testBloom.contains(new User(2, "user2"));
        //集群版本的布隆过滤器：getClusteredBloomFilter，操作是一样的
```

### HyperLogLog -- 基数估算
- 这个在原生命令中就有提及，算是一个用处，但是具体的应用场景还有待思考
```text
xxx = getHyperLogLog
xxx.add(1)
xxx.add(2)

xxx.count()
```

### LongAdder DoubleAdder -- 累加器
```text
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
        RLongAdder longAdder = redissonClient.getLongAdder("longAdder");
        longAdder.add(1);
        longAdder.increment();
        longAdder.increment();
        longAdder.add(1000);
        longAdder.decrement();
        longAdder.add(23);
        long sum = longAdder.sum();
        longAdder.reset();
        longAdder.destroy();
```
### RateLimiter -- 分布式对象
```text
 /**
         * 限流器：
         * 基于Redis的分布式限流器可以用来在分布式环境下现在请求方的调用频率
         * 不保证公平
         */
        RRateLimiter limiter = redissonClient.getRateLimiter("limiter");
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
        RFuture<Void> voidRFuture = limiter.acquireAsync();

```

### 分布式集合之映射Map-- 我们通常运用都会使用集合数据类型
#### 第一个大头RMap，存放字符串,用处可以说是最广的
这个K-V也可以存对象用
```text
 /**
         * Map -- RMAP
         * 分布式映射结构的RMap Java对象实现了java.util.concurrent.ConcurrentMap接口和java.util.Map接口
         * 保持了元素的插入顺序
         * 最大元素数量是4 294 967 295个
         *
         */
        RMap<String, String > testMap = redissonClient.getMap("testMap");
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
                }

```
### 映射（Map）的元素淘汰（Eviction），本地缓存（LocalCache）和数据分片（Sharding）
- 元素淘汰（Eviction） 类 -- 带有元素淘汰（Eviction）机制的映射类允许针对一个映射中每个元素单独设定 有效时间 和 最长闲置时间
也就是我们常用的，对key过期
- 本地缓存（LocalCache） 类 -- 本地缓存（Local Cache）也叫就近缓存（Near Cache）
也就是我们经常用的对频繁读写的数据进行本地缓存，避免网络小号
- 数据分片（Sharding） 类 -- 数据分片（Sharding）类仅适用于Redis集群环境下，因此带有数据分片（Sharding）功能的映射也叫集群分布式映射
- 一些集群的方法我们平常用不上，我们会RMap RMapCache RLocalCacheMap 三种，分别是普通，过期支持，本地缓存支持
```text
                                   Local Cache	数据分片功能      Sharding	  元素淘汰功能Eviction
RMap映射	            getMap()                  No                  	No          	No
RMapCache映射缓存	getMapCache()             No                	No          	Yes
RLocalCachedMap     getLocalCachedMap()       Yes               	No          	No
本地缓存映射	
 	
RLocalCachedMap    getLocalCachedMapCache()   Yes               	No          	Yes
Cache
本地缓存映射缓存
仅限于Redisson PRO版本		
```

### RMapCache -- 元素淘汰（Eviction）
- 针对单个元素的淘汰机制,保留了元素的插入顺序
- 继承了java.util.concurrent.ConcurrentMap接口和java.util.Map接口
- redis有对应的过期策略，懒汉，即时删除，定时删除，定期删除，一般都会采用定期+懒汉的方式
- 业务当中，可以使用带过期时间的特性存储：各种验证码，session，各种限时验证。。。。
```text
 /**
         * 过期淘汰 RMapCache
         *
         */
        RMapCache<String, User> map = redissonClient.getMapCache("userMap");
        // 有效时间 ttl = 10分钟
        map.put("1", new User(1,"user1"), 10, TimeUnit.MINUTES);
        // 有效时间 ttl = 10分钟, 最长闲置时间 maxIdleTime = 10秒钟
        map.put("2", new User(2,"user2"), 10, TimeUnit.MINUTES, 10, TimeUnit.SECONDS);

        // 有效时间 = 3 秒钟
        map.putIfAbsent("3", new User(3,"user3"), 3, TimeUnit.SECONDS);
        // 有效时间 ttl = 40秒钟, 最长闲置时间 maxIdleTime = 10秒钟
        map.putIfAbsent("4", new User(4,"user4"), 40, TimeUnit.SECONDS, 10, TimeUnit.SECONDS);

  
```

### RLocalCachedMap
- 基本的设置
```text
   LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
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
        /**
         * fastPutIfAbsent
         * fastRemove
         * putAsync
         * fastPutAsync
         * fastRemoveAsync
         *
         */
```
- 我们就是希望通过本地缓存来实现缓存的需求，怎么做到呢？
```text
public void loadData(String cacheName, Map<String, String> data) {
    RLocalCachedMap<String, String> clearMap = redisson.getLocalCachedMap(cacheName, 
            LocalCachedMapOptions.defaults().cacheSize(1).syncStrategy(SyncStrategy.INVALIDATE));
    RLocalCachedMap<String, String> loadMap = redisson.getLocalCachedMap(cacheName, 
            LocalCachedMapOptions.defaults().cacheSize(1).syncStrategy(SyncStrategy.NONE));
    
    loadMap.putAll(data);
    clearMap.clearLocalCache();
}
```
### 数据分片操作主要涉及到集群操作，针对哈希槽上数据的问题
getClusteredMap

### 映射的持久化 -- RMap、RMapCache、RLocalCachedMap和RLocalCachedMapCache
```text
MapOptions<K, V> options = MapOptions.<K, V> defaults().writer(myWriter).loader(myLoader);

RMap<K, V> map = redisson.getMap("test", options);
// 或
RMapCache<K, V> map = redisson.getMapCache("test", options);
// 或
RLocalCachedMap<K, V> map = redisson.getLocalCachedMap("test", options);
// 或
RLocalCachedMapCache<K, V> map = redisson.getLocalCachedMapCache("test", options);
```
### 对于映射可以添加监听器，监听添加、删除、过期、更新事件
```text
RMapCache<String, Integer> map = redisson.getMapCache("myMap");
// 或
RLocalCachedMapCache<String, Integer> map = redisson.getLocalCachedMapCache("myMap", options);

int updateListener = map.addListener(new EntryUpdatedListener<Integer, Integer>() {
     @Override
     public void onUpdated(EntryEvent<Integer, Integer> event) {
          event.getKey(); // 字段名
          event.getValue() // 新值
          event.getOldValue() // 旧值
          // ...
     }
});

int createListener = map.addListener(new EntryCreatedListener<Integer, Integer>() {
     @Override
     public void onCreated(EntryEvent<Integer, Integer> event) {
          event.getKey(); // 字段名
          event.getValue() // 值
          // ...
     }
});

int expireListener = map.addListener(new EntryExpiredListener<Integer, Integer>() {
     @Override
     public void onExpired(EntryEvent<Integer, Integer> event) {
          event.getKey(); // 字段名
          event.getValue() // 值
          // ...
     }
});

int removeListener = map.addListener(new EntryRemovedListener<Integer, Integer>() {
     @Override
     public void onRemoved(EntryEvent<Integer, Integer> event) {
          event.getKey(); // 字段名
          event.getValue() // 值
          // ...
     }
});

map.removeListener(updateListener);
map.removeListener(createListener);
map.removeListener(expireListener);
map.removeListener(removeListener);
```

### 分布式集合之多值映射（Multimap）
- RMultimap  每个Multimap最多允许有4 294 967 295个不同字段
- 多值映射在业务场景中，可以存储某一个对象绑定的一些上下文，比如一条说说绑定一些评论，一些聊天框的上下文几条信息
- 可以基于set ,可以基于list ,关键在于有没有重复值
- 测试用例
```text
  /**
         * 多值映射
         * 用起来相对比较容易，但是当同一个key或者list中的值比较多的情况，获取全部，或者更新，都是需要获取key对应的全部值，非常消耗性能
         */
        RSetMultimap<String, User> setMulti = redissonClient.getSetMultimap("setMulti");
        setMulti.put("111",new User(1,"user1"));
        setMulti.put("111",new User(2,"user2"));
        setMulti.put("111",new User(3,"user3"));
        setMulti.put("111",new User(1,"user1"));
        setMulti.put("222",new User(1,"user1"));

        RSet<User> users = setMulti.get("111");
        List<User> list = Arrays.asList(new User(4,"user4"),new User(5,"user5"));
        Set<User> oldValues = setMulti.replaceValues("111", list);
        setMulti.removeAll("111");
        //getListMultimap list的操作是和set一模一样的
```

#### 多值映射设置过期值 
```text
RSetMultimapCache<String, String> multimap = redisson.getSetMultimapCache("multimap");
multimap.put("1", "a");
multimap.expireKey("2", 10, TimeUnit.MINUTES);
```

### 分布式集合Set 
- RSet Java对象实现了java.util.Set接口。通过元素的相互状态比较保证了每个元素的唯一性
- set的基本操作和Map是类似的，特点就是里面元素的唯一性
```text
RSet<Object> set = redisson.getSet("rset");

set.add(111);
set.remove("222");
set.add("333", 10, TimeUnit.SECONDS);
```

### 分布式集合 之 有序集合 xxxSortedSet
#### SortedSet
- 这个有序集合，有序是根据比较器，对元素进行排序，可以传入比较器
```text
RSortedSet<Integer> sset = redisson.getSortedSet("sset");
set.trySetComparator(new MyComparator()); // 配置元素比较器
set.add(3);

set.removeAsync(0);
set.addAsync(5);
```
#### 计分排序集（ScoredSortedSet）
- 给元素添加权重
```text

        /**
         * 实现一个有序集合
         */
        RSortedSet<User> sortSet = redissonClient.getSortedSet("sortSet");
        sortSet.trySetComparator(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getAge() - o2.getAge();
            }
        });
        List<User> list = Arrays.asList(new User(4,"user4",22),new User(5,"user5",12),new User(6,"user6",60));
        sortSet.addAll(list);

        //这种排序操作带上权重的话，就可以到前面去啦
        //排名从0开始，poll就直接删除了，返回删除的值
        RScoredSortedSet<User> sortSet = redissonClient.getScoredSortedSet("scoreSortSet");
        sortSet.add(0.5,new User(4,"user4",2));
        sortSet.add(0.3,new User(5,"user5",12));
        sortSet.add(0.8,new User(6,"user6",60));
        sortSet.add(0.6,new User(7,"user7",112));
        sortSet.add(0.1,new User(8,"user8",11));
        Collection<User> users = sortSet.pollFirst(2);
        User user = sortSet.pollLast();
        Integer user4 = sortSet.rank(new User(4, "user4", 2));
        Double user41 = sortSet.getScore(new User(4, "user4", 2));
```


### 字典排序集（LexSortedSet）所有字符串元素按照字典顺序排列
```text
RLexSortedSet set = redisson.getLexSortedSet("simple");
set.add("d");
set.addAsync("e");
set.add("f");

set.lexRangeTail("d", false);
set.lexCountHead("e");
set.lexRange("d", true, "z", false);
```


### 分布式集合之list/Queue/Deque/Blocking Queue/Bounded Blocking Queue/Blocking Deque/Blocking Fair Queue/Blocking Fair Deque/Delayed Queue/Priority Queue/Priority Deque/Priority Blocking Queue/Priority Blocking Deque
- 这个就是涉及到比较复杂的数据结构，用他们和java里面用那些数据结构都差不多，就是实现了分布式
- 一般我们用到的都不多
```text
  /**
         * 列表
         */
        //确保了元素插入时的顺序
        RList<Object> rlist = redissonClient.getList("rlist");
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

        //优先级的含义是通过比较产生的优先，queue和deque都承接着原始的操作，带有可阻塞功能
        RPriorityQueue<User> pqueue = redissonClient.getPriorityQueue("pqueue");
        pqueue.trySetComparator((o11, o2) -> o11.getAge() - o2.getAge());

        RPriorityDeque<User> pdeque = redissonClient.getPriorityDeque("pdeque");
        RPriorityBlockingQueue<User> pbqueue = redissonClient.getPriorityBlockingQueue("pbqueue");
        RPriorityBlockingDeque<User> pbdeque = redissonClient.getPriorityBlockingDeque("pbdeque");

```

### 分布式锁 同步器
#### Reentrant Lock -- 可重入锁
```text
        /**
         * 锁：
         * 可重入锁 ：Reentrant Lock
         */
        RLock lock = redissonClient.getLock("testMap");
        lock.lock(10, TimeUnit.SECONDS);
        //异步方式
        lock.lockAsync();
        /**
         * 以上是最普通的锁，锁住一个map
         * 锁给我们分布式操作带来便利的同时，在节点宕机的时候，这个锁可能带来灾难，永远锁住这个map
         * redisson提供了看门狗 watchdog，它会将锁在宕机前尽量都释放，监控时间默认是30s，Config.lockWatchdogTimeout
         */
        //1000秒内尝试获取锁,锁的有效期10秒
        boolean b = lock.tryLock(100, 10, TimeUnit.SECONDS);
        //异步方式
        boolean b1 = lock.tryLock(100, 10, TimeUnit.SECONDS);
        lock.unlock();
```

#### FairLock -- 公平锁
```text
 RLock fairLock = redissonClient.getFairLock("testMap");
        fairLock.lock();
        //通过公平锁也有异步方式
```

#### MultiLock -- 联锁
- 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例
- 这个是挺高级的。。。但是可能没有机会在什么场景遇到
```text
RLock lock1 = redissonInstance1.getLock("lock1");
RLock lock2 = redissonInstance2.getLock("lock2");
RLock lock3 = redissonInstance3.getLock("lock3");

RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
// 同时加锁：lock1 lock2 lock3
// 所有的锁都上锁成功才算成功。
lock.lock();
...
lock.unlock();
```

#### Red Lock -- 红锁 -- 实现Redlock算法
```text
RLock lock1 = redissonInstance1.getLock("lock1");
RLock lock2 = redissonInstance2.getLock("lock2");
RLock lock3 = redissonInstance3.getLock("lock3");

RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
// 同时加锁：lock1 lock2 lock3
// 红锁在大部分节点上加锁成功就算成功。
lock.lock();
...
lock.unlock();
```

#### ReadWriteLock -- 读写锁
- 该对象允许同时有多个读取锁，但是最多只能有一个写入锁
```text
RReadWriteLock rwlock = redisson.getLock("anyRWLock");
// 最常见的使用方法
rwlock.readLock().lock();
// 或
rwlock.writeLock().lock();
```
#### Semaphore -- 信号量
- 获取信号量，释放信号量，尝试获取信号量
```text
RSemaphore semaphore = redisson.getSemaphore("semaphore");
semaphore.acquire();
//或
semaphore.acquireAsync();
semaphore.acquire(23);
semaphore.tryAcquire();
//或
semaphore.tryAcquireAsync();
semaphore.tryAcquire(23, TimeUnit.SECONDS);
//或
semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
semaphore.release(10);
semaphore.release();
//或
semaphore.releaseAsync();
```
- PermitExpirableSemaphore --- 可过期性信号量 每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放
```text
RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("mySemaphore");
String permitId = semaphore.acquire();
// 获取一个信号，有效期只有2秒钟。
String permitId = semaphore.acquire(2, TimeUnit.SECONDS);
// ...
semaphore.release(permitId);
```

#### CountDownLatch -- 闭锁
```text
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.trySetCount(1);
latch.await();

// 在其他线程或其他JVM里
RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
latch.countDown();
```

