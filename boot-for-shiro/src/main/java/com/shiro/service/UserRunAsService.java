package com.shiro.service;

import com.shiro.repository.UserRunAsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 16 9:50
 */
@Service
public class UserRunAsService {
    @Autowired
    private UserRunAsRepository userRunAsRepository;

    public void grantRunAs(Long fromUserId, Long toUserId) {

        userRunAsRepository.grantRunAs(fromUserId, toUserId);
    }


    public void revokeRunAs(Long fromUserId, Long toUserId) {
        userRunAsRepository.revokeRunAs(fromUserId, toUserId);
    }


    public boolean exists(Long fromUserId, Long toUserId) {
        return userRunAsRepository.exists(fromUserId, toUserId);
    }


    public List<Long> findFromUserIds(Long toUserId) {
      return   userRunAsRepository.findFromUserIds(toUserId);
    }

    public List<Long> findToUserIds(Long fromUserId) {
        return  userRunAsRepository.findByFromUserId(fromUserId);
    }
}
