package com.redission.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 19 17:49
 */
@Configuration
public class RedissonConfig {
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
}
