/**
 * Author:   claire
 * Date:    2022/3/3 - 5:58 下午
 * Description: influxdb主类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/3 - 5:58 下午          V1.0.0          influxdb主类
 */
package com.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 功能简述 
 * 〈influxdb主类〉
 *
 * @author claire
 * @date 2022/3/3 - 5:58 下午
 * @since 1.0.0
 */
@EnableScheduling
@SpringBootApplication
public class InfluxdbTestApplication {
    public static void main(String[] args){
        SpringApplication.run(InfluxdbTestApplication.class,args);
    }
}
