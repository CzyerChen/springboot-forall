/**
 * Author:   claire
 * Date:    2021/10/25 - 10:12 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021/10/25 - 10:12 上午          V1.0.0
 */
package com.learning.redis.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2021/10/25 - 10:12 上午
 * @since 1.0.0
 */
@Component
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("接受到消息：" + message + "," + new String(pattern));
    }
}
