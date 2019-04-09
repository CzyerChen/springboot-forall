package com.shiro.controller;

import com.shiro.domain.TUser;
import com.shiro.model.ResponseBo;
import com.shiro.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 18:10
 */
@Controller
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseBo login(@RequestParam String username, @RequestParam String password,@RequestParam boolean rememberMe){
        password = MD5Utils.encrypt(username, password);

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password,rememberMe);

        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
            return ResponseBo.ok();
        }catch (UnknownAccountException e){
            return ResponseBo.error(e.getMessage());
        }catch (IncorrectCredentialsException e){
            return ResponseBo.error(e.getMessage());
        }catch (LockedAccountException e){
            return ResponseBo.error(e.getMessage());
        }catch (ExcessiveAttemptsException e){
            return ResponseBo.error(e.getMessage());
        }catch (AuthenticationException e){
            return ResponseBo.error(e.getMessage());
        }
    }



    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        String name = (String) SecurityUtils.getSubject().getPrincipal();
        TUser user = new TUser();
        user.setUsername(name);
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/403")
    public String forbid() {
        return "403";
    }

}
