package com.secure.domain;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 04 13:14
 */
public class IpAuthenticationProvider implements AuthenticationProvider {

    final static Map<String, SimpleGrantedAuthority> ipAuthorityMap = new ConcurrentHashMap<>();
    //维护一个ip白名单列表，每个ip对应一定的权限
    static {
        ipAuthorityMap.put("127.0.0.1", new SimpleGrantedAuthority("ADMIN"));
        ipAuthorityMap.put("172.30.59.75", new SimpleGrantedAuthority("ADMIN"));
        ipAuthorityMap.put("0:0:0:0:0:0:0:1",new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        IpAuthenticationToken ipAuthenticationToken = (IpAuthenticationToken)authentication;
        String ip = ipAuthenticationToken.getIp();
        SimpleGrantedAuthority simpleGrantedAuthority = ipAuthorityMap.get(ip);
        if(simpleGrantedAuthority != null){
            return new IpAuthenticationToken(ip, Collections.singletonList(simpleGrantedAuthority));
        }else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (IpAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
