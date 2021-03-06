目标搭建哨兵2个，一个主一个从存储节点
```text
redis1 		--- 主 192.168.35.127 6379
redis1 		--- 从 192.168.35.127 6380
sential1 	--- 主 192.168.35.127 26379
sential2 	--- 从 192.168.35.127 26380
```
### 首先 --- 下载
- 试过3.x 4.x 现在最新稳定版是5.0.4就拿这个试试了
- 比较寒碜，就一台机子，模拟跑一跑

### 其次 --- 按照4个节点
1.一个压缩包redis-5.0.4.tar.gz
2.解压
```text
tar -zxvf redis-5.0.4.tar.gz /usr/local/cache
```
3.重命名，因为这样解压大家都是redis-5.0.4这个名字
```text
mv redis-5.0.4 redis1
```
4.编译
```text
/redis1/make
.....
接下来就是一段时间的编译，会生成关键的redis启动脚本
```
5.因为需要4个redis节点，因而拷贝三遍redis1，再重命名
```text
最终结果：
drwxrwxr-x 6 root root     309 Apr 19 06:57 redis1
drwxrwxr-x 6 root root     309 Apr 19 06:58 redis2
drwxrwxr-x 6 root root     309 Mar 18 12:21 redis3
drwxrwxr-x 6 root root     309 Mar 18 12:21 redis4
```

