/**
 * Author:   claire
 * Date:    2021-02-09 - 17:57
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 17:57          V1.17.0
 */
package com.learning.cron;

import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2021-02-09 - 17:57
 */
public class HCoreCronTest {

    public static void main(String[] args) {
        CronUtil.start();
        CronUtil.stop();

        CronUtil.schedule("*/2 * * * * *", new Task() {
            @Override
            public void execute() {
                Console.log("Task excuted.");
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
