/**
 * Author:   claire
 * Date:    2021/12/20 - 3:57 下午
 * Description: 测试控制器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021/12/20 - 3:57 下午          V1.0.0          测试控制器
 */
package com.learning.nacos.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.learning.nacos.config.DemoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 功能简述
 * 〈测试控制器〉
 *
 * @author claire
 * @date 2021/12/20 - 3:57 下午
 * @since 1.0.0
 */
@ConditionalOnBean
@RestController
@RequestMapping("config")
public class ConfigController {
    @NacosInjected
    private ConfigService configService;

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @NacosValue(value = "${testnumber:-1}", autoRefreshed = true)
    private Integer testNumber;

    @Autowired
    private DemoProperties demoProperties;

    @GetMapping("/get")
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }

    @GetMapping("/getnumber")
    @ResponseBody
    public Integer getnumber() {
        return testNumber;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> publish(@RequestParam String dataId,
                                          @RequestParam(defaultValue = "DEFAULT_GROUP") String group,
                                          @RequestParam String content) throws NacosException {
        boolean result = configService.publishConfig(dataId, group, content);
        if (result) {
            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("getconfig")
    public void getConfig() throws NacosException {
        String config = configService.getConfig("example", "DEFAULT_GROUP", 1000);
        System.out.println(config);
    }

    @GetMapping("getnumconfig")
    public void getNumConfig(@RequestParam String name, @RequestParam String group) throws NacosException {
        String config = configService.getConfig(name, group, 1000);
        System.out.println(config);
    }

    @GetMapping("demo")
    public void getDemoProperties(){
        System.out.println(demoProperties.toString());
    }
}
