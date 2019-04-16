> redisson主要通过[redisson学习指南](https://yq.aliyun.com/articles/554765?spm=a2c4e.11155435.0.0.53db56a3uYBzsp)进行

> 需要了解redis及其原理，需要了解原生redis操作，再来看redisson的封装，才能看出其中韵味

> 版本redis 2.6.10

### 尝试redis基础命令
redis的基本数据类型：
- 字符串
- 哈希表
- 集合
- 列表
- 有序集合
- 地理位置
- 位图
- HyberLogLog

```text
观察每一个操作的复杂度，基本平均为O(1),这也是为什么redis是单线程也能那么快的执行操作

这些操作都和操作系统底层指令紧密相关，做到更少的重排，更快的执行

关注复杂度较高的操作，建议不要在生产中随意执行

```


#### 字符串
1.set  0(1) -- 给键设置值，可以覆盖
```text
set key "value "

get key

set key "new" 原来的值会覆盖

2.6.12开始可以使用SET NX /SET XX/SET EX 过期时间秒/SET PX 过期时间毫秒
```

2.SETNX O(1) -- 键不存在进行设置
```text
redis 127.0.0.1:6379> exists job
(integer) 0
redis 127.0.0.1:6379> EXISTS test
(integer) 0
redis 127.0.0.1:6379> SETNX test "claire"
(integer) 1
redis 127.0.0.1:6379> SETNX test "new"  //已存在不会覆盖，返回值0
(integer) 0
redis 127.0.0.1:6379> GET test
"claire"
```

3.SETEX O(1) -- 过期时间 SETEX key 时间 值
```text
redis 127.0.0.1:6379> SETEX exkey 60  10086
OK
redis 127.0.0.1:6379> GET exkey
"10086"
redis 127.0.0.1:6379> TTL exkey
(integer) 17
redis 127.0.0.1:6379> TTL exkey
(integer) -1
redis 127.0.0.1:6379> GET exkey
(nil)
redis 127.0.0.1:6379> SET exkey "value1"
OK
redis 127.0.0.1:6379> GET exkey
"value1"
redis 127.0.0.1:6379> SETEX exkey 60  "value2"
OK
redis 127.0.0.1:6379> GET exkey
"value2"
redis 127.0.0.1:6379> TTL exkey
(integer) 55
```

4.PSETEX O(1) -- 和SETEX一致，将过期时间设置为毫秒级别

5.GET O(1) -- 获取键值


6.GETSET O(1) -- 返回旧值，保存新值

7.STRLEN O(1) -- 查看key对应值的长度
```text
redis 127.0.0.1:6379> SET mykey "hello"
OK
redis 127.0.0.1:6379> get mykey
"hello"
redis 127.0.0.1:6379> STRLEN mykey
(integer) 5
```
不存在则为0

8.APPEND 平均为O(1) -- 如果值是字符串，就将数据追加到后面
```text
redis 127.0.0.1:6379> EXISTS strings
(integer) 0
redis 127.0.0.1:6379> APPEND strings "str1"
(integer) 4
redis 127.0.0.1:6379> APPEND strings "str2"
(integer) 8
redis 127.0.0.1:6379> APPEND strings "str3"
(integer) 12
redis 127.0.0.1:6379> GET strings
"str1str2str3"
```

9.SETRANGE O(1)-O(M) -- SETRANGE KEY 位移量 值：将value设置到指定偏移量上
值会进行覆盖
```text
redis 127.0.0.1:6379> SET greet "hello lily"
OK
redis 127.0.0.1:6379> SETRANGE greet 6 "claire"
(integer) 12
redis 127.0.0.1:6379> GET greet
"hello claire"
```
10.GETRANGE O(N) -- GETRANGE KEY START END 取决于获取的长度
start和end都包括的内容
```text
redis 127.0.0.1:6379> GET greet
"hello claire"
redis 127.0.0.1:6379> GETRANGE greet 2 4
"llo"
```

11.INCR O(1) -- 数字进行递增，可以用于控制主键，避免冲突，不是数字就报错
```text
redis 127.0.0.1:6379> SET hits 1
OK
redis 127.0.0.1:6379> GET hits
"1"
redis 127.0.0.1:6379> INCR  hits
(integer) 2
redis 127.0.0.1:6379> GET hits
"2"
redis 127.0.0.1:6379>
redis 127.0.0.1:6379> SET hitstr "1ss"
OK
redis 127.0.0.1:6379> INCR hitstr
(error) ERR value is not an integer or out of range
```
12.INCRBY O(1) --INCRBY KEY 步长
```text
redis 127.0.0.1:6379> INCRBY hits 4
(integer) 6
```

13.INCRBYFLOAT  O(1) -- 加上浮点数增量
```text
redis 127.0.0.1:6379> INCRBYFLOAT hits 3.4
"9.4"
```

14.DECR key O(1) -- 减一
15.DECRBY key decrement O(1) --- 减N

16.MSET key value O(N) -- MSET 是一个原子性(atomic)操作， 所有给定键都会在同一时间内被设置
原子性操作很重要，一批数据需要同时写入缓存，可以使用这个命令
相同key值会被覆盖
```text
redis 127.0.0.1:6379> MSET date "2012.3.30" time "11:00 a.m." weather "sunny"
OK
redis 127.0.0.1:6379> MGET date time weather
1) "2012.3.30"
2) "11:00 a.m."
3) "sunny"
```


17.MSETNX key value O(N) -- 避免上面值会覆盖的情况而出现的
```text
redis 127.0.0.1:6379> MSETNX date "2012.3.30" time "11:00 a.m." weather "rain"
(integer) 0
redis 127.0.0.1:6379> MGET date time weather
1) "2012.3.30"
2) "11:00 a.m."
3) "sunny"
```
18.MGET O(N) -- 批量获取


#### 哈希表
1.HSET hash field value   O(1)
2.HSETNX O(1)
3.HGET O(1)
4.HEXISTS O(1)
5.HDEL O(1)
6.HLEN O(1)
7.HSTRLEN O(1)
8.HINCRBY O(1)
9.HINCRBYFLOAT O(1)
10.HMSET O(N)
11.HMSETNX O(N)
12.HKEYS O(N)
13.HVALS O(N)
14.HGETALL O(N)
15.HSCAN
```text
redis 127.0.0.1:6379> HSET lists a "value1"
(integer) 1
redis 127.0.0.1:6379> HSET lists b "value2"
(integer) 1
redis 127.0.0.1:6379> HGET lists a
"value1"
redis 127.0.0.1:6379> HGET lists b
"value2"
redis 127.0.0.1:6379> HSETNX lists a "valuenew"
(integer) 0
redis 127.0.0.1:6379> HGET lists a
"value1"
redis 127.0.0.1:6379> HSET lists c "value2"
(integer) 1
redis 127.0.0.1:6379> HGET lists c
"value2"
redis 127.0.0.1:6379> HEXISTS lists c
(integer) 1
redis 127.0.0.1:6379> HDEL lists c
(integer) 1
redis 127.0.0.1:6379> HEXISTS lists c
(integer) 0
redis 127.0.0.1:6379> HLEN lists
(integer) 2
redis 127.0.0.1:6379> HSTRLEN lists a
(error) ERR unknown command 'HSTRLEN'
redis 127.0.0.1:6379> HSETNX counts a 10
(integer) 1
redis 127.0.0.1:6379> HINCRBY counts a 10
(integer) 20
redis 127.0.0.1:6379> HINCRBYFLOAT counts a 1.1
"21.1"
redis 127.0.0.1:6379> HMSET setvalues a "valuea" b "valueb"
OK
redis 127.0.0.1:6379> HMGET setvalues
(error) ERR wrong number of arguments for 'hmget' command
redis 127.0.0.1:6379> HMGET setvalues a b
1) "valuea"
2) "valueb"
redis 127.0.0.1:6379> HKEYS  setvalues
1) "a"
2) "b"
redis 127.0.0.1:6379> HVALUES  setvalues
(error) ERR unknown command 'HVALUES'
redis 127.0.0.1:6379> HVALS  setvalues
1) "valuea"
2) "valueb"
redis 127.0.0.1:6379> HGETALL setvalues
1) "a"
2) "valuea"
3) "b"
4) "valueb"
```

#### 列表
1.LPUSH key value O(1)  注意： 插入到表头 ，key不存在会创建列表
2.LPUSHX key value O(1)  注意： 插入到表头 ，key不存在不会创建列表，什么也不做，这是差别
3.RPUSH key value  O(1) 注意：插入到表尾(最右边)，key不存在会创建列表
4.RPUSHX key value O(1) 注意：插入到表尾(最右边)，key不存在不会创建列表，什么也不做，这是差别
5.LPOP key 头弹出元素，左边
6.RPOP key 尾弹出元素， 右边
7.RPOPLPUSH source destination  O(1) -- 在一个原子时间内，将右边尾元素查看，并将值放入目标的头中，左边
- 注意：如果源和目标是同一个，那么这将是一个列表的旋转，像Collections.rotate()
8.LREM key count value  O(N)， N 为列表的长度 -- 移除从表头到表尾，最先发现的N个 value
9.LLEN key O(1) -- 列表长度
10.LINDEX key index O(N)  --- 遍历列表，返回指定位的值
11.LINSERT key BEFORE|AFTER pivot value O(N) -- 寻找需要插入的点，在对应的位上插入数据
12.LSET key index value 作用在头尾元素上O(1)/其他O(N)
13.LRANGE key start stop O(S+N) -- stop 为-1是代表全部
14.LTRIM key start stop O(N) --- trim是修建的意思-- 表示只留下开始到结束之间的数据
15.BLOP O(1) ---阻塞列表的弹出
16.BROP O(1) ---阻塞列表的弹出
17.BLOP O(1) ---阻塞列表的循环
```text
redis 127.0.0.1:6379> LPUST country "china"
(error) ERR unknown command 'LPUST'
redis 127.0.0.1:6379> LPUSH country "china"
(integer) 1
redis 127.0.0.1:6379> LRANGE country 0 1
1) "china"
redis 127.0.0.1:6379> LPUSH country "usa"
(integer) 2
redis 127.0.0.1:6379> LRANGE country 0 2
1) "usa"
2) "china"
redis 127.0.0.1:6379> LPUSH country "us" "russia"
(integer) 4
redis 127.0.0.1:6379> LRANGE country 0 4
1) "russia"
2) "us"
3) "usa"
4) "china"
redis 127.0.0.1:6379> LRANGE country 0 5
1) "russia"
2) "us"
3) "usa"
4) "china"
redis 127.0.0.1:6379> LRANGE country 0 3
1) "russia"
2) "us"
3) "usa"
4) "china"
redis 127.0.0.1:6379> LRANGE country 0 2
1) "russia"
2) "us"
3) "usa"
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "russia"
2) "us"
3) "usa"
4) "china"
redis 127.0.0.1:6379> RPUSH country "claire"
(integer) 5
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "russia"
2) "us"
3) "usa"
4) "china"
5) "claire"
redis 127.0.0.1:6379> LPOP country
"russia"
redis 127.0.0.1:6379> RPOP country
"claire"
redis 127.0.0.1:6379> RPOPLPUSH country dest
"china"
redis 127.0.0.1:6379> LRANGE dest 0 -1
1) "china"
redis 127.0.0.1:6379> LRANGE country 0 -1
1) "us"
2) "usa"
redis 127.0.0.1:6379> RPUSH country "claire"
(integer) 3
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "us"
2) "usa"
3) "claire"
redis 127.0.0.1:6379> RPOPLPUSH  country country
"claire"
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "claire"
2) "us"
3) "usa"
redis 127.0.0.1:6379> RPOPLPUSH  country country
"usa"
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "usa"
2) "claire"
3) "us"
redis 127.0.0.1:6379> RPUSH country "claire"
(integer) 4
redis 127.0.0.1:6379> LPUSH country "claire"
(integer) 5
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "claire"
2) "usa"
3) "claire"
4) "us"
5) "claire"
redis 127.0.0.1:6379> LREM country 2 claire
(integer) 2
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "usa"
2) "us"
3) "claire"
redis 127.0.0.1:6379> LLEN country
(integer) 3
redis 127.0.0.1:6379> LINDEX country 1
"us"
redis 127.0.0.1:6379> LINSERT country  BEFORE "us" "newinsert"
(integer) 4
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "usa"
2) "newinsert"
3) "us"
4) "claire"
redis 127.0.0.1:6379> LSET country 1 "newset"
OK
redis 127.0.0.1:6379> LRANGE country 0  -1
1) "usa"
2) "newset"
3) "us"
4) "claire"
```


#### 集合
1.SADD key member O(N) -- 无序
2.SISMEMBER key member O(1) -- 判断是否包含成员
3.SMEMBER key O(N)  -- 查看集合所有成员
4.SPOP O(1) -- 随机移除一个集合值 
5.SRANDMEMBER key [count]  O(N)--- 随机获取一位集合值
6.SREM key member [member …] O(N) -- 移除一个或多个元素
7.SMOVE source destination member O(N) -- 移动数据元素，源和目标这边不能够相同，相同就报错，和列表不同
8.SCARD key O(1) -- 返回集合中的数量，做一些统计的时候，会需要使用
9.SSCAN key cursor [MATCH pattern] [COUNT count]
10.SINTER key [key …] O(N*M) -- 两个集合的交集 ,场景就是一些共同的轨迹，共同好友，大家都爱去的地方等等，但是他的复杂度消耗还是比较大的
11.SINTERSTORE destination key [key …] O(N * M)  -- 将上面得到的交集存到另一个集合中，这也是使用场景的一部分
12.SUNION key [key …]  O(N) -- 两个集合的并集
13.SUNIONSTORE destination key [key …] O(N) -- 保存两个集合的并集
14.SDIFF key [key …] O(N) -- 差集就是寻求两个人的不同之处
15.SDIFFSTORE destination key [key …] O(N)  -- 保存差集
```text
redis 127.0.0.1:6379> SADD collection "a" "b" "c"
(integer) 3
redis 127.0.0.1:6379> SMENBERS collection
(error) ERR unknown command 'SMENBERS'
redis 127.0.0.1:6379> SMEmBERS collection
1) "b"
2) "c"
3) "a"
redis 127.0.0.1:6379> SPOP collection
"b"
redis 127.0.0.1:6379> SMEMBERS collection
1) "c"
2) "a"
redis 127.0.0.1:6379> SRANDMEMBER collection 2
1) "a"
2) "c"
redis 127.0.0.1:6379> SMEMBERS collection
1) "c"
2) "a"
redis 127.0.0.1:6379> SREM collection a
(integer) 1
redis 127.0.0.1:6379> SMEMBERS collection
1) "c"
redis 127.0.0.1:6379> SADD collection "aaa"
(integer) 1
redis 127.0.0.1:6379> SADD collection "aa"
(integer) 1
redis 127.0.0.1:6379> SADD collection "bb"
(integer) 1
redis 127.0.0.1:6379> SADD collection "bbb"
(integer) 1
redis 127.0.0.1:6379> SMEMBERS collection
1) "bb"
2) "c"
3) "aaa"
4) "aa"
5) "bbb"
redis 127.0.0.1:6379> SMOVE collection col "aaa"
(integer) 1
redis 127.0.0.1:6379> SMEMBERS col
1) "aaa"
redis 127.0.0.1:6379> SCARD collection
(integer) 4
redis 127.0.0.1:6379> SADD col "b" "bb" "c"
(integer) 3
redis 127.0.0.1:6379> SMEMBERS col
1) "b"
2) "c"
3) "bb"
4) "aaa"
redis 127.0.0.1:6379> SMEMBERS collextion
(empty list or set)
redis 127.0.0.1:6379> SMEMBERS collection
1) "aa"
2) "bb"
3) "bbb"
4) "c"
redis 127.0.0.1:6379> SINTER collection col
1) "c"
2) "bb"
redis 127.0.0.1:6379> SUNION collection col
1) "b"
2) "bb"
3) "aaa"
4) "bbb"
5) "c"
6) "aa"
redis 127.0.0.1:6379> SDIFF collection col
1) "bbb"
2) "aa"
```

#### 有序集合
- 有序集合的使用场景大约是一些临时数据的排行，微信计步排行榜，点赞人员列表（一般最好根据点赞时间是有序的），评论点赞列表（点赞多的评论需要放在上面显示）等等

1.ZADD key score member [[score member] [score member] …] O(M*log(N)) -- 将一个或多个 member 元素及其 score 值加入到有序集 key 当中
- score 值可以是整数值或双精度浮点数
- 有序的插入序列
2.ZSCORE key member O(1) -- 查看某一项的分值
3.ZINCRBY key increment member O(log(N)) -- 给某一项的分值增加固定的量，比如点赞数加一，那这条评论的分值可能不是加一，让它加10，增加它的权重
4.ZCARD key O(1) -- 返回数量
5.ZCOUNT key min max  O(log(N)) -- 计算在指定大小中间的命中数 前后都包含
6.ZRANGE key start stop [WITHSCORES]  O(log(N)+M) --  递增排序
7.ZREVRANGE key start stop [WITHSCORES] O(log(N)+M) -- 递减排序
8.ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]  O(log(N)+M)  -- 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列
9.ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count] O(log(N)+M)  -- 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递减(从小到大)次序排列
10.ZRANK key member O(log(N)) -- 直接获取排名的位数 ，增序排
11.ZREVRANK key member O(log(N)) -- 直接获取排名的位数 ，倒序排
12.ZREM key member [member …] O(M*log(N)) -- 删除成员
13.ZREMRANGEBYRANK key start stop  O(log(N)+M) -- 移除有序集 key 中，指定排名(rank)区间内的所有成员
14.ZREMRANGEBYSCORE key min max   O(log(N)+M)  -- 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
15.ZRANGEBYLEX key min max [LIMIT offset count]  O(log(N)+M) -- 值相同情况：当有序集合的所有成员都具有相同的分值时， 有序集合的元素会根据成员的字典序（lexicographical ordering）来进行排序， 而这个命令则可以返回给定的有序集合键 key 中， 值介于 min 和 max 之间的成员
```text
合法的 min 和 max 参数必须包含 ( 或者 [ ， 其中 ( 表示开区间（指定的值不会被包含在范围之内）， 而 [ 则表示闭区间（指定的值会被包含在范围之内）。

特殊值 + 和 - 在 min 参数以及 max 参数中具有特殊的意义， 其中 + 表示正无限， 而 - 表示负无限
```
16.ZLEXCOUNT key min max  O(log(N))  -- 值相同情况：对于一个所有成员的分值都相同的有序集合键 key 来说， 这个命令会返回该集合中， 成员介于 min 和 max 范围内的元素数量
17.ZREMRANGEBYLEX key min max O(log(N)+M) --- 对于一个所有成员的分值都相同的有序集合键 key 来说， 这个命令会移除该集合中， 成员介于 min 和 max 范围内的所有元素
18.ZSCAN key cursor [MATCH pattern] [COUNT count] 
19.ZUNIONSTORE destination numkeys key [key …] [WEIGHTS weight [weight …]] [AGGREGATE SUM|MIN|MAX]  O(N)+O(M log(M)) -- 这个操作就是两个集合一起操作，最后存进一个集合里
20.ZINTERSTORE destination numkeys key [key …] [WEIGHTS weight [weight …]] [AGGREGATE SUM|MIN|MAX]  O(N*K)+O(M*log(M)) --- 交集存起来，相同的key权重会相加，这就不是简单的交集，能够保有原来的属性，也是比较重要的 
```text
redis 127.0.0.1:6379> ZADD ranks 10 "www.baidu.com" 9 "www.bing.com" 8 "www.qq.com" 1 "www.google.com"
(integer) 4
redis 127.0.0.1:6379> SMEMBERS ranks
(error) ERR Operation against a key holding the wrong kind of value
redis 127.0.0.1:6379> ZRANGE  ranks 0 -1 WITHSCORES
1) "www.google.com"
2) "1"
3) "www.qq.com"
4) "8"
5) "www.bing.com"
6) "9"
7) "www.baidu.com"
8) "10"
redis 127.0.0.1:6379> ZSCORE ranks "www.baidu.com"
"10"
redis 127.0.0.1:6379> ZINCRBY ranks 20 "www.baidu.com"
"30"
redis 127.0.0.1:6379> ZCARD ranks
(integer) 4
redis 127.0.0.1:6379> ZCOUNT ranks 9 10
(integer) 1
redis 127.0.0.1:6379> ZCOUNT ranks 9 20
(integer) 1
redis 127.0.0.1:6379> ZRANGE  ranks 0 -1 WITHSCORES
1) "www.google.com"
2) "1"
3) "www.qq.com"
4) "8"
5) "www.bing.com"
6) "9"
7) "www.baidu.com"
8) "30"
redis 127.0.0.1:6379> ZCOUNT ranks 9 30
(integer) 2
redis 127.0.0.1:6379> ZREVRANGE  ranks 0 -1 WITHSCORES
1) "www.baidu.com"
2) "30"
3) "www.bing.com"
4) "9"
5) "www.qq.com"
6) "8"
7) "www.google.com"
8) "1"
redis 127.0.0.1:6379> ZRANGEBYSCORE  ranks - +\
(error) ERR min or max is not a float
redis 127.0.0.1:6379> ZRANGEBYSCORE  ranks - +
(error) ERR min or max is not a float
redis 127.0.0.1:6379> ZRANGEBYSCORE  ranks -inf  +inf
1) "www.google.com"
2) "www.qq.com"
3) "www.bing.com"
4) "www.baidu.com"
redis 127.0.0.1:6379> ZRANGEBYSCORE  ranks -inf  1-
(error) ERR min or max is not a float
redis 127.0.0.1:6379> ZRANGEBYSCORE  ranks -inf  10
1) "www.google.com"
2) "www.qq.com"
3) "www.bing.com"
redis 127.0.0.1:6379> ZREVRANGEBYSCORE  ranks -inf  +inf
(empty list or set)
redis 127.0.0.1:6379> ZREVRANGEBYSCORE  ranks +inf  -inf
1) "www.baidu.com"
2) "www.bing.com"
3) "www.qq.com"
4) "www.google.com"
redis 127.0.0.1:6379> ZRANK ranks "www.baidu.com"
(integer) 3
redis 127.0.0.1:6379> ZREVRANK ranks "www.baidu.com"
(integer) 0
redis 127.0.0.1:6379> ZREM ranks "www.qq.com"
(integer) 1
redis 127.0.0.1:6379> ZREMRANGEBYRANK  ranks 0 1
(integer) 2
redis 127.0.0.1:6379> ZRANGE  ranks 0 -1 WITHSCORES
1) "www.baidu.com"
2) "30"
redis 127.0.0.1:6379> ZADD ranks 2 "www.test.com"
(integer) 1
redis 127.0.0.1:6379> ZADD ranks 3 "www.domain.com"
(integer) 1
redis 127.0.0.1:6379> ZRANGE  ranks 0 -1 WITHSCORES
1) "www.test.com"
2) "2"
3) "www.domain.com"
4) "3"
5) "www.baidu.com"
6) "30"
redis 127.0.0.1:6379> ZREMREANGEBSCORE  ranks 3 10
(error) ERR unknown command 'ZREMREANGEBSCORE'
redis 127.0.0.1:6379> ZREMRANGEBYSCORE  ranks 3 10
(integer) 1
redis 127.0.0.1:6379> ZRANGE  ranks 0 -1 WITHSCORES
1) "www.test.com"
2) "2"
3) "www.baidu.com"
4) "30"
redis 127.0.0.1:6379> ZZRANGEBYLEX - +
(error) ERR unknown command 'ZZRANGEBYLEX'
redis 127.0.0.1:6379> ZRANGEBYLEX - +
(error) ERR unknown command 'ZRANGEBYLEX'
redis 127.0.0.1:6379> ZADD cola 1 "a" 2 "b" 3 "c" 3 "d"
(integer) 4
redis 127.0.0.1:6379> ZRANGE  cola 0 -1 WITHSCORES
1) "a"
2) "1"
3) "b"
4) "2"
5) "c"
6) "3"
7) "d"
8) "3"
redis 127.0.0.1:6379> ZADD colb 1 "aa" 1 "bb" 1 "cccc" 3 "dddd"
(integer) 4
redis 127.0.0.1:6379> ZUNIONSTORE  cc 2 cola colb WEIGHTS 1 3
(integer) 8
redis 127.0.0.1:6379> ZRANGE  cc 0 -1 WITHSCORES
 1) "a"
 2) "1"
 3) "b"
 4) "2"
 5) "aa"
 6) "3"
 7) "bb"
 8) "3"
 9) "c"
10) "3"
11) "cccc"
12) "3"
13) "d"
14) "3"
15) "dddd"
16) "9"
redis 127.0.0.1:6379> ZINTERSTORE bb 2 cola cc
(integer) 4
redis 127.0.0.1:6379> ZRANGE  bb  0 -1 WITHSCORES
1) "a"
2) "2"
3) "b"
4) "4"
5) "c"
6) "6"
7) "d"
8) "6"
```

