package com.secure.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 15:59
 */
@Controller
@RequestMapping("/")
public class TestController {

    @RequestMapping("/")
    public String testSecurityIndex(){
        return "index";
    }
    @RequestMapping("/hello")
    public String testSecurityHome(){
        return "hello";
    }

    @RequestMapping("/login")
    public String testSecurityLogin(){
        return "login";
    }

    public  void testMethod(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
        }else {
            String s = principal.toString();
        }


    }
}
