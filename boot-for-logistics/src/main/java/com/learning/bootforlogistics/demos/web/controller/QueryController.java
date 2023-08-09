/**
 * Author:   claire
 * Date:    2023/6/6 - 9:36 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/6/6 - 9:36 上午          V1.0.0
 */
package com.learning.bootforlogistics.demos.web.controller;

import com.learning.bootforlogistics.demos.web.config.LogisticsConfig;
import com.learning.bootforlogistics.demos.web.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2023/6/6 - 9:36 上午
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/logistics/query")
public class QueryController {
    @Autowired
    private LogisticsConfig config;

    /**
     * 响应的body:
     * {
     *   "data": {
     *     "expressCode": "YTO",
     *     "expressCompanyName": "圆通快递",
     *     "number": "YT*****535",
     *     "logisticsStatus": "SIGN",
     *     "logisticsStatusDesc": "已签收",
     *     "theLastMessage": "您******如果您对我们的服务感到满意，请在[评价快递员]处赐予我们五星好评~",
     *     "theLastTime": "2023-06-05 20:22:50",
     *     "takeTime": "21小时35分",
     *     "logisticsTraceDetails": [
     *       {
     *         "areaCode": "CN330204000000",
     *         "areaName": "浙江省,宁波市,江东区",
     *         "subLogisticsStatus": "ACCEPT",
     *         "time": 1685874576000,
     *         "logisticsStatus": "ACCEPT",
     *         "desc": "您的快****"
     *       },
     *       {
     *         "areaCode": "CN330204000000",
     *         "areaName": "浙江省,宁波市,江东区",
     *         "subLogisticsStatus": "TRANSPORT",
     *         "time": 1685881628000,
     *         "logisticsStatus": "TRANSPORT",
     *         "desc": "您的快件****心公司】"
     *       },
     *      ...
     *       {
     *         "subLogisticsStatus": "STA_INBOUND",
     *         "time": 1685952305000,
     *         "logisticsStatus": "DELIVERING",
     *         "desc": "您的快件****您服务！"
     *       },
     *       {
     *         "subLogisticsStatus": "SIGN",
     *         "time": 1685967770000,
     *         "logisticsStatus": "SIGN",
     *         "desc": "您的快****星好评~"
     *       }
     *     ]
     *   },
     *   "msg": "成功",
     *   "success": true,
     *   "code": 200,
     *   "taskNo": "9278282****72754862"
     * }
     */
    @GetMapping
    public String queryOneTime(@RequestParam String number) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + config.getQueryAppcode());
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
//        bodys.put("expressCode", "YTO"); //可选，快递公司编号 例如圆通:YTO，详见产品说明中：快递公司编码对照表 注意：快递公司编号不传时，系统会自动识别快递公司编号，但响应时间会比传递快递编号略长
        //bodys.put("mobile", "mobile"); //看快递公司，顺丰速运/丰网速运需要传入收/寄件人手机号或后四位手机号
        bodys.put("number", number); //必选，快递运单号

        try {
            HttpResponse response = HttpUtils.doPost(config.getQueryHost(), config.getQueryPath(), config.getQueryMethod(), headers, querys, bodys);
            log.info(response.toString());
            String result =EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8); //这边注意指定编码，否则中文会乱码
            log.info(result);
            return result;
        } catch (Exception e) {
            log.error("err", e);
        }
        return "success";
    }
}
