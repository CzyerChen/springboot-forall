package com.shiro.service;


import com.shiro.domain.TUser;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 15:26
 */
public interface TUserService {
    TUser findUserByUserName(String username);
}
