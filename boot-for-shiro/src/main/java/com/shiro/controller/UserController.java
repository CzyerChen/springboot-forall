package com.shiro.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 09 14:16
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequiresPermissions(value = {"user:query"})
    @RequestMapping("/all")
    public String getAllUsers(ModelMap map){
        map.addAttribute("value","获取全部用户信息");
        return "user";
    }

    @RequiresPermissions("user:add")
    @RequestMapping("/add")
    public String userAdd(ModelMap model) {
        model.addAttribute("value", "新增用户");
        return "user";
    }

    @RequiresRoles(value = {"admin"})
    //@RequiresPermissions("user:delete")
    @RequestMapping("/delete")
    public String userDelete(ModelMap model) {
        model.addAttribute("value", "删除用户");
        return "user";
    }

}
