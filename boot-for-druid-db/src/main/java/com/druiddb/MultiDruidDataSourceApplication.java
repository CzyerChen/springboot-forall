/**
 * Author:   claire
 * Date:    2020-02-08 - 13:50
 * Description: 多数据库连接功能启动类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 13:50          V1.3.1           多数据库连接功能启动类
 */
package com.druiddb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 <br/> 
 * 〈多数据库连接功能启动类〉
 *
 * @author claire
 * @date 2020-02-08 - 13:50
 * @since 1.3.1
 */
@SpringBootApplication
public class MultiDruidDataSourceApplication {

    public static void main(String[] args){
        SpringApplication.run(MultiDruidDataSourceApplication.class,args);
    }
}