### 接着 --- 启动测试主从
1.主节点配置
修改ip即可
2.从节点配置
修改ip port，然后添加slaveof 192.168.35.127 6379
```text
bind 192.168.35.127
slaveof 192.168.35.127 6379
```
3.主节点启动
```text
/redis1/src/redis-server ../redis.conf

[root@node2 src]# ./redis-server  ../redis.conf 
17274:C 19 Apr 2019 07:08:07.990 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
17274:C 19 Apr 2019 07:08:07.990 # Redis version=5.0.4, bits=64, commit=00000000, modified=0, pid=17274, just started
17274:C 19 Apr 2019 07:08:07.990 # Configuration loaded
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 5.0.4 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6379
 |    `-._   `._    /     _.-'    |     PID: 17274
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

17274:M 19 Apr 2019 07:08:07.993 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
17274:M 19 Apr 2019 07:08:07.993 # Server initialized
17274:M 19 Apr 2019 07:08:07.993 # WARNING overcommit_memory is set to 0! Background save may fail under low memory condition. To fix this issue add 'vm.overcommit_memory = 1' to /etc/sysctl.conf and then reboot or run the command 'sysctl vm.overcommit_memory=1' for this to take effect.
17274:M 19 Apr 2019 07:08:07.993 * Ready to accept connections
17274:M 19 Apr 2019 07:08:35.226 * Replica 192.168.35.127:6380 asks for synchronization
17274:M 19 Apr 2019 07:08:35.226 * Full resync requested by replica 192.168.35.127:6380
17274:M 19 Apr 2019 07:08:35.226 * Starting BGSAVE for SYNC with target: disk
17274:M 19 Apr 2019 07:08:35.226 * Background saving started by pid 17393
17393:C 19 Apr 2019 07:08:35.228 * DB saved on disk
17393:C 19 Apr 2019 07:08:35.228 * RDB: 0 MB of memory used by copy-on-write
17274:M 19 Apr 2019 07:08:35.259 * Background saving terminated with success
17274:M 19 Apr 2019 07:08:35.259 * Synchronization with replica 192.168.35.127:6380 succeeded
```
4.从节点启动
```text
[root@node2 src]# ./redis-server ../redis.conf 
17389:C 19 Apr 2019 07:08:35.223 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
17389:C 19 Apr 2019 07:08:35.223 # Redis version=5.0.4, bits=64, commit=00000000, modified=0, pid=17389, just started
17389:C 19 Apr 2019 07:08:35.223 # Configuration loaded
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 5.0.4 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6380
 |    `-._   `._    /     _.-'    |     PID: 17389
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

17389:S 19 Apr 2019 07:08:35.225 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
17389:S 19 Apr 2019 07:08:35.225 # Server initialized
17389:S 19 Apr 2019 07:08:35.225 # WARNING overcommit_memory is set to 0! Background save may fail under low memory condition. To fix this issue add 'vm.overcommit_memory = 1' to /etc/sysctl.conf and then reboot or run the command 'sysctl vm.overcommit_memory=1' for this to take effect.
17389:S 19 Apr 2019 07:08:35.225 * Ready to accept connections
17389:S 19 Apr 2019 07:08:35.225 * Connecting to MASTER 192.168.35.127:6379
17389:S 19 Apr 2019 07:08:35.225 * MASTER <-> REPLICA sync started
17389:S 19 Apr 2019 07:08:35.225 * Non blocking connect for SYNC fired the event.
17389:S 19 Apr 2019 07:08:35.226 * Master replied to PING, replication can continue...
17389:S 19 Apr 2019 07:08:35.226 * Partial resynchronization not possible (no cached master)
17389:S 19 Apr 2019 07:08:35.227 * Full resync from master: 720f8fc85d655dc08b6cb5f05e9ca5e52fb6a5eb:0
17389:S 19 Apr 2019 07:08:35.259 * MASTER <-> REPLICA sync: receiving 175 bytes from master
17389:S 19 Apr 2019 07:08:35.259 * MASTER <-> REPLICA sync: Flushing old data
17389:S 19 Apr 2019 07:08:35.259 * MASTER <-> REPLICA sync: Loading DB in memory
17389:S 19 Apr 2019 07:08:35.259 * MASTER <-> REPLICA sync: Finished with success

```
5.登录客户端，查看一下集群信息
- 主节点上client,info replication
```text
192.168.35.127:6379> info replication
# Replication
role:master 
connected_slaves:1
slave0:ip=192.168.35.127,port=6380,state=online,offset=486808,lag=0
master_replid:720f8fc85d655dc08b6cb5f05e9ca5e52fb6a5eb
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:486808
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:486
```
- info
```text
192.168.35.127:6379> info
# Server
redis_version:5.0.4
redis_git_sha1:00000000
redis_git_dirty:0
redis_build_id:9e22b01d009067e0
redis_mode:standalone
os:Linux 3.10.0-514.6.1.el7.x86_64 x86_64
arch_bits:64
multiplexing_api:epoll
atomicvar_api:atomic-builtin
gcc_version:4.8.5
process_id:17274
run_id:c3f1d17b306ff6f63959e49a243e8115630a25af
tcp_port:6379
uptime_in_seconds:348593
uptime_in_days:4
hz:10
configured_hz:10
lru_clock:12516808
executable:/usr/local/cacheinfo/redis1/src/./redis-server
config_file:/usr/local/cacheinfo/redis1/redis.conf

# Clients
connected_clients:1
client_recent_max_input_buffer:2
client_recent_max_output_buffer:0
blocked_clients:0

# Memory
used_memory:1923880
used_memory_human:1.83M
used_memory_rss:4390912
used_memory_rss_human:4.19M
used_memory_peak:1923880
used_memory_peak_human:1.83M
used_memory_peak_perc:100.07%
used_memory_overhead:1906400
used_memory_startup:791208
used_memory_dataset:17480
used_memory_dataset_perc:1.54%
allocator_allocated:2326952
allocator_active:2580480
allocator_resident:11517952
total_system_memory:8203079680
total_system_memory_human:7.64G
used_memory_lua:37888
used_memory_lua_human:37.00K
used_memory_scripts:0
used_memory_scripts_human:0B
number_of_cached_scripts:0
maxmemory:0
maxmemory_human:0B
maxmemory_policy:noeviction
allocator_frag_ratio:1.11
allocator_frag_bytes:253528
allocator_rss_ratio:4.46
allocator_rss_bytes:8937472
rss_overhead_ratio:0.38
rss_overhead_bytes:-7127040
mem_fragmentation_ratio:2.33
mem_fragmentation_bytes:2509288
mem_not_counted_for_evict:0
mem_replication_backlog:1048576
mem_clients_slaves:16922
mem_clients_normal:49694
mem_aof_buffer:0
mem_allocator:jemalloc-5.1.0
active_defrag_running:0
lazyfree_pending_objects:0

# Persistence
loading:0
rdb_changes_since_last_save:0
rdb_bgsave_in_progress:0
rdb_last_save_time:1555672115
rdb_last_bgsave_status:ok
rdb_last_bgsave_time_sec:0
rdb_current_bgsave_time_sec:-1
rdb_last_cow_size:258048
aof_enabled:0
aof_rewrite_in_progress:0
aof_rewrite_scheduled:0
aof_last_rewrite_time_sec:-1
aof_current_rewrite_time_sec:-1
aof_last_bgrewrite_status:ok
aof_last_write_status:ok
aof_last_cow_size:0

# Stats
total_connections_received:3
total_commands_processed:347694
instantaneous_ops_per_sec:0
total_net_input_bytes:13480675
total_net_output_bytes:510292
instantaneous_input_kbps:0.02
instantaneous_output_kbps:0.00
rejected_connections:0
sync_full:1
sync_partial_ok:0
sync_partial_err:0
expired_keys:0
expired_stale_perc:0.00
expired_time_cap_reached_count:0
evicted_keys:0
keyspace_hits:0
keyspace_misses:0
pubsub_channels:0
pubsub_patterns:0
latest_fork_usec:331
migrate_cached_sockets:0
slave_expires_tracked_keys:0
active_defrag_hits:0
active_defrag_misses:0
active_defrag_key_hits:0
active_defrag_key_misses:0

# Replication
role:master
connected_slaves:1
slave0:ip=192.168.35.127,port=6380,state=online,offset=486766,lag=0
master_replid:720f8fc85d655dc08b6cb5f05e9ca5e52fb6a5eb
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:486766
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:486766

# CPU
used_cpu_sys:167.423374
used_cpu_user:220.853765
used_cpu_sys_children:0.001750
used_cpu_user_children:0.000000

# Cluster
cluster_enabled:0

# Keyspace
```
6.修改主机绑定
```text
修改redis.conf中的配置bind 0.0.0.0
可以灵活地面对IP修改，或者内外网卡的问题
```

