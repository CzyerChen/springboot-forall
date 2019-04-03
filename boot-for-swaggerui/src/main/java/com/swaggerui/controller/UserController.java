package com.swaggerui.controller;

import com.swaggerui.domain.UserPO;
import com.swaggerui.model.ApiResponse;
import com.swaggerui.model.UserNotFoundException;
import com.swaggerui.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ApiOperation(value = "全部用户",notes = "获取全部用户数据")
    public ApiResponse testForAllUser(){
        return getResponse("api.success.code","api.success.message",userService.getAllUsers());
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "唯一用户",notes = "根据ID获取用户")
    @ApiImplicitParam(required = true,name = "id",value = "用户ID",dataType = "int")
    public ResponseEntity testFindById(@PathVariable String id){
        UserPO userById = userService.findUserById(Integer.valueOf(id));
        if(userById == null){
            throw new UserNotFoundException("用户不存在");
            //throw  new RuntimeException("测试 eroor");
        }
        return ResponseEntity.ok(userById);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ApiOperation(value = "保存用户",notes = "保存用户信息")
    @ApiImplicitParam(name = "user",value = "用户信息",dataType = "UserPO",required = true)
    public ApiResponse testSaveUser(@RequestBody UserPO user){
        userService.saveUser(user);
        return ApiResponse.builder().code(200L).responseMessage("SUCCESS").build();
    }




}
