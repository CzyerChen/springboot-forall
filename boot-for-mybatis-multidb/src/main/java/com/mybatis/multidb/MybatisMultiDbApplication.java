/**
 * Author:   claire
 * Date:    2020-02-08 - 14:28
 * Description: mybatis 多数据库连接启动类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 14:28          V1.3.1           mybatis 多数据库连接启动类
 */
package com.mybatis.multidb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 功能简述 <br/> 
 * 〈mybatis 多数据库连接启动类〉
 *
 * @author claire
 * @date 2020-02-08 - 14:28
 */
@SpringBootApplication
public class MybatisMultiDbApplication {
    public static void main(String[] args){
        SpringApplication.run(MybatisMultiDbApplication.class,args);
    }
}
