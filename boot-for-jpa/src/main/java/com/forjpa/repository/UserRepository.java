package com.forjpa.repository;

import com.forjpa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 25 20:20
 */
public interface UserRepository extends Repository<User,Integer> {

    User findByName(String name);


}
