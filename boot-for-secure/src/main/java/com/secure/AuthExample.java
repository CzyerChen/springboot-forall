package com.secure;

import com.secure.domain.ExampleAuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 17:53
 */
public class AuthExample {
    private static AuthenticationManager manager = new ExampleAuthenticationManager();

    public static  void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            System.out.println("username:");
            String username = in.readLine();
            System.out.println("password:");
            String password = in.readLine();

            try{
                Authentication request = new UsernamePasswordAuthenticationToken(username,password);
                Authentication result = manager.authenticate(request);

                SecurityContextHolder.getContext().setAuthentication(request);
                break;
            }catch (Exception e){
                System.out.println("auth fail");
            }
        }
        System.out.println("auth successfully :"+SecurityContextHolder.getContext().getAuthentication());
    }
}
