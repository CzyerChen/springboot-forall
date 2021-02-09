/**
 * Author:   claire
 * Date:    2021-02-09 - 17:07
 * Description: json测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 17:07          V1.17.0          json测试
 */
package com.learning.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 功能简述 
 * 〈json测试〉
 *
 * @author claire
 * @date 2021-02-09 - 17:07
 */
public class HCoreJsonTest {

    public static void main(String[] args) {

        /*===============JSON工具-JSONUtil======================*/
        JSONObject json1 = JSONUtil.createObj()
                .put("a", "value1")
                .put("b", "value2")
                .put("c", "value3");

        String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";
        //方法一：使用工具类转换
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        //方法二：new的方式转换
        JSONObject jsonObject2 = new JSONObject(jsonStr);
    }
}
