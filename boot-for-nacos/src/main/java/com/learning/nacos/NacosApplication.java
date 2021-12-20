/**
 * Author:   claire
 * Date:    2021/12/18 - 5:06 下午
 * Description: nacos测试服务
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021/12/18 - 5:06 下午          V1.0.0          nacos测试服务
 */
package com.learning.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 
 * 〈nacos测试服务〉
 *
 * @author claire
 * @date 2021/12/18 - 5:06 下午
 * @since 1.0.0
 */
@SpringBootApplication
public class NacosApplication {

    public static void main(String[] args){
        SpringApplication.run(NacosApplication.class,args);
    }
}
