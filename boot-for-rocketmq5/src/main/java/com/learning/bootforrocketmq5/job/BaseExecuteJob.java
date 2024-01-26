/**
 * Author:   claire Date:    2024/1/16 - 10:29 下午 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2024/1/16 - 10:29 下午          V1.0.0
 */

package com.learning.bootforrocketmq5.job;

import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import test.apache.skywalking.apm.testcase.rocketmq.client.java.service.MessageService;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2024/1/16 - 10:29 下午
 * @since 1.0.0
 */
@Component
public class BaseExecuteJob {
    static final String TOPIC = "dataTopic";
    static final String TAG = "TagA";
    static final String GROUP = "group1";

    @Autowired
    private MessageService messageService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void doJob(){
        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm")+"!!!!job start!!!");
//        messageService.simpleConsumes(  String.join(",",TOPIC+"5",TOPIC+"6",TOPIC+"7"), String.join(",",TAG + ":normal",TAG + ":normal",TAG + ":normal"), GROUP,2,10);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        thread.start();
    }

}
