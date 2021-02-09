/**
 * Author:   claire
 * Date:    2021-02-09 - 16:59
 * Description: 日志类测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 16:59          V1.17.0          日志类测试
 */
package com.learning.log;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.StaticLog;

/**
 * 功能简述 
 * 〈日志类测试〉
 *
 * @author claire
 * @date 2021-02-09 - 16:59
 */
public class HCoreLogTest {
    private static final Log log = LogFactory.get();

    public static void main(String[] args){
        StaticLog.info("This is static {} log.", "INFO");
    }
}
