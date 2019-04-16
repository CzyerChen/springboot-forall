package com.shiro.controller;

import com.shiro.model.ResponseBo;
import com.shiro.model.UserStatistic;
import com.shiro.service.SessionService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 09 17:03
 */
@Controller
@RequestMapping("/sessions")
public class
SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping("/index")
    public String online(){
        return "sessioninfo";
    }

    /**
     * 对于大量的会话，需要采取分页等技术实现，而非直接获取，会造成sessionDAO的卡顿，影响业务
     * @return
     */
    @ResponseBody
    @GetMapping("/list")
    public List<UserStatistic> list(){
        return sessionService.list();
    }
    @ResponseBody
    @GetMapping("kick")
    public ResponseBo kickOut(String sid){
        try{
            sessionService.kickOut(sid);
            return ResponseBo.ok();
        }catch (Exception e){
            return ResponseBo.error("操作失败");
        }
    }
}
