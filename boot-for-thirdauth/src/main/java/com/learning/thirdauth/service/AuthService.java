/**
 * Author:   claire
 * Date:    2023/3/9 - 5:32 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/3/9 - 5:32 下午          V1.0.0
 */
package com.learning.thirdauth.service;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/3/9 - 5:32 下午
 * @since 1.0.0
 */
@Service
public class AuthService {
    @Autowired
    private Environment environment;

//   public String  getAuthorizationUrl(AuthSource source){
//      AuthRequest authRequest = getAuthRequest(source);
//      return authRequest.authorize();
//   }
//
//   private AuthRequest getAuthRequest(AuthSource source) {
//      AuthConfig config = getConfig(source);
//      AuthRequest authRequest = new AuthGiteeRequest(config);
//      return authRequest;
//   }

//   private AuthConfig getConfig(AuthSource source) {
//      AuthConfig config = properties.getConfig(source.getName().toLowerCase());
//      if (config == null) {
//         throw new IllegalArgumentException("无法获取第三方登录配置: " + source);
//      }
//      return config;
//   }
//
//   public AuthResponse login(AuthSource source, AuthRequest authRequest) {
//      AuthConfig config = getConfig(source);
//      AuthResponse response = AuthDefaultRequestExecutor.INSTANCE.execute(config, authRequest);
//
//      if (response.ok()) {
//         // 登录成功，处理用户信息
//         handleAuthSuccess(source, response);
//      } else {
//         // 登录失败，处理错误信息
//         handleAuthFailure(source, response);
//      }
//      return response;
//   }
}