- 以上是主从复制的配置，对于机器之间可以配置SSL MODE或者免密，建议配置SSL MODE

### 然后---开始哨兵的配置
- 哨兵配置
```text
哨兵1
# sentinel monitor mymaster 127.0.0.1 6379 2
sentinel monitor xiangqubaMaster3 192.168.35.127 6379 2

# sentinel down-after-milliseconds mymaster 30000
sentinel down-after-milliseconds xiangqubaMaster3 30000

# sentinel config-epoch mymaster 1
sentinel config-epoch xiangqubaMaster3 1

# sentinel parallel-syncs mymaster 1
sentinel parallel-syncs xiangqubaMaster3 1


哨兵2：
# sentinel monitor mymaster 127.0.0.1 6379 2
sentinel monitor xiangqubaMaster3 192.168.35.127 6379 1

# sentinel down-after-milliseconds mymaster 30000
sentinel down-after-milliseconds xiangqubaMaster3 30000

# sentinel config-epoch mymaster 1
sentinel config-epoch xiangqubaMaster3 1

# sentinel parallel-syncs mymaster 1
sentinel parallel-syncs xiangqubaMaster3 1


两个哨兵初始化都监控主节点
```
- 启动哨兵节点
```text
$ ./redis-sentinel ../sentinel.conf

-- 请注意，redis节点和哨兵节点可以是同一个节点的，但是产线当中，不要放置在一起，哨兵是注重高可用的，如果节点也宕机，哨兵也宕机，那高可用的意义就没有了，风险需要被分散
```

