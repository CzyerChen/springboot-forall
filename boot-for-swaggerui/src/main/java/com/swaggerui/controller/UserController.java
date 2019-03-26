package com.swaggerui.controller;

import com.swaggerui.domain.UserPO;
import com.swaggerui.model.ApiResponse;
import com.swaggerui.model.UserNotFoundException;
import com.swaggerui.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 05 16:30
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ApiOperation(value = "全部用户",notes = "获取全部用户数据")
    public ApiResponse testForAllUser(){
        return getResponse("api.success.code","api.success.message",userService.getAllUsers());
    }


    @ResponseBody
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "唯一用户",notes = "根据ID获取用户")
    public ResponseEntity testForAllUser(
            @ApiParam(required = true,name = "id",value = "用户ID")
            @PathVariable String id){
        UserPO userById = userService.findUserById(Integer.valueOf(id));
        if(userById == null){
            throw new UserNotFoundException("用户不存在");
            //throw  new RuntimeException("测试 eroor");
        }
        return ResponseEntity.ok(userById);
    }


}
