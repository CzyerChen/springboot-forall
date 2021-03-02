/**
 * Author:   claire
 * Date:    2021-03-01 - 15:50
 * Description: hdfs连接配置
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-03-01 - 15:50          V1.17.0          hdfs连接配置
 */
package boot.mybatisplus.config;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 功能简述
 * 〈hdfs连接配置〉
 *
 * @author claire
 * @date 2021-03-01 - 15:50
 * @since 1.17.0
 */
@Configuration
@ConfigurationProperties(prefix = "filesystem")
@Data
public class HdfsClientConfig {
    private String defaultUrl;
}
