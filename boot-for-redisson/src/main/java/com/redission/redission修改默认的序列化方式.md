> 默认序列化方式：key采用String,value采用JDK默认的序列化方式
```text
public static RedisCacheConfiguration defaultCacheConfig() {

		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

		registerDefaultConverters(conversionService);

		return new RedisCacheConfiguration(Duration.ZERO, true, true, CacheKeyPrefix.simple(),
				SerializationPair.fromSerializer(new StringRedisSerializer()),
				SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()), conversionService);
	}
```
> 我们在应用中，如果于很多别的框架结合，可能就需要统一序列化方式，jdk protobuf arvo kryo jackson fastjson

> spring boot 自动配置的缓存序列化为 JdkSerializationRedisSerializer，缺点:占用过多空间，跨平台困难，可读信差,很致命

> 处于性能，处于兼容性，考虑使用情况和社区活跃度，市场上fastjson是首选，性能比较出众，社区活跃，资料很多

### redis的序列化有哪些
- 一般redis的序列化方式主要有：字符串序列化、json序列化、xml序列化、jdk序列化，具体可查阅org.springframework.data.redis.serializer.RedisSerializer 的实现类
- 对于json这种虽然相对较大，有很大可读性的传递方式，有jackson和fastjson方式，这也是一块大家改序列方式的最大原因
```text
有以下这些序列化方式：
FastJsonRedisSerializer
GenericFastJsonRedisSerializer
GenericJackson2JsonRedisSerializer
GenericToStringSerializer
Jackson2JsonRedisSerializer
JdkSerializationRedisSerializer
OxmSerializer
StringRedisSerializer
```

### 看看源码 spring data redis中给出了实现
- 其实序列化/反序列化，就是二进制流字节流怎么转化为我们常用的类型，我们常用的类型怎么转化为字节数组，我们最终用的也就是JDK给我们最好的封装，一般情况下也不用完全自己写
#### key、hash key的序列化一般使用如下方式
```text
public class StringRedisSerializer implements RedisSerializer<String> {

	private final Charset charset;

	public StringRedisSerializer() {
		this(Charset.forName("UTF8"));
	}

	public StringRedisSerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	public String deserialize(byte[] bytes) {
		return (bytes == null ? null : new String(bytes, charset));
	}

	public byte[] serialize(String string) {
		return (string == null ? null : string.getBytes(charset));
	}
}

```
#### value、hash value的序列化一般使用如下方式：
- JdkSerializationRedisSerializer
```text
public class JdkSerializationRedisSerializer implements RedisSerializer<Object> {

// 默认构造函数
public JdkSerializationRedisSerializer(){
this(new SerializingConverter(), new DeserializingConverter());}

// 指定ClassLoader的构造函数（热部署时可能需要用到，避免不同的类加载器搞混）
public JdkSerializationRedisSerializer(ClassLoader classLoader){...}

// 指定org.springframework.core.convert.converter.Converter的构造函数（严重依赖spring框架，也可以自己实现）
public JdkSerializationRedisSerializer(Converter<Object, byte[]> serializer, Converter<byte[], Object> deserializer){...};

//序列化反序列方法：
public Object deserialize(@Nullable byte[] bytes) {

		if (SerializationUtils.isEmpty(bytes)) {
			return null;
		}

		try {
			return deserializer.convert(bytes);
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}

	@Override
	public byte[] serialize(@Nullable Object object) {
		if (object == null) {
			return SerializationUtils.EMPTY_ARRAY;
		}
		try {
			return serializer.convert(object);
		} catch (Exception ex) {
			throw new SerializationException("Cannot serialize", ex);
		}
	}

```
- 实现序列化反序列的机器是默认构造函数中的new SerializingConverter() new DeserializingConverter()
- 先看SerializingConverter
```text
public class SerializingConverter implements Converter<Object, byte[]> {
    private final Serializer<Object> serializer;

    public SerializingConverter() {
        this.serializer = new DefaultSerializer();
    }

    public SerializingConverter(Serializer<Object> serializer) {
        Assert.notNull(serializer, "Serializer must not be null");
        this.serializer = serializer;
    }

    public byte[] convert(Object source) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);

        try {
            this.serializer.serialize(source, byteStream);
            return byteStream.toByteArray();
        } catch (Throwable var4) {
            throw new SerializationFailedException("Failed to serialize object using " + this.serializer.getClass().getSimpleName(), var4);
        }
    }
}
```
- 看到convert方法主要就是使用了默认的序列化器的serialize方法，那我们就看 new DefaultSerializer()
```text
public class DefaultSerializer implements Serializer<Object> {
    public DefaultSerializer() {
    }

    public void serialize(Object object, OutputStream outputStream) throws IOException {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires a Serializable payload but received an object of type [" + object.getClass().getName() + "]");
        } else {
        //使用了我们非常熟悉的，原始流操作，写Object对象流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        }
    }
}
 
```
- 看完了序列化，再看一下反序列化DeserializingConverter ，也是看默认反序列化器DefaultDeserializer
```text
public class DefaultDeserializer implements Deserializer<Object> {
    @Nullable
    private final ClassLoader classLoader;

    public DefaultDeserializer() {
        this.classLoader = null;
    }

    public DefaultDeserializer(@Nullable ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Object deserialize(InputStream inputStream) throws IOException {
        ConfigurableObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);

        try {
            return objectInputStream.readObject();
        } catch (ClassNotFoundException var4) {
            throw new NestedIOException("Failed to deserialize object type", var4);
        }
    }
}
```
### 看了上面的基础序列化反序列方式，我们看看如何自定义
- 我们需要找到配置redis序列化方式的入口 -- 这个是我加入了spring-boot-autoconfigure，一开始我没有加，只有redis.cache实现的一个具体的RedisCacheConfiguration
- 为什么看下面这个自动配置的RedisCacheConfiguration呢？因为我们的自定义需要参照成熟的嘛，我们没有手动配置的时候，是怎么实现自动装配这个类的呢？
```text
@Configuration
@ConditionalOnClass({RedisConnectionFactory.class}) // 需要一个RedisConnectionFactory Class
@AutoConfigureAfter({RedisAutoConfiguration.class})  // 需要redis开启自动配置
@ConditionalOnBean({RedisConnectionFactory.class})  // 需要一个RedisConnectionFactory Bean 意思是实例化好了
@ConditionalOnMissingBean({CacheManager.class})    //需要没有CacheManager ，因为我们需要自己注入，改变序列化方式呀
@Conditional({CacheCondition.class})       
class RedisCacheConfiguration {

....
//这就是我们需要做的
 @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ResourceLoader resourceLoader) {
        RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(this.determineConfiguration(resourceLoader.getClassLoader()));
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet(cacheNames));
        }

        return (RedisCacheManager)this.customizerInvoker.customize(builder.build());
    }

    //改变配置的
    private org.springframework.data.redis.cache.RedisCacheConfiguration determineConfiguration(ClassLoader classLoader) {
        if (this.redisCacheConfiguration != null) {
            return this.redisCacheConfiguration;
        } else {
            Redis redisProperties = this.cacheProperties.getRedis();
            //获取基础配置，那我们基础的也不去修改
            org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig();
            //改变序列化方式
            config = config.serializeValuesWith(SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
            if (redisProperties.getTimeToLive() != null) {
            //修改ttl
                config = config.entryTtl(redisProperties.getTimeToLive());
            }

            if (redisProperties.getKeyPrefix() != null) {
            //修改key前缀
                config = config.prefixKeysWith(redisProperties.getKeyPrefix());
            }

            if (!redisProperties.isCacheNullValues()) {
                config = config.disableCachingNullValues();
            }

            if (!redisProperties.isUseKeyPrefix()) {
                config = config.disableKeyPrefix();
            }

            return config;
        }
    }
}
```
- 看上面的自动配置代码，只是给我一个提示，我们目前知道需要RedisConnectionFactory，我们需要实现CacheManager()方法，那很自然我们的方法就出来了public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory)
- 我们需要改变序列化配置 config = config.serializeValuesWith(SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader))); 就是这一行


