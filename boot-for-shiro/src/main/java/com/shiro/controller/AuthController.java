package com.shiro.controller;

import com.shiro.constants.Constants;
import com.shiro.service.ClientService;
import com.shiro.service.OAuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Desciption  服务端：授权控制器
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 15 16:19
 */
@Controller
public class AuthController {
    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private ClientService clientService;

    public Object authorize(Model model, HttpServletRequest request) throws OAuthProblemException, OAuthSystemException, URISyntaxException {
        try {
            OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);
            if (!oAuthService.checkClientId(oAuthAuthzRequest.getClientId())) {
                //不存在clientId
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
            }
            Subject subject = SecurityUtils.getSubject();
            //没有登录
            if (!subject.isAuthenticated()) {
                if (!login(subject, request)) {//登录失败
                    model.addAttribute("client", clientService.findByClientId(oAuthAuthzRequest.getClientId()));
                    return "auth2login";
                }
            }

            String username = (String) subject.getPrincipal();
            //生成授权码
            String authCode = null;
            String requestParam = oAuthAuthzRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (requestParam.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oAuthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                authCode = oAuthIssuerImpl.authorizationCode();
                oAuthService.addCode(authCode, username);
            }

            //进行auth响应
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authCode);
            String url = oAuthAuthzRequest.getParam(OAuth.OAUTH_REDIRECT_URI);//重定向路径

            //响应
            final OAuthResponse response = builder.location(url).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(headers, HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) {
            //认证错误
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                return new ResponseEntity(
                        "OAuth callback url needs to be provided by client!!!", HttpStatus.NOT_FOUND);
            }
            //返回错误消息
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));

        }
    }

    private boolean login(Subject subject, HttpServletRequest request) {
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return false;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //参数不合法
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }

        //进行核心认证身份
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return true;
        } catch (Exception e) {
            request.setAttribute("error", "登录失败:" + e.getClass().getName());
            return false;
        }
    }
}
