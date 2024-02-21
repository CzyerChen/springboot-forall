/**
 * Author:   claire
 * Date:    2021-05-13 - 10:46
 * Description: 测试认证类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-13 - 10:46          V1.0.0          测试认证类
 */
package com.learning.api.test;

import cn.hutool.http.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 功能简述 
 * 〈测试认证类〉
 *
 * @author claire
 * @date 2021-05-13 - 10:46
 * @since 1.0.0
 */
@RestController
@RequestMapping("test")
public class TestAuthController {
    public static Logger logger = LoggerFactory.getLogger(TestAuthController.class);


    @GetMapping("sendauth")
    public String testSendAuth(){
        String uid ="4";
        String password = "somekey";
        try {
            Map<String, Object> parmMap = new HashMap<>();
            parmMap.put("biz", "测试业务");
            parmMap.put("prod", "测试产品");
            parmMap.put("fileid", "randomfileid1");
            parmMap.put("priority", "0");
            parmMap.put("t", String.valueOf(System.currentTimeMillis()));
            String uploadSign = getSignature(uid, password, parmMap);
            parmMap.put("uid", uid);
            parmMap.put("sign", uploadSign);

            StopWatch clock = new StopWatch("发送");
            clock.start("postfile");
            String result = HttpUtil.post("http://127.0.0.1:8080/web/test/auth", parmMap);
            clock.stop();

            return result;
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }

    @GetMapping("sendauth2")
    public String testSendAuth2(){
        String uid ="4";
        String password = "somekey";
        try {
            Map<String, Object> parmMap = new HashMap<>();
            parmMap.put("priority", "0");
            parmMap.put("t", String.valueOf(System.currentTimeMillis()));
            String uploadSign = getSignature(uid, password, parmMap);
            parmMap.put("uid", uid);
            parmMap.put("sign", uploadSign);

            StopWatch clock = new StopWatch("发送");
            clock.start("postfile");
            String result = HttpUtil.post("http://127.0.0.1:8080/web/test/auth", parmMap);
            clock.stop();

            return result;
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }

    public static String getSignature(String uid, String appkey, Map<String, Object> parmMap) {
        List<String> keys = new ArrayList<String>(parmMap.keySet());
        Collections.sort(keys);
        StringBuffer prestr = new StringBuffer(uid);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if("uid".equals(key) || "sign".equals(key)){
                continue;
            }
            String value = (String) parmMap.get(key);
            prestr.append(key).append("=").append(value);
        }
        prestr.append(appkey);
        String signature = null;
        signature = DigestUtils.md5Hex(prestr.toString().getBytes(Charset.forName("utf-8"))).toLowerCase();
        return signature;
    }
}
