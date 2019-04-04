package com.secure.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 04 13:12
 */
public class IpAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
     public IpAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/ipVerify"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
         String host = request.getRemoteHost();
        System.out.println("ip:"+host);
        return getAuthenticationManager().authenticate(new IpAuthenticationToken(host));
    }
}
