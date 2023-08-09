/**
 * Author:   claire
 * Date:    2023/6/6 - 9:16 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/6/6 - 9:16 上午          V1.0.0
 */
package com.learning.bootforlogistics.demos.web.controller;

import com.learning.bootforlogistics.demos.web.config.LogisticsConfig;
import com.learning.bootforlogistics.demos.web.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2023/6/6 - 9:16 上午
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/logistics/subscribe")
public class CallbackController {
    @Autowired
    private LogisticsConfig config;

    @GetMapping("submit")
    public String submitTask(@RequestParam String number) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + config.getSubscribeAppcode());
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("callBackUrl", "http://127.0.0.1:8080/logistics/callback");
//        bodys.put("expressCode", "YTO");   //可选，快递公司编号 例如圆通:YTO，详见产品说明中：快递公司编码对照表 注意：快递公司编号不传时，系统会自动识别快递公司编号，但响应时间会比传递快递编号略长
//        bodys.put("mobile", "mobile");  //看快递公司，顺丰速运/丰网速运需要传入收/寄件人手机号或后四位手机号
        bodys.put("number", number);//必选，快递运单号
        String result = "";
        try {
            HttpResponse response = HttpUtils.doPost(config.getSubscribeHost(), config.getSubscribePath(), config.getSubscribeMethod(), headers, querys, bodys);
            log.info(response.toString());
            result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            log.info(result);
        } catch (Exception e) {
            log.error("err", e);
        }
        return result;
    }

    /**
     * {
     * "expressCode":"EMS", // 快递公司编号 详见：快递公司编码对照表
     * "number":"9796578035309",// 运单编号
     * "logisticsStatus":"SIGN", // 当前最新物流状态 详见：物流状态编码对照表
     * "logisticsStatusDesc":"已签收", // 当前最新物流状态描述
     * "details":[ // 完整的物流轨迹
     * {
     * "time":1632123146000, // 物流变更时间
     * "logisticsStatus":"ACCEPT", // 物流状态 详见：物流状态编码对照表
     * "subLogisticsStatus":"ACCEPT", // 物流子状态 详见：物流状态编码对照表
     * "desc":"【杭州电商仓配揽投部】已收寄,揽投员:刘岭,电话:13754324900", //物流路由信
     * 息描述内容
     * "areaCode":"CN330100000000", // 路由节点所在地区行政编码
     * "areaName":"浙江省,杭州市" // 路由节路由节点所在地区
     * },
     * {
     * "time":1632140994000,
     * "logisticsStatus":"TRANSPORT",
     * "subLogisticsStatus":"TRANSPORT",
     * "desc":"离开【杭州电商仓配揽投部】,下一站【杭州萧山区东片集散中心】",
     * "areaCode":"CN330100000000",
     * "areaName":"浙江省,杭州市"
     * },
     * ...
     * ]
     * }
     * <p>
     * {
     * "success":true
     * }
     * // 接收失败
     * {
     * "success":false,
     * "msg": "接收失败"  //自动失败重试，最多推送 3 次。如有疑问可工单联系工程师手动解决
     * }
     */
    @GetMapping("callback")
    public Map<String, Object> receive(@RequestBody String data, HttpServletRequest request) {
        log.info("接收到快递物流推送数据: {}", data);
        Map<String, Object> map = new HashMap<>();
        //处理业务逻辑
        Boolean result = Math.random() > 0.5;
        if (result) {
            map.put("success", true);
        } else {
            map.put("success", false);
            map.put("msg", "接收失败, 业务处理失败");
        }
        return map;
    }
}
