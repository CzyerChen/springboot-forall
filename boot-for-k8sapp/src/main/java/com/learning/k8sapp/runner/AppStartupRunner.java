/**
 * Author:   claire
 * Date:    2023/8/14 - 6:03 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/14 - 6:03 下午          V1.0.0
 */
package com.learning.k8sapp.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author claire
 * @date 2023/8/14 - 6:03 下午
 * @since 1.0.0
 */
@PropertySource("classpath:proper.properties")
@Component
public class AppStartupRunner implements CommandLineRunner {
    @Value("${env:'default'}")
    private String env;
    @Value("${msg:'default'}")
    private String msg;
    @Value("${test.prop1:'defaultprop1'}")
    private String prop1;
    @Value("${test.prop2:'defaultprop2'}")
    private String prop2;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.println("cur env:"+env +", msg:"+msg);
            System.out.println("cur prop1:"+prop1 +", prop2:"+prop2);
            Thread.sleep(10000);
        }
    }
}
