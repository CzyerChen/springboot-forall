/**
 * Author:   claire
 * Date:    2021/10/25 - 10:10 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021/10/25 - 10:10 上午          V1.0.0
 */
package com.learning.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021/10/25 - 10:10 上午
 * @since 1.0.0
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;
    }
}
