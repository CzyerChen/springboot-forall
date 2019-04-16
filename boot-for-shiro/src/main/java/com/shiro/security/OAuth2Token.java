package com.shiro.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 15 17:30
 */
public class OAuth2Token implements AuthenticationToken {
    private String authCode;
    private String principal;
    public OAuth2Token(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
