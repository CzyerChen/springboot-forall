package com.shiro.repository;

import com.shiro.domain.RunAsIdClass;
import com.shiro.domain.UserRunAs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 16 9:52
 */
public interface UserRunAsRepository extends JpaRepository<UserRunAs, RunAsIdClass> ,UserRunAsRepositoryCustom{

    List<Long> findByFromUserId(Long fromUserId);

    @Query(value = "SELECT  from_user_id FROM sys_user_runas WHERE  to_user_id =:toUserId",nativeQuery = true)
    List<Long> findFromUserIds(Long toUserId);
}
