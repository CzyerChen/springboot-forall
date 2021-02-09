/**
 * Author:   claire
 * Date:    2021-02-09 - 17:23
 * Description: http操作测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 17:23          V1.17.0          http操作测试
 */
package com.learning.http;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.*;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.socket.aio.AioServer;
import cn.hutool.socket.nio.NioServer;

import java.util.HashMap;

import static com.sun.tools.javadoc.Main.execute;

/**
 * 功能简述
 * 〈http操作测试〉
 *
 * @author claire
 * @date 2021-02-09 - 17:23
 */
public class HCoreHttpTest {
    public static void main(String[] args) {
        /*=============== HttpUtil ======================*/
        // 最简单的HTTP请求，可以自动通过header等信息判断编码，不区分HTTP和HTTPS
        String result1 = HttpUtil.get("https://www.baidu.com");

        // 当无法识别页面编码的时候，可以自定义请求页面的编码
        String result2 = HttpUtil.get("https://www.baidu.com", CharsetUtil.CHARSET_UTF_8);

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("city", "北京");

        String result3 = HttpUtil.get("https://www.baidu.com", paramMap);

        //链式构建请求
        String result4 = HttpRequest.post("")
                .header(Header.USER_AGENT, "Hutool http")//头信息，多个头信息多次调用此方法即可
                .form(paramMap)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        Console.log(result2);

        HttpResponse res = HttpRequest.post("").execute();
        //预定义的头信息
        Console.log(res.header(Header.CONTENT_ENCODING));
        //自定义头信息
        Console.log(res.header("Content-Disposition"));

        /*=============== HtmlUtil ======================*/
        String html = "<html><body>123'123'</body></html>";
        // 结果为：&lt;html&gt;&lt;body&gt;123&#039;123&#039;&lt;/body&gt;&lt;/html&gt;
        String escape = HtmlUtil.escape(html);

        /*=============== UA工具类-UserAgentUtil ======================*/
        UserAgent ua = UserAgentUtil.parse("");

        ua.getBrowser().toString();//Chrome
        ua.getVersion();//14.0.835.163
        ua.getEngine().toString();//Webkit
        ua.getEngineVersion();//535.1
        ua.getOs().toString();//Windows 7
        ua.getPlatform().toString();//Windows

        /*===============WebSocket ======================*/
        NioServer server = new NioServer(8080);
        AioServer aioServer = new AioServer(8899);
    }
}
