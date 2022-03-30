/**
 * Author:   claire
 * Date:    2022/3/11 - 11:07 上午
 * Description: spring-boot-admin 测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/11 - 11:07 上午          V1.0.0          spring-boot-admin 测试
 */
package com.learning.bootadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 
 * 〈spring-boot-admin 测试〉
 *
 * @author claire
 * @date 2022/3/11 - 11:07 上午
 * @since 1.0.0
 */
@SpringBootApplication
@EnableAdminServer
public class BootAdminApplication {
    public static void main(String[] args){
        SpringApplication.run(BootAdminApplication.class,args);
    }
}
