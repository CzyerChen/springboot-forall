package boot.mybatisplus.config;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * druid 页面访问 :http://127.0.0.1:8080/druid
 * @author: claire  on 2019-06-21 - 16:07
 **/
@WebServlet(urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "allow", value = "127.0.0.1"),// IP白名单 没有配置或者为空，则允许所有访问
                @WebInitParam(name = "deny", value = "192.168.11.11"),// IP黑名单
                @WebInitParam(name = "loginUsername", value = "admin"),// 用户名
                @WebInitParam(name = "loginPassword", value = "admin"),// 密码
                @WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的“Reset All”功能
        })
public class DruidServlet extends StatViewServlet {
    private static final long serialVersionUID = -6085007333934055609L;

}
