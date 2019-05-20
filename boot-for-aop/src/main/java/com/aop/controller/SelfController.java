package com.aop.controller;

import com.aop.aspect.SelfAspectAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/self")
public class SelfController {

    @SelfAspectAnnotation(value = "ROLE[admin]")
    @RequestMapping("/aspect")
    public String testSelfMethod(@RequestParam String id){
        System.out.print("self method id:"+id);
        return "success:"+id;
    }
}
