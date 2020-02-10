package com.redission.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.redission.serializer.FastJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 19 17:49
 */
@Configuration
public class RedissonConfig extends CachingConfigurerSupport {
    @Value("${redisson.config.file}")
    private String redissonFile;


    /**
     * 从文件,无需指定是用什么模式，并且参数有哪些,多参数的配置比较合适
     */
    @Bean
    public RedissonClient redissonClient() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(redissonFile);

        Config config = Config.fromYAML(classPathResource.getInputStream());
        return Redisson.create(config);

    }

    /**
     * java bean方式，手动注入参数设置参数
     *
     * @return
     */
     /* //集群模式
       config.useClusterServers();
       //本地节点
       config.useCustomServers();
       //主从模式
       config.useMasterSlaveServers();
       //哨兵模式
       config.useSentinelServers()*/
   /*@Bean
    public RedissonClient redissonClient(){
       Config config = new Config();
       //单节点
       config.useSingleServer()
               .setAddress("redis://127.0.0.1:6379")
               .setDatabase(0)
               .setConnectionMinimumIdleSize(32)
               .setConnectionPoolSize(64);
       return Redisson.create(config);

   }*/

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer)).entryTtl(Duration.ofDays(30));
        return configuration;
    }
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        /**
         *             参考默认实现，详见autoconfigure中的RedisCacheConfiguration
         *             Redis redisProperties = this.cacheProperties.getRedis();
         *             org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig();
         *             config = config.serializeValuesWith(SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
         *             if (redisProperties.getTimeToLive() != null) {
         *                 config = config.entryTtl(redisProperties.getTimeToLive());
         *             }
         */

/**
 *         有人还会想用redis默认的序列化方式jsonjackson,思路是一样的，就是不用自定义了，这个有实现
 *         Jackson2JsonRedisSerializer serializer=new Jackson2JsonRedisSerializer(Object.class);
 *         RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);
 *         RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
 */

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //自定义序列化方式
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        //封装成pair
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
        //获取默认配置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        //给final参数设置值，通过封装旧参数，返回新对象的方式
        RedisCacheConfiguration redisCacheConfiguration1 = redisCacheConfiguration.entryTtl(Duration.ofSeconds(100));
        //组装cachemanager
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration1);
        //添加json白名单，不然会返回是不可接受的序列化，com.alibaba.fastjson.JSONException: autoType is not support
        /*
        使用fastjson的时候：序列化时将class信息写入，反解析的时候，
        fastjson默认情况下会开启autoType的检查，相当于一个白名单检查，
        如果序列化信息中的类路径不在autoType中，
        反解析就会报com.alibaba.fastjson.JSONException: autoType is not support的异常
        可参考 https://blog.csdn.net/u012240455/article/details/80538540
         */
        ParserConfig.getGlobalInstance().addAccept("com.redisson.domain");
        return redisCacheManager;


    }



    @Bean(name = "redisTemplate")
    @SuppressWarnings("unchecked")
    @ConditionalOnMissingBean(name = "redisTemplte")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        //value采用自定义方式
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);

        //key依旧采用默认的string方式
        template.setKeySerializer(new StringRedisSerializer());

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


}
