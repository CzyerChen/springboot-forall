> redisson的调用流程
> redisson是一个用于连接redis的java客户端工作，相对于jedis，是一个采用异步模型，大量使用netty promise编程的客户端框架

> 跟随[参考](https://www.iflym.com/index.php/code/201503290001.html),走redisson调用流程


```text
主要有：
从Reddison这个入口看几个对象Config ConnecitonManager

从两个日常操作，看读和写

从一个命令看一条执行流程：RList<String> list = redissonClient.getList("list")  list.size()

```


### Config对象
- 源码
```text
public class Config {
    private SentinelServersConfig sentinelServersConfig;
    private MasterSlaveServersConfig masterSlaveServersConfig;
    private SingleServerConfig singleServerConfig;
    private ClusterServersConfig clusterServersConfig;
    private ReplicatedServersConfig replicatedServersConfig;
    private ConnectionManager connectionManager;
    private int threads = 16;
    private int nettyThreads = 32;
    private Codec codec;
    private ExecutorService executor;
    private boolean referenceEnabled = true;
    private TransportMode transportMode;
    private EventLoopGroup eventLoopGroup;
    private long lockWatchdogTimeout;
    private boolean keepPubSubOrder;
    private boolean decodeInExecutor;
    private boolean useScriptCache;
    private AddressResolverGroupFactory addressResolverGroupFactory;

    public Config() {
        this.transportMode = TransportMode.NIO;
        this.lockWatchdogTimeout = 30000L;
        this.keepPubSubOrder = true;
        this.decodeInExecutor = false;
        this.useScriptCache = false;
        this.addressResolverGroupFactory = new DnsAddressResolverGroupFactory();
    }

}
```
- 结构
```text
                                BaseConfig
                                    |
                                    |
                |-------------------------------------|
        BaseMasterSlaveServerConfig          SingleServerConfig
                 |
                 |
 |-----------------------|-------------------------|                          
 |                       |                         |
SentinelServersConfig  MasterSlaveServersConfig ClusterServersConfig
       
```

### Redisson对象
- 我们创建一个RedissonClient对象就是通过Redisson.create创建的
```text
public class Redisson implements RedissonClient {
    protected final QueueTransferService queueTransferService = new QueueTransferService();
    protected final EvictionScheduler evictionScheduler;
    protected final ConnectionManager connectionManager;
    protected final ConcurrentMap<Class<?>, Class<?>> liveObjectClassCache = PlatformDependent.newConcurrentHashMap();
    protected final Config config;
    protected final SemaphorePubSub semaphorePubSub = new SemaphorePubSub();
    protected final ConcurrentMap<String, ResponseEntry> responses = PlatformDependent.newConcurrentHashMap();

    protected Redisson(Config config) {
        this.config = config;
        Config configCopy = new Config(config);
        this.connectionManager = ConfigSupport.createConnectionManager(configCopy);
        this.evictionScheduler = new EvictionScheduler(this.connectionManager.getCommandExecutor());
    }
    ......
    }
```
- 上面的源码可以看出来，它的初始化需要一个Config,上面已经看到了,再看一下create方法
```text
  public static RedissonClient create() {
        Config config = new Config();
        ((SingleServerConfig)config.useSingleServer().setTimeout(1000000)).setAddress("redis://127.0.0.1:6379");
        return create(config);
    }
   
   
    public static RedissonClient create(Config config) {
        Redisson redisson = new Redisson(config);
        if (config.isReferenceEnabled()) {
            redisson.enableRedissonReferenceSupport();
        }

        return redisson;
    }
 
```

### ConnectionManager对象
- 我们需要去建立一个连接，ConnectionManager对象是必不可少的
```text
                                         ConnectionManager
                                                 |
                                                 |
                                    MasterSlaveConnectionManager
                                                 |
                                                 |
               |---------------------------------|---------------------------------|
      SingleConnectionManager       ClusterConnectionManager              SentinelConnectionManager                                                 
                                               
```

- 源码
```text
public interface ConnectionManager {
    UUID getId();

    CommandSyncService getCommandExecutor();

    PublishSubscribeService getSubscribeService();

    ExecutorService getExecutor();

    URI getLastClusterNode();

    Config getCfg();

    boolean isClusterMode();

    ConnectionEventsHub getConnectionEventsHub();

    boolean isShutdown();

    boolean isShuttingDown();

    IdleConnectionWatcher getConnectionWatcher();

    int calcSlot(String var1);

    int calcSlot(byte[] var1);

    MasterSlaveServersConfig getConfig();

    Codec getCodec();

    Collection<MasterSlaveEntry> getEntrySet();

    MasterSlaveEntry getEntry(int var1);

    MasterSlaveEntry getEntry(InetSocketAddress var1);

    void releaseRead(NodeSource var1, RedisConnection var2);

    void releaseWrite(NodeSource var1, RedisConnection var2);

    RFuture<RedisConnection> connectionReadOp(NodeSource var1, RedisCommand<?> var2);

    RFuture<RedisConnection> connectionWriteOp(NodeSource var1, RedisCommand<?> var2);

    RedisClient createClient(NodeType var1, URI var2, int var3, int var4, String var5);

    RedisClient createClient(NodeType var1, InetSocketAddress var2, URI var3, String var4);

    RedisClient createClient(NodeType var1, URI var2, String var3);

    MasterSlaveEntry getEntry(RedisClient var1);

    void shutdown();

    void shutdown(long var1, long var3, TimeUnit var5);

    EventLoopGroup getGroup();

    Timeout newTimeout(TimerTask var1, long var2, TimeUnit var4);

    InfinitySemaphoreLatch getShutdownLatch();

    RFuture<Boolean> getShutdownPromise();
```
- 我们主要看读写操作，读从从库读，写向主库写
```text
    RFuture<RedisConnection> connectionReadOp(NodeSource var1, RedisCommand<?> var2);

    RFuture<RedisConnection> connectionWriteOp(NodeSource var1, RedisCommand<?> var2);
    
     public RFuture<RedisConnection> connectionWriteOp(NodeSource source, RedisCommand<?> command) {
            MasterSlaveEntry entry = this.getEntry(source);
            if (entry == null) {
                RedisNodeNotFoundException ex = new RedisNodeNotFoundException("Node: " + source + " hasn't been discovered yet");
                return RedissonPromise.newFailedFuture(ex);
            } else {
                return source.getRedirect() != null && !URIBuilder.compare(entry.getClient().getAddr(), source.getAddr()) && entry.hasSlave(source.getAddr()) ? entry.redirectedConnectionWriteOp(command, source.getAddr()) : entry.connectionWriteOp(command);
            }
        }
    
    
   #### 未连接
    一个写入操作，首先根据连接信息，获取一个客户端连接对象MasterSlaveEntry
    private MasterSlaveEntry getEntry(NodeSource source) {
            if (source.getRedirect() != null) {
                return this.getEntry(source.getAddr());
            } else {
                MasterSlaveEntry entry = source.getEntry();
                if (source.getRedisClient() != null) {
                    entry = this.getEntry(source.getRedisClient());
                }
    
                if (entry == null && source.getSlot() != null) {
                    entry = this.getEntry(source.getSlot());
                }
    
                return entry;
            }
        }
       
       然后就是一段比较复杂的处理 
        source.getRedirect() != null && 
        !URIBuilder.compare(entry.getClient().getAddr(), source.getAddr()) 
        && entry.hasSlave(source.getAddr()) ? 
        entry.redirectedConnectionWriteOp(command, source.getAddr()) : 
        entry.connectionWriteOp(command);
        
        判断是否需要重定向，我们直接看不需要entry.connectionWriteOp(command)
        MasterSlaveEntry：
        public RFuture<RedisConnection> connectionWriteOp(RedisCommand<?> command) {
                return this.writeConnectionPool.get(command);
            }
            
         我们从连接池中获取一个，我们目前是在写操作里面，我们就从主库连接池中取
         MasterConnectionPool:
         public RFuture<RedisConnection> get(RedisCommand<?> command) {
                 return this.acquireConnection(command, (ClientConnectionsEntry)this.entries.get(0));
             }
             
         继续看acquireConnection获取连接：
         ConnectionPool:
         protected final RFuture<T> acquireConnection(RedisCommand<?> command, final ClientConnectionsEntry entry) {
                 final RPromise<T> result = new RedissonPromise();
                 ConnectionPool.AcquireCallback<T> callback = new ConnectionPool.AcquireCallback<T>() {
                     public void run() {
                         result.removeListener(this);
                         ConnectionPool.this.connectTo(entry, result);
                     }
         
                     public void operationComplete(Future<T> future) throws Exception {
                         entry.removeConnection(this);
                     }
                 };
                 result.addListener(callback);
                 //继续获取
                 this.acquireConnection((ClientConnectionsEntry)entry, (Runnable)callback); 
                 return result;
          }
          //还是获取
          protected void acquireConnection(ClientConnectionsEntry entry, Runnable runnable) {
                  entry.acquireConnection(runnable);
              }
         
         //再接着获取
          ClientConnectionsEntry:
          public void acquireConnection(Runnable runnable) {
                  this.freeConnectionsCounter.acquire(runnable);
              }
          
           private final AsyncSemaphore freeConnectionsCounter;
          AsyncSemaphore 异步信号量，看是否有充足连接可以获取，如果已达上限，就强制当前连接
          //接着acquire
          public void acquire(Runnable listener) {
                  this.acquire(listener, 1);
              }
            public void acquire(Runnable listener) {
                  this.acquire(listener, 1);
              }

     #### 已经连接
     entry.connectionWriteOp(command)     

    public RFuture<RedisConnection> connectionWriteOp(RedisCommand<?> command) {
        return this.writeConnectionPool.get(command);
    }
    
    public RFuture<RedisConnection> get(RedisCommand<?> command) {
            return this.acquireConnection(command, (ClientConnectionsEntry)this.entries.get(0));
        }
        
     protected final RFuture<T> acquireConnection(RedisCommand<?> command, final ClientConnectionsEntry entry) {
             final RPromise<T> result = new RedissonPromise();
             //已经有连接的话
             ConnectionPool.AcquireCallback<T> callback = new ConnectionPool.AcquireCallback<T>() {
                 public void run() {
                     result.removeListener(this);
                     //拿着现有连接
                     ConnectionPool.this.connectTo(entry, result);
                 }
     
                 public void operationComplete(Future<T> future) throws Exception {
                     entry.removeConnection(this);
                 }
             };
             result.addListener(callback);
             this.acquireConnection((ClientConnectionsEntry)entry, (Runnable)callback);
             return result;
         }
    
    
    private void connectTo(ClientConnectionsEntry entry, RPromise<T> promise) {
            if (promise.isDone()) {
                this.releaseConnection(entry);
            } else {
            //从连接中拿出一个
                T conn = this.poll(entry);
                if (conn != null) {
                //不活跃
                    if (!conn.isActive() && entry.getNodeType() == NodeType.SLAVE) {
                        entry.trySetupFistFail();
                    }
                   //成功连接
                    this.connectedSuccessful(entry, promise, conn);
                } else {
                //重新建立连接
                    this.createConnection(entry, promise);
                }
            }
        }
```
### 拿一个命令看看它怎么执行
- 一个很简单的RList<String> list = redissonClient.getList("list")  list.size()
- 源码
```text
        //Re'di's'son 获取一个list
    public <V> RList<V> getList(String name) {
        return new RedissonList(this.connectionManager.getCommandExecutor(), name, this);
    }
    
      //想拿到它的长度    
      public int size() {
          return (Integer)this.get(this.sizeAsync());
      }
      
      //异步去获取
      public RFuture<Integer> sizeAsync() {
              return this.commandExecutor.readAsync(this.getName(), this.codec, RedisCommands.LLEN_INT, new Object[]{this.getName()});
      }
      
      public <T, R> RFuture<R> readAsync(String key, Codec codec, RedisCommand<T> command, Object... params) {
              RPromise<R> mainPromise = this.createPromise();
              NodeSource source = this.getNodeSource(key);
              this.async(true, source, codec, command, params, mainPromise, 0, false);
              return mainPromise;
       }
       
       
     public <V, R> void async(boolean readOnlyMode, final NodeSource source, Codec codec, final RedisCommand<V> command, final Object[] params, RPromise<R> mainPromise, int attempt, final boolean ignoreRedirect) {
     //跳过初始化
     。。。
     //初始化一个连接，用于执行命令
      final RFuture<RedisConnection> connectionFuture = this.getConnection(readOnlyMode, source, command);
      RPromise<R> attemptPromise = new RedissonPromise();
      //AsyncDetail初始化
      details.init(connectionFuture, attemptPromise, readOnlyMode, source, codecToUse, command, params, mainPromise, attempt);
      FutureListener<R> mainPromiseListener = new FutureListener<R>() {
                     public void operationComplete(Future<R> future) throws Exception {
                         if (future.isCancelled() && connectionFuture.cancel(false)) {
                             CommandAsyncService.log.debug("Connection obtaining canceled for {}", command);
                             details.getTimeout().cancel();
                             if (details.getAttemptPromise().cancel(false)) {
                                 CommandAsyncService.this.free(params);
                             }
                         }
     
                     }
                 };
       //跳过计时器
       。。。。。
       
      connectionFuture.addListener(new FutureListener<RedisConnection>() {
                     public void operationComplete(Future<RedisConnection> connFuture) throws Exception {
                         if (connFuture.isCancelled()) {
                         //取消
                             CommandAsyncService.this.connectionManager.getShutdownLatch().release();
                         } else if (!connFuture.isSuccess()) {
                         //未成功
                             CommandAsyncService.this.connectionManager.getShutdownLatch().release();
                             details.setException(CommandAsyncService.this.convertException(connectionFuture));
                         } else if (!details.getAttemptPromise().isDone() && !details.getMainPromise().isDone()) {
                         
                             final RedisConnection connection = (RedisConnection)connFuture.getNow();
                             //发送指令
                             CommandAsyncService.this.sendCommand(details, connection);
                             details.getWriteFuture().addListener(new ChannelFutureListener() {
                                 public void operationComplete(ChannelFuture future) throws Exception {
                                     CommandAsyncService.this.checkWriteFuture(details, ignoreRedirect, connection);
                                 }
                             });
                             CommandAsyncService.this.releaseConnection(source, connectionFuture, details.isReadOnlyMode(), details.getAttemptPromise(), details);
                         } else {
                             CommandAsyncService.this.releaseConnection(source, connectionFuture, details.isReadOnlyMode(), details.getAttemptPromise(), details);
                         }
                     }
                 });
                 attemptPromise.addListener(new FutureListener<R>() {
                     public void operationComplete(Future<R> future) throws Exception {
                         CommandAsyncService.this.checkAttemptFuture(source, details, future, ignoreRedirect);
                     }
                 });
     }

//接下来发送指令了
protected <R, V> void sendCommand(AsyncDetails<V, R> details, RedisConnection connection) {
        if (details.getSource().getRedirect() == Redirect.ASK) {
            List<CommandData<?, ?>> list = new ArrayList(2);
            RPromise<Void> promise = new RedissonPromise();
            list.add(new CommandData(promise, details.getCodec(), RedisCommands.ASKING, new Object[0]));
            list.add(new CommandData(details.getAttemptPromise(), details.getCodec(), details.getCommand(), details.getParams()));
            RPromise<Void> main = new RedissonPromise();
            //这部就是最终发送的地方啦，最后就是通过netty的Channel,建立通道，刷写数据
            ChannelFuture future = connection.send(new CommandsData(main, list, false));
            details.setWriteFuture(future);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("acquired connection for command {} and params {} from slot {} using node {}... {}", new Object[]{details.getCommand(), LogHelper.toString(details.getParams()), details.getSource(), connection.getRedisClient().getAddr(), connection});
            }

            ChannelFuture future = connection.send(new CommandData(details.getAttemptPromise(), details.getCodec(), details.getCommand(), details.getParams()));
            details.setWriteFuture(future);
        }

    }
    
    //我们发送数据
    public ChannelFuture send(CommandsData data) {
            return this.channel.writeAndFlush(data);
        }
        
        AbstractChannel:
        public ChannelFuture writeAndFlush(Object msg) {
                return this.pipeline.writeAndFlush(msg);
            }
       
       DefaultChannelPipeline:
       public final ChannelFuture writeAndFlush(Object msg) {
               return this.tail.writeAndFlush(msg);
           }
   
   AbstractChannelHandlerContext:
       public ChannelFuture writeAndFlush(Object msg) {
               return this.writeAndFlush(msg, this.newPromise());
           }
           
       public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
               if (msg == null) {
                   throw new NullPointerException("msg");
               } else if (this.isNotValidPromise(promise, true)) {
                   ReferenceCountUtil.release(msg);
                   return promise;
               } else {
               //过了这么久，饶了一大圈，走到channel 走进context，才真正做write
                   this.write(msg, true, promise);
                   return promise;
               }
           }
       
       private void write(Object msg, boolean flush, ChannelPromise promise) {
               AbstractChannelHandlerContext next = this.findContextOutbound();
               Object m = this.pipeline.touch(msg, next);
               EventExecutor executor = next.executor();
               if (executor.inEventLoop()) {
                   if (flush) {//是否需要flush
                    //要，其实就差一步
                       next.invokeWriteAndFlush(m, promise);
                   } else {
                   //不要
                       next.invokeWrite(m, promise);
                   }
               } else {
                   Object task;
                   if (flush) {
                       task = AbstractChannelHandlerContext.WriteAndFlushTask.newInstance(next, m, promise);
                   } else {
                       task = AbstractChannelHandlerContext.WriteTask.newInstance(next, m, promise);
                   }
       
                   safeExecute(executor, (Runnable)task, promise, m);
               }
       
           }
           
       private void invokeWriteAndFlush(Object msg, ChannelPromise promise) {
               if (this.invokeHandler()) {
               //调用写
                   this.invokeWrite0(msg, promise);
               //调用flush
                   this.invokeFlush0();
               } else {
                   this.writeAndFlush(msg, promise);
               }
       
           }
           
        private void invokeWrite0(Object msg, ChannelPromise promise) {
                try {
                //抓一个handler来处理这里的问题，我们这边是执行一个command，就用CommandHandler
                    ((ChannelOutboundHandler)this.handler()).write(this, msg, promise);
                } catch (Throwable var4) {
                    notifyOutboundHandlerException(var4, promise);
                }
        
            }
         
         //这就是最后了
         CommandHandler：   
         public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                 if (this.debugEnabled) {
                     logger.debug("{} write(ctx, {}, promise)", this.logPrefix(), msg);
                 }
         
                 if (msg instanceof RedisCommand) {//是redis命令码
                     this.writeSingleCommand(ctx, (RedisCommand)msg, promise);
                 } else if (msg instanceof List) {//是list吗
                     List<RedisCommand<?, ?, ?>> batch = (List)msg;
                     if (batch.size() == 1) {//一条数据吗
                         this.writeSingleCommand(ctx, (RedisCommand)batch.get(0), promise);
                     } else {//批量吗
                         this.writeBatch(ctx, batch, promise);
                     }
                 } else {
                     if (msg instanceof Collection) {//是集合吗
                         this.writeBatch(ctx, (Collection)msg, promise);
                     }
         
                 }
             }
     
       
```
- 一条命令的执行流程就是上面这样，那一些命令是有返回值，这个返回值怎么获取的呢?即使没有返回数据，也可能有指令完成的消息
```text
我们上面看到发送命令都是采用了异步的方式，并且在发送命令的时候添加了一大堆的监听器
监听是否执行完成，是否成功，是否有异常...还有一个就是返回值的监听器
  CommandAsyncService:
  write():
  attemptPromise.addListener(new FutureListener<R>() {
                public void operationComplete(Future<R> future) throws Exception {
                    CommandAsyncService.this.checkAttemptFuture(source, details, future, ignoreRedirect);
                }
            });
            
   落实到最后，就是对顶层Netty Channel的行为监听，然后反序列化:
   public class ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelInboundHandler {
       public ChannelInboundHandlerAdapter() {
       }
   
       public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelRegistered();
       }
   
       public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelUnregistered();
       }
   
       public void channelActive(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelActive();
       }
   
       public void channelInactive(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelInactive();
       }
   
   //接收到消息了，就是它
       public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
           ctx.fireChannelRead(msg);
       }
   
       public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelReadComplete();
       }
   
       public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
           ctx.fireUserEventTriggered(evt);
       }
   
       public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelWritabilityChanged();
       }
   
       public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
           ctx.fireExceptionCaught(cause);
       }
   }
   
   //往下走，看到ChannelHandler channelRead
   /**
        * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
        */
       @Override
       public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
           //获取到netty通信最基础的，二进制缓冲对象
           //英文解释：A random and sequential accessible sequence of zero or more bytes (octets)
           ByteBuf input = (ByteBuf) msg;
   
           if (!input.isReadable() || input.refCnt() == 0) {
               logger.warn("{} Input not readable {}, {}", logPrefix(), input.isReadable(), input.refCnt());
               return;
           }
   
           if (debugEnabled) {
               logger.debug("{} Received: {} bytes, {} commands in the stack", logPrefix(), input.readableBytes(), stack.size());
           }
   
           try {
               if (buffer.refCnt() < 1) {
                   logger.warn("{} Ignoring received data for closed or abandoned connection", logPrefix());
                   return;
               }
   
               if (debugEnabled && ctx.channel() != channel) {
                   logger.debug("{} Ignoring data for a non-registered channel {}", logPrefix(), ctx.channel());
                   return;
               }
   
               if (traceEnabled) {
                   logger.trace("{} Buffer: {}", logPrefix(), input.toString(Charset.defaultCharset()).trim());
               }
               
               //写到本地
               buffer.writeBytes(input);
               //反序列化
               decode(ctx, buffer);
           } finally {
               input.release();
           }
       }
       
       
       protected void decode(ChannelHandlerContext ctx, ByteBuf buffer) throws InterruptedException {
       
               if (pristine && stack.isEmpty() && buffer.isReadable()) {
       
                   if (debugEnabled) {
                       logger.debug("{} Received response without a command context (empty stack)", logPrefix());
                   }
       
                   if (consumeResponse(buffer)) {
                       pristine = false;
                   }
       
                   return;
               }
             //可以反序列化
               while (canDecode(buffer)) {
                   //反序列化的结果，或许可以说命令的结果或者返回值，是和命令发送的顺序一一对应的
                   //双端队列stack，这边当队列用，先进先出，获取队首元素
                   RedisCommand<?, ?, ?> command = stack.peek();
                   if (debugEnabled) {
                       logger.debug("{} Stack contains: {} commands", logPrefix(), stack.size());
                   }
       
                   pristine = false;
       
                   try {
                       //再调用
                       if (!decode(ctx, buffer, command)) {
                           return;
                       }
                   } catch (Exception e) {
       
                       ctx.close();
                       throw e;
                   }
       
                   if (isProtectedMode(command)) {
                       onProtectedMode(command.getOutput().getError());
                   } else {
       
                       if (canComplete(command)) {
                           stack.poll();
       
                           try {
                               complete(command);
                           } catch (Exception e) {
                               logger.warn("{} Unexpected exception during request: {}", logPrefix, e.toString(), e);
                           }
                       }
                   }
       
                   afterDecode(ctx, command);
               }
       
               if (buffer.refCnt() != 0) {
                   buffer.discardReadBytes();
               }
           }
           
        
        
        。。。。。反复调用几次
        protected boolean decode(ByteBuf buffer, RedisCommand<?, ?, ?> command, CommandOutput<?, ?, ?> output) {
                return rsm.decode(buffer, command, output);
            }
            
            
       最后调用redisStateMachine的反序列化，就如英文所说，反序列化响应，然后返回一个是否响应已读，关于状态处理，有ERROR 信号 INTERGER 事务 BYTES
        /**
            * Attempt to decode a redis response and return a flag indicating whether a complete response was read.
            *
            * @param buffer Buffer containing data from the server.
            * @param command the command itself
            * @param output Current command output.
            * @return true if a complete response was read.
            */
           public boolean decode(ByteBuf buffer, RedisCommand<?, ?, ?> command, CommandOutput<?, ?, ?> output) {
               int length, end;
               ByteBuffer bytes;
       
               if (debugEnabled) {
                   logger.debug("Decode {}", command);
               }
       
               if (isEmpty(stack)) {
                   add(stack, new State());
               }
       
               if (output == null) {
                   return isEmpty(stack);
               }
       
               loop:
       
               while (!isEmpty(stack)) {
                   State state = peek(stack);
       
                   if (state.type == null) {
                       if (!buffer.isReadable()) {
                           break;
                       }
                       state.type = readReplyType(buffer);
                       buffer.markReaderIndex();
                   }
       
                   switch (state.type) {
                       case SINGLE:
                           if ((bytes = readLine(buffer)) == null) {
                               break loop;
                           }
       
                           if (!QUEUED.equals(bytes)) {
                               safeSet(output, bytes, command);
                           }
                           break;
                       case ERROR:
                           if ((bytes = readLine(buffer)) == null) {
                               break loop;
                           }
                           safeSetError(output, bytes, command);
                           break;
                       case INTEGER:
                           if ((end = findLineEnd(buffer)) == -1) {
                               break loop;
                           }
                           long integer = readLong(buffer, buffer.readerIndex(), end);
                           safeSet(output, integer, command);
                           break;
                       case BULK:
                           if ((end = findLineEnd(buffer)) == -1) {
                               break loop;
                           }
                           length = (int) readLong(buffer, buffer.readerIndex(), end);
                           if (length == -1) {
                               safeSet(output, null, command);
                           } else {
                               state.type = BYTES;
                               state.count = length + 2;
                               buffer.markReaderIndex();
                               continue loop;
                           }
                           break;
                       case MULTI:
                           if (state.count == -1) {
                               if ((end = findLineEnd(buffer)) == -1) {
                                   break loop;
                               }
                               length = (int) readLong(buffer, buffer.readerIndex(), end);
                               state.count = length;
                               buffer.markReaderIndex();
                               safeMulti(output, state.count, command);
                           }
       
                           if (state.count <= 0) {
                               break;
                           }
       
                           state.count--;
                           addFirst(stack, new State());
       
                           continue loop;
                       case BYTES:
                       //这边就是我们自定义的序列化反序列化，去接收一个二进制数组，然后把它转成我们要的东西
                           if ((bytes = readBytes(buffer, state.count)) == null) {
                               break loop;
                           }
                           safeSet(output, bytes, command);
                           break;
                       default:
                           throw new IllegalStateException("State " + state.type + " not supported");
                   }
       
                   buffer.markReaderIndex();
                   remove(stack);
       
                   output.complete(size(stack));
               }
       
               if (debugEnabled) {
                   logger.debug("Decoded {}, empty stack: {}", command, isEmpty(stack));
               }
       
               return isEmpty(stack);
           }

```