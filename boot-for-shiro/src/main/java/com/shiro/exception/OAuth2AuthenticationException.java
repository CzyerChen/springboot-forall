package com.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 15 17:44
 */
public class OAuth2AuthenticationException extends AuthenticationException {

    public OAuth2AuthenticationException(Throwable cause) {
        super(cause);
    }
}
