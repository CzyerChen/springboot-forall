/**
 * Author:   claire
 * Date:    2021-01-05 - 13:17
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-05 - 13:17          V1.14.0
 */
package com.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-05 - 13:17
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ShardingSphereTestApplication {

    public static void main(String[] args){
        SpringApplication.run(ShardingSphereTestApplication.class,args);
    }
}
