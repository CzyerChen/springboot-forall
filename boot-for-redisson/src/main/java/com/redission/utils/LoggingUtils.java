/**
 * Author:   claire
 * Date:    2021-02-07 - 15:41
 * Description: 日志帮助类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-07 - 15:41          V1.17.0          日志帮助类
 */
package com.redission.utils;

import com.redission.constant.LogFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能简述 
 * 〈日志帮助类〉
 *
 * @author claire
 * @date 2021-02-07 - 15:41
 */
public class LoggingUtils {
    public static <T> Logger Logger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 打印到指定的文件下
     *
     * @param desc 日志文件名称
     * @return
     */
    public static Logger Logger(LogFileName desc) {
        return LoggerFactory.getLogger(desc.getName());
    }
}
