package com.swaggerui.service;

import com.swaggerui.domain.UserPO;

import java.util.List;

public interface UserService {

    List<UserPO> getAllUsers();

    UserPO  findUserById(int id);
}
