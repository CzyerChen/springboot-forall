package com.mvc.controller;

import com.mvc.domain.UserVO;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 03 14:26
 */
@Controller
public class TestController {

    @RequestMapping("/")
    public String index(ModelMap map){
        map.addAttribute("host","https://www.github.com/CzyerChen");
        return "hello";
    }


    @PostMapping(value = "/user",
    consumes = MediaType.APPLICATION_XML_VALUE,
    produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public UserVO addUser(@RequestBody UserVO userVO){
        System.out.println("userVO:"+userVO.toString());
        userVO.setUsername("insideUser");
        userVO.setAge(22);
        return userVO;
    }
}
