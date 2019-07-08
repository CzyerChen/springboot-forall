package com.notes.web;

import javafx.scene.chart.ValueAxis;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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


    @RequestMapping("/aspect")
    public void aspect(){
        System.out.print("start to execute aspect method");
    }

    @GetMapping
    public void test6(@RequestParam(value = "username") String username,@RequestParam(value = "age") String age) {

    }


}