#### HyberLogLog  -- 用于对数值的估算 做最为简单的统计，但是并不会存储指定值
```text
HyperLogLog介绍

HyperLogLog 可以接受多个元素作为输入，并给出输入元素的基数估算值：
• 基数：集合中不同元素的数量。比如 {'apple', 'banana', 'cherry', 'banana', 'apple'} 的基数就是 3 。
• 估算值：算法给出的基数并不是精确的，可能会比实际稍微多一些或者稍微少一些，但会控制在合
理的范围之内。
HyperLogLog 的优点是，即使输入元素的数量或者体积非常非常大，计算基数所需的空间总是固定
的、并且是很小的。
在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基
数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。
但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以
HyperLogLog 不能像集合那样，返回输入的各个元素。
```

#### 地理位置  很实用 但是需要版本的支持 对地理实时性监测有要求的可以使用
- 从 >= 3.2.0版本开始，增加了对地理环境的支持，用于简单的地理搜索，对于公园几公里内有可用的出租车，这种临时数据，有不错的应用场景
1.GEOADD key longitude latitude member [longitude latitude member …] -- 添加地理位置
2.GEOPOS key member [member …] -- 获取地理位置
3.GEODIST key member1 member2 [unit] --  两个节点之间的距离
4.GEORADIUS key longitude latitude radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [ASC|DESC] [COUNT count]  O(N+log(M)) -- 距离某一个坐标多少距离之内的点数据
5.GEORADIUSBYMEMBER key member radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [ASC|DESC] [COUNT count]  O(log(N)+M) --
6.GEOHASH key member [member …]  O(log(N))  -- 返回对应的GEOHASH值，这个值在对接其他应用当中可以使用
```text
redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2

redis> GEODIST Sicily Palermo Catania
"166274.15156960039"

redis> GEORADIUS Sicily 15 37 100 km
1) "Catania"

redis> GEORADIUS Sicily 15 37 200 km
1) "Palermo"
2) "Catania"


redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2

redis> GEOPOS Sicily Palermo Catania NonExisting
1) 1) "13.361389338970184"
   2) "38.115556395496299"
2) 1) "15.087267458438873"
   2) "37.50266842333162"
3) (nil)


redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2

redis> GEODIST Sicily Palermo Catania
"166274.15156960039"

redis> GEODIST Sicily Palermo Catania km
"166.27415156960038"

redis> GEODIST Sicily Palermo Catania mi
"103.31822459492736"

redis> GEODIST Sicily Foo Bar
(nil)


redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2

redis> GEORADIUS Sicily 15 37 200 km WITHDIST
1) 1) "Palermo"
   2) "190.4424"
2) 1) "Catania"
   2) "56.4413"

redis> GEORADIUS Sicily 15 37 200 km WITHCOORD
1) 1) "Palermo"
   2) 1) "13.361389338970184"
      2) "38.115556395496299"
2) 1) "Catania"
   2) 1) "15.087267458438873"
      2) "37.50266842333162"

redis> GEORADIUS Sicily 15 37 200 km WITHDIST WITHCOORD
1) 1) "Palermo"
   2) "190.4424"
   3) 1) "13.361389338970184"
      2) "38.115556395496299"
2) 1) "Catania"
   2) "56.4413"
   3) 1) "15.087267458438873"
      2) "37.50266842333162"
      
redis> GEOADD Sicily 13.583333 37.316667 "Agrigento"
(integer) 1

redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2

redis> GEORADIUSBYMEMBER Sicily Agrigento 100 km
1) "Agrigento"
2) "Palermo"

redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2

redis> GEOHASH Sicily Palermo Catania
1) "sqc8b49rny0"
2) "sqdtr74hyu0"

```
#### 位图

#### 数据库

#### 自动过期

#### 事务

#### LUA脚本




