/**
 * Author:   claire
 * Date:    2022/7/25 - 1:55 下午
 * Description: 微信消息回调控制器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/7/25 - 1:55 下午          V1.0.0          微信消息回调控制器
 */
package com.learning.qwhook.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 功能简述 
 * 〈微信消息回调控制器〉
 *
 * @author claire
 * @date 2022/7/25 - 1:55 下午
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("msg")
public class MsgCallBackController {


    @RequestMapping("detail")
    public String receiveMsg(@RequestBody Map<String,Object> content){
     log.info("[MsgCallBackController][receiveMsg] content:{}", JSON.toJSONString(content));
     return "success";
    }
    
}
