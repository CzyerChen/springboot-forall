package com.shiro.repository;

import com.shiro.domain.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 17:25
 */
public interface TUserRepository extends JpaRepository<TUser,Integer> {

    @Query(value = "select u.password from t_user u where u.username =:username",nativeQuery = true)
    String findPasswordByUsername(@Param("username") String username);

    TUser findByUsername(String username);
}
