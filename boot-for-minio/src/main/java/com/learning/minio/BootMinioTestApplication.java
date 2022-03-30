/**
 * Author:   claire
 * Date:    2022/3/30 - 11:17 上午
 * Description: minio主类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/30 - 11:17 上午          V1.0.0          minio主类
 */
package com.learning.minio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 
 * 〈minio主类〉
 *
 * @author claire
 * @date 2022/3/30 - 11:17 上午
 * @since 1.0.0
 */
@SpringBootApplication
public class BootMinioTestApplication {

    public static void main(String[] args){
        SpringApplication.run(BootMinioTestApplication.class,args);
    }
}