- 哨兵启动
```text
启动一个
[root@node2 src]# ./redis-sentinel ../sentinel.conf &
[2] 16410
[root@node2 src]# 16410:X 23 Apr 2019 08:18:33.979 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
16410:X 23 Apr 2019 08:18:33.979 # Redis version=5.0.4, bits=64, commit=00000000, modified=0, pid=16410, just started
16410:X 23 Apr 2019 08:18:33.979 # Configuration loaded
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 5.0.4 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in sentinel mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26379
 |    `-._   `._    /     _.-'    |     PID: 16410
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

16410:X 23 Apr 2019 08:18:33.981 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
16410:X 23 Apr 2019 08:18:33.981 # Sentinel ID is 605e22eb580a81e3c08800c7a2a0ac250f7c6644
16410:X 23 Apr 2019 08:18:33.981 # +monitor master mymaster 192.168.35.127 6379 quorum 2


再启动一个17219:X 23 Apr 2019 08:20:03.308 # Redis version=5.0.4, bits=64, commit=00000000, modified=0, pid=17219, just started
     17219:X 23 Apr 2019 08:20:03.308 # Configuration loaded
                     _._                                                  
                _.-``__ ''-._                                             
           _.-``    `.  `_.  ''-._           Redis 5.0.4 (00000000/0) 64 bit
       .-`` .-```.  ```\/    _.,_ ''-._                                   
      (    '      ,       .-`  | `,    )     Running in sentinel mode
      |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26380
      |    `-._   `._    /     _.-'    |     PID: 17219
       `-._    `-._  `-./  _.-'    _.-'                                   
      |`-._`-._    `-.__.-'    _.-'_.-'|                                  
      |    `-._`-._        _.-'_.-'    |           http://redis.io        
       `-._    `-._`-.__.-'_.-'    _.-'                                   
      |`-._`-._    `-.__.-'    _.-'_.-'|                                  
      |    `-._`-._        _.-'_.-'    |                                  
       `-._    `-._`-.__.-'_.-'    _.-'                                   
           `-._    `-.__.-'    _.-'                                       
               `-._        _.-'                                           
                   `-.__.-'                                               
     
     17219:X 23 Apr 2019 08:20:03.311 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
     17219:X 23 Apr 2019 08:20:03.311 # Sentinel ID is 4605bd74e7a1420801c561466d7c9890a43da1eb
     17219:X 23 Apr 2019 08:20:03.311 # +monitor master mymaster 192.168.35.127 6379 quorum 1
     
     
能够看到第一个启动的哨兵发现加入的伙伴:
[root@node2 src]# 16410:X 23 Apr 2019 08:20:05.311 * +sentinel sentinel 4605bd74e7a1420801c561466d7c9890a43da1eb 192.168.35.127 26380 @ mymaster 192.168.35.127 6379


其实哨兵的存在，他们互相感知，然后监视master的动向，给master选举备胎

