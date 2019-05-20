package com.aop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

    @RequestMapping("/aspect")
    public String test(@RequestParam String id){
        System.out.print("test method with id "+id);
        return "SUCCESS : "+ id;
    }
}
