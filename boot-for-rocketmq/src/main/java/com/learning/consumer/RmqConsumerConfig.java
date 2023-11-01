/**
 * Author:   claire
 * Date:    2023/10/25 - 1:46 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/25 - 1:46 下午          V1.0.0
 */
package com.learning.consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author claire
 * @date 2023/10/25 - 1:46 下午
 * @since 1.0.0
 */
@Configuration
@EnableBinding(SelfSink.class)
public class RmqConsumerConfig {
    
}
