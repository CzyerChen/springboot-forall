package com.stest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 15:37
 */
public class TestShiro {

    @Test
    public void testA(){
        //1.读取配置，初始化SecurityManager工厂，获取SecurityManager实例
        Factory<SecurityManager> factory  = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager instance = factory.getInstance();
        //2.SecurityUtils绑定SecurityManager实例
        SecurityUtils.setSecurityManager(instance);
        //3.得到Subject的身份认证的subject主体
        Subject subject = SecurityUtils.getSubject();
        //封装用户名密码的对象
        UsernamePasswordToken token  = new UsernamePasswordToken("ziyan", "123456");

        try{
            //4. 认证
            subject.login(token);
        }catch (Exception e){
            //5.认证失败
        }

        Assert.assertEquals(true,subject.isAuthenticated());

        subject.logout();

    }

}
