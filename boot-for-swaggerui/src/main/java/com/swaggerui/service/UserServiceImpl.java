package com.swaggerui.service;

import com.swaggerui.domain.UserPO;
import com.swaggerui.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 05 16:25
 */
@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;

    public List<UserPO> getAllUsers() {
        return userRepository.findAll();
    }

    public UserPO findUserById(int id) {
        return userRepository.findById(id);
    }
}
