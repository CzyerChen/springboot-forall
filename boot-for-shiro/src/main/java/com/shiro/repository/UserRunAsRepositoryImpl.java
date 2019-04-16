package com.shiro.repository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 16 10:04
 */
public class UserRunAsRepositoryImpl  implements UserRunAsRepositoryCustom{
    @Override
    public void grantRunAs(Long fromUserId, Long toUserId) {

    }

    @Override
    public void revokeRunAs(Long fromUserId, Long toUserId) {

    }

    @Override
    public boolean exists(Long fromUserId, Long toUserId) {
        return false;
    }
}
