package com.stest.repository;

import com.stest.domain.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 08 17:25
 */
public interface TUserRepository extends JpaRepository<TUser,Integer> {
}
