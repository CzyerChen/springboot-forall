/**
 * Author:   claire
 * Date:    2021-05-27 - 16:59
 * Description: 测试类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-27 - 16:59          V1.0.0          测试类
 */
package com.demo.controller;

import com.demo.bean.KeyValue;
import com.demo.send.BatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
    public void testSendFile(@RequestParam String filePath,@RequestParam String information,@RequestParam String priority){
        KeyValue keyValue = BatchUtils.smsFileSend(new File(filePath), information,uploadfileUrl,sendUrl, "保险",priority);
        logger.info(keyValue.getKey());
        logger.info(String.valueOf(keyValue.getValue()));
    }
}
