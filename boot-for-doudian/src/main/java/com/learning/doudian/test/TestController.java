/**
 * Author:   claire
 * Date:    2022/10/24 - 12:53 下午
 * Description: 测试控制器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/10/24 - 12:53 下午          V1.0.0          测试控制器
 */
package com.learning.doudian.test;

import com.alibaba.fastjson.JSON;
import com.learning.doudian.domain.DoudianPushData;
import com.learning.doudian.domain.DoudianPushResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能简述
 * 〈测试控制器〉
 *
 * @author claire
 * @date 2022/10/24 - 12:53 下午
 * @since 1.0.0
 */
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("message")
    public DoudianPushResult receiveMsg(@RequestBody List<DoudianPushData> data) {
        System.out.println(JSON.toJSONString(data));
        DoudianPushResult result = new DoudianPushResult();
        result.setCode(0);
        result.setMsg("success");
        return result;
    }
}
