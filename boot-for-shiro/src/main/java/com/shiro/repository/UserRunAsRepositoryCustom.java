package com.shiro.repository;

import java.util.List;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 16 10:01
 */
public interface UserRunAsRepositoryCustom {
    void grantRunAs(Long fromUserId, Long toUserId);

    void revokeRunAs(Long fromUserId, Long toUserId);

    boolean exists(Long fromUserId, Long toUserId);

}
