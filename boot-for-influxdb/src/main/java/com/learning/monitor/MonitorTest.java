/**
 * Author:   claire
 * Date:    2022/3/4 - 9:00 上午
 * Description: 监控测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/4 - 9:00 上午          V1.0.0          监控测试
 */
package com.learning.monitor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 功能简述 
 * 〈监控测试〉
 *
 * @author claire
 * @date 2022/3/4 - 9:00 上午
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class MonitorTest {

    private InfluxDB influxDB;

    @Scheduled(fixedRate = 5000)
    public void writeQps(){
        int count = (int)Math.random()*10;
        Point point = Point.measurement("test_analysis")//   test_analysis 数据表
                .tag("url","/demo") //url字段
                .addField("count",count) //统计内容
                .time(System.currentTimeMillis(),TimeUnit.MILLISECONDS) //时间
                .build();

        influxDB.write("big_sms","autogen",point);
        log.info("数据内容：{}",count);

    }
}
