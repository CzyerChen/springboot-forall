package com.demo; /**
 * Author:   claire
 * Date:    2021-05-27 - 16:48
 * Description: 启动类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-27 - 16:48          V1.0.0          启动类
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 
 * 〈启动类〉
 *
 * @author claire
 * @date 2021-05-27 - 16:48
 * @since 1.0.0
 */
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoApplication.class,args);
    }
}