```
- 模拟一次主节点挂机行为：
```text
[root@node2 src]# ./redis-cli  -h 192.168.35.127 -p 6379 shutdown
13090:M 23 Apr 2019 08:22:50.991 # User requested shutdown...
13090:M 23 Apr 2019 08:22:50.991 * Saving the final RDB snapshot before exiting.
13090:M 23 Apr 2019 08:22:50.992 * DB saved on disk
13090:M 23 Apr 2019 08:22:50.992 * Removing the pid file.
13090:M 23 Apr 2019 08:22:50.992 # Redis is now ready to exit, bye bye...
[1]-  Done                    ./redis-server ../redis.conf  (wd: /usr/local/cacheinfo/redis1/src)
(wd now: /usr/local/cacheinfo/redis3/src

```
- 主节点挂机后，我们看一下集群情况
```text

```
- 我们看到，由于我只有两个节点，所以查看info，只能看到只有一个节点，在关注一下哨兵的日志
```text
哨兵1：[root@node2 src]# 16410:X 23 Apr 2019 08:23:21.032 # +sdown master mymaster 192.168.35.127 6379
    16410:X 23 Apr 2019 08:23:21.069 # +new-epoch 1
    16410:X 23 Apr 2019 08:23:21.070 # +vote-for-leader 4605bd74e7a1420801c561466d7c9890a43da1eb 1
    16410:X 23 Apr 2019 08:23:22.083 # +config-update-from sentinel 4605bd74e7a1420801c561466d7c9890a43da1eb 192.168.35.127 26380 @ mymaster 192.168.35.127 6379
    16410:X 23 Apr 2019 08:23:22.083 # +switch-master mymaster 192.168.35.127 6379 192.168.35.127 6380
    16410:X 23 Apr 2019 08:23:22.083 * +slave slave 192.168.35.127:6379 192.168.35.127 6379 @ mymaster 192.168.35.127 6380
    16410:X 23 Apr 2019 08:23:52.156 # +sdown slave 192.168.35.127:6379 192.168.35.127 6379 @ mymaster 192.168.35.127 6380
    
    
说明了6379的master当机了，选举新的leader 将192.168.35.127 6379 master角色授予给@ mymaster 192.168.35.127 6380

看到了结果，我们再看一下哨兵2，两个哨兵之间也是有交流的：

17219:X 23 Apr 2019 08:23:21.066 # +sdown master mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:21.066 # +odown master mymaster 192.168.35.127 6379 #quorum 1/1
17219:X 23 Apr 2019 08:23:21.066 # +new-epoch 1
17219:X 23 Apr 2019 08:23:21.066 # +try-failover master mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:21.068 # +vote-for-leader 4605bd74e7a1420801c561466d7c9890a43da1eb 1
17219:X 23 Apr 2019 08:23:21.070 # 605e22eb580a81e3c08800c7a2a0ac250f7c6644 voted for 4605bd74e7a1420801c561466d7c9890a43da1eb 1
17219:X 23 Apr 2019 08:23:21.140 # +elected-leader master mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:21.140 # +failover-state-select-slave master mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:21.211 # +selected-slave slave 192.168.35.127:6380 192.168.35.127 6380 @ mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:21.211 * +failover-state-send-slaveof-noone slave 192.168.35.127:6380 192.168.35.127 6380 @ mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:21.277 * +failover-state-wait-promotion slave 192.168.35.127:6380 192.168.35.127 6380 @ mymaster 192.168.35.127 6379
13095:M 23 Apr 2019 08:23:21.277 # Setting secondary replication ID to 39a442cd63a92a19bd930081cd6b15995a8af2ac, valid up to offset: 30639. New replication ID is 879b99b315457b8c8297e3d8d2cbaab5fc193c95
13095:M 23 Apr 2019 08:23:21.277 * Discarding previously cached master state.
13095:M 23 Apr 2019 08:23:21.278 * MASTER MODE enabled (user request from 'id=10 addr=192.168.35.127:58050 fd=10 name=sentinel-4605bd74-cmd age=198 idle=0 flags=x db=0 sub=0 psub=0 multi=3 qbuf=140 qbuf-free=32628 obl=36 oll=0 omem=0 events=r cmd=exec')
13095:M 23 Apr 2019 08:23:21.280 # CONFIG REWRITE executed with success.
17219:X 23 Apr 2019 08:23:21.993 # +promoted-slave slave 192.168.35.127:6380 192.168.35.127 6380 @ mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:21.993 # +failover-state-reconf-slaves master mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:22.082 # +failover-end master mymaster 192.168.35.127 6379
17219:X 23 Apr 2019 08:23:22.082 # +switch-master mymaster 192.168.35.127 6379 192.168.35.127 6380
17219:X 23 Apr 2019 08:23:22.082 * +slave slave 192.168.35.127:6379 192.168.35.127 6379 @ mymaster 192.168.35.127 6380
17219:X 23 Apr 2019 08:23:52.112 # +sdown slave 192.168.35.127:6379 192.168.35.127 6379 @ mymaster 192.168.35.127 6380

这个哨兵2的信息貌似比1稍微丰富一点，主节点的失败检测，config rewrite ,切换master角色

```

### 主从加哨兵--高可用，集群--分布式容错
- 哨兵集群+主从的redis组合是比较常用的，redis的主作为写入，从作为读取，分散压力，也能很好地配置资源
- 降低服务器压力,读取数据访问的速度加快，主的数据库会将修改内容同步到从的数据库-数据备份
- 就是因为读库会全部同步主库，存储相对来说会是一个比较消耗的问题，对于主和主互为备份，读和读互为备份，读或写的吞吐受机器数量限制
- 集群的概念和主从的全复制方式会不同，集群是通过分片处理，集群机器对外是一个完整的整体，集群是一组相互独立的、通过高速网络互联的计算机，它们构成了一个组，并以单一系统的模式加以管理
- 一个客户与集群相互作用时，集群像是一个独立的服务器。集群配置是用于提高可用性和可缩放性
- 但是集群也是有问题存在的，就是客户端的操作会相对复杂
- 集群最少需要6个服务器
#### [主从复制原理](https://blog.csdn.net/nuli888/article/details/52136822)
- redis主从配置比较简单，基本就是在从节点配置文件加上:slaveof 127.0.0.1 6379
- 主从就搭建好了redis主从中如果主节点发生故障，不会自动切换，需要借助redis的Sentinel或者keepalive来实现主的故障转移
- 主要是通过master server持久化的rdb文件实现的
- master server 先dump出内存快照文件，然后将rdb文件传给slave server，slave server 根据rdb文件重建内存表
- 复制流程
```text
1、slave server启动连接到master server之后，salve server主动发送SYNC命令给master server

2、master server接受SYNC命令之后，判断，是否有正在进行内存快照的子进程，
如果有，则等待其结束，
否则，fork一个子进程，子进程把内存数据保存为文件，并发送给slave server

3、master server子进程进程做数据快照时，父进程可以继续接收client端请求写数据，此时，父进程把新写入的数据放到待发送缓存队列中

4、slave server 接收内存快照文件之后，清空内存数据，根据接收的快照文件，重建内存表数据结构

5、master server把快照文件发送完毕之后，发送缓存队列中保存的子进程快照期间改变的数据给slave server，slave server做相同处理，保存数据一致性

6、master server 后续接收的数据，都会通过步骤1建立的连接，把数据发送到slave server

注意：
redis2.8之前不支持增量，到2.8之后就支持增量,

2.8之前的版本，如果从节点刚加入或者故障恢复，需要清空自己内存，重做一个完整的快照文件，恢复过程会比较慢
```

#### [集群的原理和流程](https://blog.csdn.net/nuli888/article/details/52136822)
- 上面看到了集群模式必须需要3+3个机器组成
- 3.0之后的功能，至少需要3(Master)+3(Slave)才能建立集群，是无中心的分布式存储架构，可以在多个节点之间进行数据共享，解决了Redis高可用、可扩展等问题
- 将数据自动切分(split)到多个节点
- 当集群中的某一个节点故障时，redis还可以继续处理客户端的请求
- 一个 redis 集群包含 16384 个哈希槽（hash slot），数据库中的每个数据都属于这16384个哈希槽中的一个。集群使用公式 CRC16(key) % 16384 来计算键 key 属于哪个槽。集群中的每一个节点负责处理一部分哈希槽
- 集群中也有主节点和从节点的说法，其中不是完全复制，而是以分片副本的说法存在，和主从复制的思想不同
- redis集群不保证数据的强一致性，在特定的情况下，redis集群会丢失已经被执行过的写命令
- 使用异步复制（asynchronous replication）是redis 集群可能会丢失写命令的其中一个原因，有时候由于网络原因，如果网络断开时间太长，redis集群就会启用新的主节点，之前发给主节点的数据就会丢失
```text
所有的redis节点彼此互联(PING-PONG机制),内部使用二进制协议优化传输速度和带宽

节点的fail是通过集群中超过半数的节点检测失效时才生效

客户端与redis节点直连,不需要中间proxy层.客户端不需要连接集群所有节点,连接集群中任何一个可用节点

redis-cluster把所有的物理节点映射到[0-16383]slot上,cluster 负责维护node<->slot<->value
```
- 节点fail over选举过程
```text
集群中所有master参与,如果半数以上master节点与master节点通信超过(cluster-node-timeout),认为当前master节点挂掉

当集群不可用时,所有对集群的操作做都不可用，收到((error) CLUSTERDOWN The cluster is down)错误

如果集群任意master挂掉,且当前master没有slave.集群进入fail状态

超过半数以上master挂掉，无论是否有slave集群进入fail状态

```

#### 集群模式的搭建
- 准备两台机器，分配端口
```text
server1:
192.168.35.127:7000
192.168.35.127:7001
192.168.35.127:7002
server2：
192.168.35.128:7003
192.168.35.128:7004
192.168.35.128:7005
```
- 准备好目录和安装包解压和make ，步骤同主从，略
- 修改配置文件redis.conf，文件6个文件夹内一致，端口不同
```text
vi redis.conf

port 7000
daemonize yes  
cluster-enabled yes   新的
cluster-config-file nodes.conf   新的
cluster-node-timeout 5000    新的
appendonly yes
```
- 启动6个示例，就正常启动
```text
./redis-server  ../redis.conf  &
```
- 创建集群
```text
 ./redis-trib.rb  create --replicas 1 192.168.35.127:7000 192.168.35.127:7001 192.168.35.127:7002 192.168.35.128:7003 192.168.35.128:7004 192.168.35.128:7005
```
- 提示ruby和rubygems的错误
```text
[root@localhost ~]# yum install gcc gcc-c++ kernel-devel automake autoconf libtool make wget tcl vim ruby rubygems unzip git -y
```
- 缺少redis和ruby的接口
```text
/usr/lib/ruby/site_ruby/1.8/rubygems/custom_require.rb:31:in `gem_original_require': no such file to load -- redis (LoadError)
        from /usr/lib/ruby/site_ruby/1.8/rubygems/custom_require.rb:31:in `require'
        from ./redis-trib.rb:25


命令：gem install redis
```
- 重新发出集群构建命令
```text
>>> Creating cluster
>>> Performing hash slots allocation on 6 nodes...
Using 3 masters:
192.168.35.128:7003
192.168.35.127:7000
192.168.35.128:7004
Adding replica 192.168.35.127:7001 to 192.168.35.128:7003
Adding replica 192.168.35.128:7005 to 192.168.35.127:7000
```
- 测试连接
```text
[root@localhost src]# redis-cli -c -p 7003
127.0.0.1:7003> set key value
-> Redirected to slot [12539] located at 192.168.35.128:7004
OK

[root@localhost src]# redis-cli -c -p 7000
127.0.0.1:7000> get key
-> Redirected to slot [12539] located at 192.168.35.128:7004
"value"
```


### Docker方式运行redis
- docker是比较热门的部署方式，轻量，减少了很多业务工作量，对于维护像数据库、服务这种独立的模块可以单独的管理，就像一个个虚拟机，但是资源做了很好地分配，不需要一个完整的虚机
- 没有redis环境，安装redis节点
```text
docker run -d --name redis-node redis
```
- 运行redis节点
```text
<path-to-config> - Redisson Node的JSON或YAML配置文件路径

docker run -d --network container:redis-node -v <path-to-config>:/opt/redisson-node/redisson.conf redisson/redisson-node
```
- 可以通过JAVA_OPTS来指定JAVA虚拟主机的运行参数
```text
docker run -d --network container:redis-node -e JAVA_OPTS="-Xmx1g" -v <path-to-config>:/opt/redisson-node/redisson.conf redisson/redisson-node
```





### 附加的命令
```text
停止服务：
redis-cli -h 127.0.0.1 -p 6379 shutdown 

启动服务：
./redis-server redis.config

删除某一些key：
redis-cli -a password -p 6389 KEYS “notify_*” | xargs redis-cli -a password -p 6389 DEL
 
```

