package com.shiro.service.impl;


import com.shiro.domain.TUser;
import com.shiro.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 15:28
 */
@Service
public class TUserServiceImpl implements TUserService {


    public TUser findUserByUserName(String username) {
        return null;
    }
}
