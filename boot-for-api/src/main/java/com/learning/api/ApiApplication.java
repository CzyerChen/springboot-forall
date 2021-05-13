/**
 * Author:   claire
 * Date:    2021-05-13 - 10:08
 * Description: 启动类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-13 - 10:08          V1.0.0          启动类
 */
package com.learning.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 
 * 〈启动类〉
 *
 * @author claire
 * @date 2021-05-13 - 10:08
 * @since 1.0.0
 */
@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args){
        SpringApplication.run(ApiApplication.class,args);
        
    }
}
