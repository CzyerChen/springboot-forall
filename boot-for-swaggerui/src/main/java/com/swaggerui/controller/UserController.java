package com.swaggerui.controller;

import com.swaggerui.domain.UserPO;
import com.swaggerui.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 05 16:30
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ApiOperation(value = "全部用户",notes = "获取全部用户数据")
    public List<UserPO> testForAllUser(){
        return userService.getAllUsers();
    }


    @ResponseBody
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "唯一用户",notes = "根据ID获取用户")
    public UserPO testForAllUser(
            @ApiParam(required = true,name = "id",value = "用户ID")
            @PathVariable String id){
        return userService.findUserById(Integer.valueOf(id));
    }


}
