/**
 * Author:   claire
 * Date:    2021-02-07 - 15:46
 * Description: 日志测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-07 - 15:46          V1.17.0          日志测试
 */
package utils;

import com.redission.Application;
import com.redission.constant.LogFileName;
import com.redission.utils.LoggingUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 功能简述 
 * 〈日志测试〉
 *
 * @author claire
 * @date 2021-02-07 - 15:46
 * @since 1.17.0
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LoggingTest {

    @Test
    public void testLogging(){
        Logger logger1 = LoggingUtils.Logger(LogFileName.BIZ1);
        logger1.info("测试biz1data:[{}]",77777);
        Logger logger2 = LoggingUtils.Logger(LogFileName.BIZ2);
        logger2.info("测试biz2data:[{}]",8888888);
    }
}
