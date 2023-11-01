/**
 * Author:   claire
 * Date:    2023/10/25 - 1:46 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/25 - 1:46 下午          V1.0.0
 */
package com.learning.producer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author claire
 * @date 2023/10/25 - 1:46 下午
 * @since 1.0.0
 */
@Configuration
@EnableBinding(SelfSource.class)
public class RmqProducerConfig {
}
