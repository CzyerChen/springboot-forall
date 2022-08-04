/**
 * Author:   claire
 * Date:    2022/4/14 - 6:19 下午
 * Description: 企业微信功能主类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/4/14 - 6:19 下午          V1.0.0          企业微信功能主类
 */
package com.learning.qywx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 功能简述 
 * 〈企业微信功能主类〉
 *
 * @author claire
 * @date 2022/4/14 - 6:19 下午
 * @since 1.0.0
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"chat.qiye.wechat","com.learning.qywx"})
public class QywxStartupApplication {

    public static void main(String[] args){
        SpringApplication.run(QywxStartupApplication.class,args);
    }
}
