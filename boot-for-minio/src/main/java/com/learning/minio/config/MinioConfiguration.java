/**
 * Author:   claire
 * Date:    2022/3/30 - 11:19 上午
 * Description: minio配置类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/30 - 11:19 上午          V1.0.0          minio配置类
 */
package com.learning.minio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能简述 
 * 〈minio配置类〉
 *
 * @author claire
 * @date 2022/3/30 - 11:19 上午
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.minio")
@Data
public class MinioConfiguration {
    private String accessKey;

    private String secretKey;

    private String url;

    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
