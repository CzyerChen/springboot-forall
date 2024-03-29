/**
 * Author:   claire
 * Date:    2021-05-27 - 16:59
 * Description: 测试类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-27 - 16:59          V1.0.0          测试类
 */
package com.demo.mvc.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.demo.mvc.bean.KeyValue;
import com.demo.mvc.send.BatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
//import java.io.FileReader;

/**
 * 功能简述
 * 〈测试类〉
 *
 * @author claire
 * @date 2021-05-27 - 16:59
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Value("${uploadfile.url}")
    private String uploadfileUrl;
    @Value("${send.url}")
    private String sendUrl;

    @RequestMapping("sendFile")
    public void testSendFile(@RequestParam String filePath,
                             @RequestParam String information,
                             @RequestParam String priority,
                             @RequestParam String prod,
                             @RequestParam String biz,
                             @RequestParam(required = false) String channel,
                             @RequestParam(required = false) String signName,
                             @RequestParam(required = false) String ruleName) {
        KeyValue keyValue = BatchUtils.smsFileSend(new File(filePath), information, uploadfileUrl, sendUrl, prod, biz, priority, signName, channel, ruleName);
        logger.info(keyValue.getKey());
        logger.info(String.valueOf(keyValue.getValue()));
    }

    @RequestMapping("sendFile2")
    public void testSendFile2(@RequestParam String filePath,
                              @RequestParam String information,
                              @RequestParam int identity) {
        KeyValue keyValue = BatchUtils.smsFileSend(new File(filePath), information, identity);
        logger.info(keyValue.getKey());
        logger.info(String.valueOf(keyValue.getValue()));
    }

    @RequestMapping("sendBatch")
    public void testSendBatch(@RequestParam String filePath,
                              @RequestParam String information) {
        KeyValue keyValue = BatchUtils.smsFileSend(new File(filePath), information, 1);
        logger.info(keyValue.getKey());
        logger.info(String.valueOf(keyValue.getValue()));
    }

    @GetMapping("info")
    public String info() {
        return "hello";
    }

    class Solution {
        //归并
        public void merge(int[] nums1, int m, int[] nums2, int n) {
          if(n ==0){
              
          }
        }

    }
    public static void main(String[] args){

    }
}