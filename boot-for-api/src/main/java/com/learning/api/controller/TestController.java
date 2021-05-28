/**
 * Author:   claire
 * Date:    2021-05-17 - 18:06
 * Description: 测试类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-17 - 18:06          V1.0.0          测试类
 */
package com.learning.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能简述 
 * 〈测试类〉
 *
 * @author claire
 * @date 2021-05-17 - 18:06
 * @since 1.0.0
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("script")
    public String testScript(String script){
        return script;
    }
}
