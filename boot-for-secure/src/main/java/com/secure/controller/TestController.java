package com.secure.controller;

import com.secure.domain.Msg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String testSecurityIndex(ModelMap model) {
        Msg msg = new Msg("标题aaa", "内容aaa", "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "index";
    }

    @RequestMapping("/hello")
    public String testSecurityHome() {
        return "hello";
    }

      @RequestMapping("/login")
    public String testSecurityLogin() {
        return "login";
    }

    @RequestMapping("/login1")
    public String testSecurityLogin1() {
        return "login1";
    }

    public void testMethod() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
        } else {
            String s = principal.toString();
        }


    }
}
