/**
 * Author:   claire
 * Date:    2023/3/10 - 8:34 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/3/10 - 8:34 上午          V1.0.0
 */
package com.learning.thirdauth.controller;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/3/10 - 8:34 上午
 * @since 1.0.0
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/home")
    public String home2() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginWithUsenamePassword(String username,String password){
        System.out.println(username);
        System.out.println(password);
       return "success";
    }
}