### 看了源码我们能做什么？
- 1.就是自定义序列化方式咯，重要的就是解决序列化，反序列化的问题，自定义也不需要涉及到最底层的实现
- 2.还有可以参照redis的这一套序列化，其实就是常规的序列化反序列方式，作为JDK自身的序列化是只有一个顶层接口，我们也可以针对很多类型自定义不同的序列化反序列话方式，为什么呢，默认的用用不就好了？那为啥还要这么多序列化方式？针对不同的场景使用不同的序列化方式，可以提升性能，减少网络流量消耗，protobuf jackson fastjson... 有别人帮你实现的，也有可能需要自己重写






















### 附件
- 字符序列化
```text
public class StringSerializer implements Serializer<String> {


    private final Charset charset;

    public StringSerializer() {
        this(Charset.forName("UTF-8"));
    }

    public StringSerializer(Charset charset) {
        if (charset == null)
            throw new IllegalArgumentException("Charset must not be null!");

        this.charset = charset;
    }

    @Override
    public byte[] serialize(String string) throws SerializationException {
        return (string == null ? null : string.getBytes(charset));
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new String(bytes, charset));
    }
}
```
- jdk 序列化
```text
public class JdkSerializationSerializer implements Serializer<Object> {

    private final ClassLoader classLoader;

    public JdkSerializationSerializer() {
        this.classLoader = null;
    }

    public JdkSerializationSerializer(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }

        try {
            if (!(object instanceof Serializable)) {
                throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload " +
                        "but received an object of type [" + object.getClass().getName() + "]");
            }

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();

            return byteStream.toByteArray();
        } catch (Exception ex) {
            throw new SerializationException("Cannot serialize", ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (SerializationUtils.isEmpty(bytes)) {
            return null;
        }

        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
              ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(byteStream, this.classLoader);
                try {
                    return objectInputStream.readObject();
                } catch (ClassNotFoundException ex) {
                    throw new NestedIOException("Failed to deserialize object type", ex);
                }
            } catch (Throwable ex) {
                throw new SerializationFailedException("Failed to deserialize payload. " +
                        "Is the byte array a result of corresponding serialization for " +
                        this.getClass().getSimpleName() + "?", ex);
            }
        } catch (Exception ex) {
            throw new SerializationException("Cannot deserialize", ex);
        }
    }
}
```
