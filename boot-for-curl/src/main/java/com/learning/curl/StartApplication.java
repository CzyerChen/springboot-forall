/**
 * Author:   claire
 * Date:    2021-05-08 - 00:06
 * Description: 启动类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-08 - 00:06          V1.0.0          启动类
 */
package com.learning.curl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 
 * 〈启动类〉
 *
 * @author claire
 * @date 2021-05-08 - 00:06
 * @since 1.0.0
 */
@SpringBootApplication
public class StartApplication {
    public static void main(String[] args){
        SpringApplication.run(StartApplication.class,args);
    }
}
