package com.shiro.service;

import org.apache.shiro.cache.Cache;
import org.springframework.stereotype.Service;

/**
 * Desciption  通过缓存交互进行身份判断
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 15 16:22
 */
@Service
public class OAuthService {
    private Cache cache;

    public boolean checkClientId(String clientId){
        //查询缓存，看clientID是否存在
        return true;
    }

    public void addCode(String authCode,String username){
        //添加入缓存，client信息

    }

    public boolean checkClientSecret(String clientSecret){
        //与缓存值对比
        return  true;
    }


    public boolean checkAuthCode(String authCode){
        return true;
    }

    public void addAccessToken(String accessToken,String username){
        //新增
    }

    public String getUsernameByAuthCode(String authCode){
        //查询
        return  "";
    }

    public int getExpireIn(){
        return -1;
    }

    public boolean checkAccessToken(String token){
        return true;
    }

    public String getUsernameByAccessToken(String token){
        return  "";
    }
}
