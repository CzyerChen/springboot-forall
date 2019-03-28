package com.notes.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 17:35
 */
@RestController
@RequestMapping("/test")
public class SecureTestController {
    @RequestMapping("/home")
    public String index() {
        return "index";
    }
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
