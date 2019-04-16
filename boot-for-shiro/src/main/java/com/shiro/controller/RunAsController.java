package com.shiro.controller;

import com.shiro.domain.User;
import com.shiro.service.UserRunAsService;
import com.shiro.utils.CurrentUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 16 10:13
 */
@RestController
@RequestMapping("/runas")
public class RunAsController {
    @Autowired
    private UserRunAsService userRunAsService;

    /**
     * 查看所有授权
     * @param loginUser
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String runasList(@CurrentUser User loginUser, Model model) {
        model.addAttribute("fromUserIds",
                userRunAsService.findFromUserIds(loginUser.getId()));
        model.addAttribute("toUserIds", userRunAsService.findToUserIds(loginUser.getId()));
        /*List<User> allUsers = userService.findAll();
        allUsers.remove(loginUser);
        model.addAttribute("allUsers", allUsers);*/
        Subject subject = SecurityUtils.getSubject();
        model.addAttribute("isRunas", subject.isRunAs());
        if(subject.isRunAs()) {
            String previousUsername =
                    (String)subject.getPreviousPrincipals().getPrimaryPrincipal();
            model.addAttribute("previousUsername", previousUsername);
        }
        return "runas";
    }

    /**
     * 授予角色
     * @param loginUser
     * @param toUserId
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("/grant/{toUserId}")
    public String grant(@CurrentUser User loginUser, @PathVariable("toUserId") Long toUserId, RedirectAttributes redirectAttributes) {
        if(loginUser.getId().equals(toUserId)) {
            redirectAttributes.addFlashAttribute("msg", "自己不能切换到自己的身份");
            return "redirect:/runas";
        }
        userRunAsService.grantRunAs(loginUser.getId(), toUserId);
        redirectAttributes.addFlashAttribute("msg", "操作成功");
        return "redirect:/runas";
    }


    /**
     * 回收
     * @param loginUser
     * @param toUserId
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("/revoke/{toUserId}")
    public String revoke(@CurrentUser User loginUser, @PathVariable("toUserId") Long toUserId, RedirectAttributes redirectAttributes) {
        userRunAsService.revokeRunAs(loginUser.getId(), toUserId);
        redirectAttributes.addFlashAttribute("msg", "操作成功");
        return "redirect:/runas";
    }

    /**
     * 切换身份
     * @param loginUser
     * @param switchToUserId
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("/switchTo/{switchToUserId}")
    public String switchTo(@CurrentUser User loginUser, @PathVariable("switchToUserId") Long switchToUserId, RedirectAttributes redirectAttributes) {
        Subject subject = SecurityUtils.getSubject();
        User switchToUser = null;//userService.findOne(switchToUserId);
        if(loginUser.equals(switchToUser)) {
            redirectAttributes.addFlashAttribute("msg", "自己不能切换到自己的身份");
            return "redirect:/runas";
        }
        if(switchToUser == null || !userRunAsService.exists(switchToUserId, loginUser.getId())) {
            redirectAttributes.addFlashAttribute("msg", "对方没有授予您身份，不能切换");
            return "redirect:/runas";
        }
        subject.runAs(new SimplePrincipalCollection(switchToUser.getUsername(), ""));
        redirectAttributes.addFlashAttribute("msg", "操作成功");
        redirectAttributes.addFlashAttribute("needRefresh", "true");
        return "redirect:/runas";
    }


    /**
     * 切回上一个身份
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("/switchBack")
    public String switchBack(RedirectAttributes redirectAttributes) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isRunAs()) {
            //依靠subject通过栈保存的身份授权信息，可以直接切回上一个身份
            subject.releaseRunAs();
        }
        redirectAttributes.addFlashAttribute("msg", "操作成功");
        redirectAttributes.addFlashAttribute("needRefresh", "true");
        return "redirect:/runas";
    }
}
