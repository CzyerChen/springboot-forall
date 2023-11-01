/**
 * Author:   claire
 * Date:    2023/10/25 - 2:04 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/25 - 2:04 下午          V1.0.0
 */
package com.learning.runner;

import com.learning.producer.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author claire
 * @date 2023/10/25 - 2:04 下午
 * @since 1.0.0
 */
@Component
public class ProducerRunner implements CommandLineRunner {
    @Autowired
    private SendService sendService;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 5; i++) {
//            sendService.sendText("group1-index:" + i);
//            System.out.println("send group1 index:"+i);
//            sendService.sendText2("group2-index:" + i);
//            System.out.println("send group2 index:"+i);
//            sendService.sendWithTag("group2-index:" + i,"tagfilter");
//            System.out.println("send tagfilter group2 index:"+i);
//            MessageDto messageDto = new MessageDto();
//            messageDto.setIndex(String.valueOf((Math.round(Math.random()))));
//            messageDto.setTitle("title" + i);
//            messageDto.setContent("content");
            sendService.sendTrans("transdata");
        }
    }
}
