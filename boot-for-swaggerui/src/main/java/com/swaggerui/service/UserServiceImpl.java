package com.swaggerui.service;

import com.google.common.base.Preconditions;
import com.swaggerui.domain.UserPO;
import com.swaggerui.repository.UserRepository;
import org.hibernate.cfg.beanvalidation.BeanValidationIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
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
    @Autowired
    private Validator validator;

    public List<UserPO> getAllUsers() {
        return userRepository.findAll();
    }

    public UserPO findUserById(int id) {
        Preconditions.checkNotNull(id);
        return userRepository.findById(id);
    }

    public void saveUser(UserPO userPO) {
        userRepository.save(userPO);
    }
}
